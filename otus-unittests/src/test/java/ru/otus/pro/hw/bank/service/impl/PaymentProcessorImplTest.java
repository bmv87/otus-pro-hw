package ru.otus.pro.hw.bank.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.pro.hw.bank.entity.Account;
import ru.otus.pro.hw.bank.entity.Agreement;
import ru.otus.pro.hw.bank.service.AccountService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentProcessorImplTest {

    @Mock
    AccountService accountService;

    @InjectMocks
    PaymentProcessorImpl paymentProcessor;


    private Agreement configAgreement(Long agreementId) {
        Agreement agreement = new Agreement();
        agreement.setId(agreementId);
        return agreement;
    }

    private Account configAccount(Long agreementId, Integer type, BigDecimal amount) {
        Account account = new Account();
        account.setAmount(amount);
        account.setType(type);
        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId().equals(agreementId)))).thenReturn(List.of(account));
        return account;
    }


    @Test
    public void testMakeTransfer() {
        Agreement sourceAgreement = configAgreement(1L);
        Agreement destinationAgreement = configAgreement(2L);

        Account sourceAccount = configAccount(sourceAgreement.getId(), 0, BigDecimal.TEN);

        Account destinationAccount = configAccount(destinationAgreement.getId(), 0, BigDecimal.ZERO);
        ArgumentCaptor<Long> captor1 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> captor2 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<BigDecimal> captor3 = ArgumentCaptor.forClass(BigDecimal.class);

        when(accountService.makeTransfer(captor1.capture(), captor2.capture(), captor3.capture())).thenReturn(true);

        var result = paymentProcessor.makeTransfer(sourceAgreement, destinationAgreement,
                sourceAccount.getType(), destinationAccount.getType(), BigDecimal.ONE);
        assertTrue(result);
        assertEquals(sourceAccount.getId(), captor1.getValue());
        assertEquals(destinationAccount.getId(), captor2.getValue());
        assertEquals(BigDecimal.ONE, captor3.getValue());
    }

    @Test
    public void testMakeTransferWithComission() {
        Agreement sourceAgreement = configAgreement(1L);
        Agreement destinationAgreement = configAgreement(2L);
        var transferAmount = new BigDecimal(10);
        var comission = new BigDecimal("0.1");

        Account sourceAccount = configAccount(sourceAgreement.getId(), 0, new BigDecimal(100));
        var comissionValue = transferAmount.multiply(comission).negate();

        Account destinationAccount = configAccount(destinationAgreement.getId(), 0, new BigDecimal(0));
        ArgumentCaptor<Long> captor1_1 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<BigDecimal> captor2_1 = ArgumentCaptor.forClass(BigDecimal.class);
        ArgumentCaptor<Long> captor1_2 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> captor2_2 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<BigDecimal> captor3_2 = ArgumentCaptor.forClass(BigDecimal.class);

        when(accountService.charge(captor1_1.capture(), captor2_1.capture())).thenReturn(true);
        when(accountService.makeTransfer(captor1_2.capture(), captor2_2.capture(), captor3_2.capture())).thenReturn(true);

        var result = paymentProcessor.makeTransferWithComission(
                sourceAgreement,
                destinationAgreement,
                sourceAccount.getType(),
                destinationAccount.getType(),
                transferAmount,
                comission);
        assertTrue(result);
        assertEquals(sourceAccount.getId(), captor1_1.getValue());
        assertEquals(comissionValue, captor2_1.getValue());
        assertEquals(sourceAccount.getId(), captor1_2.getValue());
        assertEquals(destinationAccount.getId(), captor2_2.getValue());
        assertEquals(transferAmount, captor3_2.getValue());
    }

    @Test
    public void testMakeTransferWithComissionChargeFalse() {
        Agreement sourceAgreement = configAgreement(1L);
        Agreement destinationAgreement = configAgreement(2L);
        var transferAmount = new BigDecimal(10);
        var comission = new BigDecimal("0.1");

        Account sourceAccount = configAccount(sourceAgreement.getId(), 0, new BigDecimal(100));

        Account destinationAccount = configAccount(destinationAgreement.getId(), 0, new BigDecimal(0));

        when(accountService.charge(any(), any())).thenReturn(false);

        var result = paymentProcessor.makeTransferWithComission(
                sourceAgreement,
                destinationAgreement,
                sourceAccount.getType(),
                destinationAccount.getType(),
                transferAmount,
                comission);

        verify(accountService, times(1)).charge(any(), any());
        verify(accountService, times(0)).makeTransfer(any(), any(), any());

        assertFalse(result);
    }

    @Test
    public void testMakeTransferWithComissionChargeTrue() {
        Agreement sourceAgreement = configAgreement(1L);
        Agreement destinationAgreement = configAgreement(2L);
        var transferAmount = new BigDecimal(10);
        var comission = new BigDecimal("0.1");

        Account sourceAccount = configAccount(sourceAgreement.getId(), 0, new BigDecimal(100));

        Account destinationAccount = configAccount(destinationAgreement.getId(), 0, new BigDecimal(0));

        when(accountService.charge(any(), any())).thenReturn(true);

        var result = paymentProcessor.makeTransferWithComission(
                sourceAgreement,
                destinationAgreement,
                sourceAccount.getType(),
                destinationAccount.getType(),
                transferAmount,
                comission);

        verify(accountService, times(1)).charge(any(), any());
        verify(accountService, times(1)).makeTransfer(any(), any(), any());

        assertFalse(result);
    }


}
