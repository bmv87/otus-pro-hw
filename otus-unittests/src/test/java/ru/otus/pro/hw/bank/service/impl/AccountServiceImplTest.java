package ru.otus.pro.hw.bank.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.pro.hw.bank.dao.AccountDao;
import ru.otus.pro.hw.bank.entity.Account;
import ru.otus.pro.hw.bank.entity.Agreement;
import ru.otus.pro.hw.bank.service.exception.AccountException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    AccountDao accountDao;

    @InjectMocks
    AccountServiceImpl accountServiceImpl;

    private Account configAccount(Long accountId, BigDecimal sourceAmount) {
        Account account = new Account();
        account.setAmount(sourceAmount);
        account.setId(accountId);

        when(accountDao.findById(eq(accountId))).thenReturn(Optional.of(account));
        return account;
    }

    @Test
    public void testAddAccount() {

        Agreement agreement = new Agreement();
        agreement.setId(1L);
        agreement.setName("Client1");
        var accountNumber = agreement.getName() + "_acc1";
        ArgumentMatcher<Account> argMatcher =
                argument -> argument.getAgreementId().equals(agreement.getId()) &&
                        argument.getAmount().equals(new BigDecimal(1000)) &&
                        argument.getNumber().equals(accountNumber) &&
                        argument.getType().equals(0);

        accountServiceImpl.addAccount(agreement, accountNumber, 0, new BigDecimal(1000));

        verify(accountDao).save(argThat(argMatcher));
    }

    @Test
    public void testGetAgreementAccounts() {

        Agreement agreement = new Agreement();
        agreement.setId(1L);
        agreement.setName("Client1");
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        when(accountDao.findByAgreementId(captor.capture())).thenReturn(List.of());

        accountServiceImpl.getAccounts(agreement);
        Assertions.assertEquals(agreement.getId(), captor.getValue());
    }

    @Test
    public void testGetAgreementAccountsFromRealSource() {

        Agreement agreement = new Agreement();
        agreement.setId(1L);
        agreement.setName("Client1");
        var accountNumber = agreement.getName() + "_acc_";

        var accountService = new AccountServiceImpl(new AccountDao());
        accountService.addAccount(agreement, accountNumber + "1", 0, new BigDecimal(1000));
        accountService.addAccount(agreement, accountNumber + "2", 0, new BigDecimal(10));
        var result = accountService.getAccounts(agreement);
        assertTrue(result.size() == 2);
        assertTrue(result.stream().allMatch(i -> i.getAgreementId().equals(agreement.getId())));
    }

    @Test
    public void testGetAccountsFromRealSource() {

        Agreement agreement = new Agreement();
        agreement.setId(1L);
        agreement.setName("Client1");
        var accountNumber = agreement.getName() + "_acc_";

        var accountService = new AccountServiceImpl(new AccountDao());
        accountService.addAccount(agreement, accountNumber + "1", 0, new BigDecimal(1000));
        accountService.addAccount(agreement, accountNumber + "2", 0, new BigDecimal(10));
        var result = accountService.getAccounts();
        assertTrue(result.size() >= 2);
        assertTrue(result.stream().anyMatch(i -> i.getAgreementId().equals(agreement.getId())));
    }

    @Test
    public void testMakeTransferSourceNotFound() {
        when(accountDao.findById(eq(1L))).thenReturn(Optional.empty());

        AccountException result = assertThrows(AccountException.class,
                () -> accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10)));
        assertEquals("No source account", result.getLocalizedMessage());
    }

    @Test
    public void testMakeTransferDistinationNotFound() {
        Account sourceAccount = configAccount(1L, new BigDecimal(100));

        when(accountDao.findById(eq(2L))).thenReturn(Optional.empty());

        AccountException result = assertThrows(AccountException.class,
                () -> accountServiceImpl.makeTransfer(sourceAccount.getId(), 2L, new BigDecimal(10)));
        assertEquals("No destination account", result.getLocalizedMessage());
    }

    @Test
    public void testChargeSourceNotFound() {
        when(accountDao.findById(any())).thenReturn(Optional.empty());

        AccountException result = assertThrows(AccountException.class,
                () -> accountServiceImpl.charge(1L, new BigDecimal(10)));
        assertEquals("No source account", result.getLocalizedMessage());
    }


    @Test
    public void testTransferWithVerify() {
        Account sourceAccount = configAccount(1L, new BigDecimal(100));
        Account destinationAccount = configAccount(2L, new BigDecimal(10));

        ArgumentMatcher<Account> sourceMatcher =
                argument -> argument.getId().equals(1L) && argument.getAmount().equals(new BigDecimal(90));

        ArgumentMatcher<Account> destinationMatcher =
                argument -> argument.getId().equals(2L) && argument.getAmount().equals(new BigDecimal(20));
        accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));

        verify(accountDao).save(argThat(sourceMatcher));
        verify(accountDao).save(argThat(destinationMatcher));
    }
}
