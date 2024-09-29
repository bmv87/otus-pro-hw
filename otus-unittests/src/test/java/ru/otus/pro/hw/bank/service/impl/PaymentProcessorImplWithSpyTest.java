package ru.otus.pro.hw.bank.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.otus.pro.hw.bank.dao.AccountDao;
import ru.otus.pro.hw.bank.entity.Account;
import ru.otus.pro.hw.bank.entity.Agreement;
import ru.otus.pro.hw.bank.service.exception.AccountException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class PaymentProcessorImplWithSpyTest {

    @Mock
    AccountDao accountDao;

    @Spy
    @InjectMocks
    AccountServiceImpl accountService;

    @InjectMocks
    PaymentProcessorImpl paymentProcessor;

    @BeforeEach
    public void init() {
        paymentProcessor = new PaymentProcessorImpl(accountService);
    }


    private Agreement configAgreement(Long agreementId) {
        Agreement agreement = new Agreement();
        agreement.setId(agreementId);
        return agreement;
    }

    private Account configAccount(Long agreementId, Long accountId, Integer type, BigDecimal amount, boolean configAccountDao) {
        Account account = new Account();
        account.setId(accountId);
        account.setAmount(amount);
        account.setType(type);
        doReturn(List.of(account)).when(accountService).getAccounts(argThat(argument -> argument != null && argument.getId().equals(agreementId)));
        if (configAccountDao) {
            when(accountDao.findById(accountId)).thenReturn(Optional.of(account));
        }
        return account;
    }


    @ParameterizedTest
    @MethodSource("provideParametersForMakeTransfer")
    public void testMakeTransfer(BigDecimal sourceAmount,
                                 BigDecimal destinationAmount,
                                 BigDecimal transferAmount,
                                 Boolean expectedResult,
                                 BigDecimal expectedSourceAmount,
                                 BigDecimal expectedDestinationAmount) {
        Agreement sourceAgreement = configAgreement(1L);

        Agreement destinationAgreement = configAgreement(2L);

        Account sourceAccount = configAccount(sourceAgreement.getId(), 10L, 0, sourceAmount, true);

        Account destinationAccount = configAccount(destinationAgreement.getId(), 20L, 0, destinationAmount, true);

        var reset = paymentProcessor.makeTransfer(
                sourceAgreement,
                destinationAgreement,
                sourceAccount.getType(),
                destinationAccount.getType(),
                transferAmount);

        assertEquals(expectedResult, reset);
        assertEquals(expectedSourceAmount, sourceAccount.getAmount());
        assertEquals(expectedDestinationAmount, destinationAccount.getAmount());
    }

    @ParameterizedTest
    @MethodSource("provideParametersForMakeTransferWithComission")
    public void testMakeTransferWithComission(BigDecimal sourceAmount,
                                              BigDecimal destinationAmount,
                                              BigDecimal transferAmount,
                                              BigDecimal comission,
                                              Boolean expectedResult,
                                              BigDecimal expectedSourceAmount,
                                              BigDecimal expectedDestinationAmount) {
        Agreement sourceAgreement = configAgreement(1L);

        Agreement destinationAgreement = configAgreement(2L);

        Account sourceAccount = configAccount(sourceAgreement.getId(), 10L, 0, sourceAmount, !(sourceAmount.compareTo(transferAmount) <= 0 || transferAmount.compareTo(BigDecimal.ZERO) <= 0));

        Account destinationAccount = configAccount(destinationAgreement.getId(), 20L, 0, destinationAmount, expectedResult);

        var reset = paymentProcessor.makeTransferWithComission(
                sourceAgreement,
                destinationAgreement,
                sourceAccount.getType(),
                destinationAccount.getType(),
                transferAmount,
                comission);

        assertEquals(expectedResult, reset);
        assertEquals(expectedSourceAmount, sourceAccount.getAmount());
        assertEquals(expectedDestinationAmount, destinationAccount.getAmount());
    }

    @Test
    public void testMakeTransferSourceNotFound() {
        Agreement sourceAgreement = configAgreement(1L);
        Agreement destinationAgreement = configAgreement(2L);

        AccountException result = assertThrows(AccountException.class,
                () -> paymentProcessor.makeTransfer(sourceAgreement, destinationAgreement, 0, 0, new BigDecimal(10)));
        assertEquals("No source account", result.getLocalizedMessage());
    }

    @Test
    public void testMakeTransferDistinationNotFound() {
        Agreement sourceAgreement = configAgreement(1L);
        Agreement destinationAgreement = configAgreement(2L);

        Account sourceAccount = configAccount(sourceAgreement.getId(), 1L, 0, new BigDecimal(100), false);
        when(accountService.getAccounts(sourceAgreement)).thenReturn(List.of(sourceAccount));

        AccountException result = assertThrows(AccountException.class,
                () -> paymentProcessor.makeTransfer(sourceAgreement, destinationAgreement, 0, 0, new BigDecimal(10)));
        assertEquals("No destination account", result.getLocalizedMessage());
    }

    @Test
    public void testMakeTransferWithComissionSourceNotFound() {
        Agreement sourceAgreement = configAgreement(1L);
        Agreement destinationAgreement = configAgreement(2L);

        AccountException result = assertThrows(AccountException.class,
                () -> paymentProcessor.makeTransferWithComission(sourceAgreement, destinationAgreement, 0, 0, new BigDecimal(10), new BigDecimal("0.1")));
        assertEquals("No source account", result.getLocalizedMessage());
    }

    @Test
    public void testMakeTransferWithComissionDistinationNotFound() {
        Agreement sourceAgreement = configAgreement(1L);
        Agreement destinationAgreement = configAgreement(2L);

        Account sourceAccount = configAccount(sourceAgreement.getId(), 1L, 0, new BigDecimal(100), false);
        when(accountService.getAccounts(sourceAgreement)).thenReturn(List.of(sourceAccount));

        AccountException result = assertThrows(AccountException.class,
                () -> paymentProcessor.makeTransferWithComission(sourceAgreement, destinationAgreement, 0, 0, new BigDecimal(10), new BigDecimal("0.1")));
        assertEquals("No destination account", result.getLocalizedMessage());
    }

    private static Stream<? extends Arguments> provideParametersForMakeTransfer() {
        return Stream.of(
                Arguments.of(new BigDecimal(100), new BigDecimal(100), new BigDecimal(10), true, new BigDecimal(90), new BigDecimal(110)),
                Arguments.of(new BigDecimal(10), new BigDecimal(100), new BigDecimal(10), true, new BigDecimal(0), new BigDecimal(110)),
                Arguments.of(new BigDecimal(10), new BigDecimal(100), new BigDecimal(100), false, new BigDecimal(10), new BigDecimal(100)),
                Arguments.of(new BigDecimal(100), new BigDecimal(100), new BigDecimal(0), false, new BigDecimal(100), new BigDecimal(100)),
                Arguments.of(new BigDecimal(100), new BigDecimal(100), new BigDecimal(-1), false, new BigDecimal(100), new BigDecimal(100))
        );
    }

    private static Stream<? extends Arguments> provideParametersForMakeTransferWithComission() {
        return Stream.of(
                Arguments.of(new BigDecimal(100), new BigDecimal(100), new BigDecimal(10), new BigDecimal("0.1"), true, new BigDecimal("89.0"), new BigDecimal(110)),
                Arguments.of(new BigDecimal(100), new BigDecimal(100), new BigDecimal(10), new BigDecimal("0.01"), true, new BigDecimal("89.90"), new BigDecimal(110)),
                Arguments.of(new BigDecimal(10), new BigDecimal(100), new BigDecimal(10), new BigDecimal("0.1"), false, new BigDecimal(10), new BigDecimal(100)),
                Arguments.of(new BigDecimal(10), new BigDecimal(100), new BigDecimal(100), new BigDecimal("0.1"), false, new BigDecimal(10), new BigDecimal(100)),
                Arguments.of(new BigDecimal(100), new BigDecimal(100), new BigDecimal(0), new BigDecimal("0.1"), false, new BigDecimal(100), new BigDecimal(100)),
                Arguments.of(new BigDecimal(100), new BigDecimal(100), new BigDecimal(-1), new BigDecimal("0.1"), false, new BigDecimal(100), new BigDecimal(100))
        );
    }

}
