package ru.otus.pro.hw.bank.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.pro.hw.bank.dao.AccountDao;
import ru.otus.pro.hw.bank.entity.Account;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplParametrizedTest {
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

    @ParameterizedTest
    @MethodSource("provideParameters")
    public void testTransferValidation(
            BigDecimal sourceAmount,
            BigDecimal destinationAmount,
            BigDecimal transferAmount,
            Boolean expectedResult,
            BigDecimal expectedSourceAmount,
            BigDecimal expectedDestinationAmount) {
        Account sourceAccount = configAccount(1L, sourceAmount);
        Account destinationAccount = configAccount(2L, destinationAmount);

        assertEquals(expectedResult, accountServiceImpl.makeTransfer(1L, 2L, transferAmount));

        assertEquals(expectedSourceAmount, sourceAccount.getAmount());
        assertEquals(expectedDestinationAmount, destinationAccount.getAmount());
    }

    @ParameterizedTest
    @MethodSource("provideParametersForCharge")
    public void testChargeMethod(BigDecimal sourceAmount, BigDecimal transferAmount, boolean expected, BigDecimal expectedAmount) {

        Account sourceAccount = configAccount(1L, sourceAmount);
        assertEquals(expected, accountServiceImpl.charge(1L, transferAmount));
        assertEquals(expectedAmount, sourceAccount.getAmount());
    }


    @ParameterizedTest
    @MethodSource("provideParametersForChargeWithVerify")
    public void testChargeWithVerify(BigDecimal sourceAmount, BigDecimal transferAmount, BigDecimal expectedAmount) {
        Account sourceAccount = configAccount(1L, sourceAmount);

        ArgumentMatcher<Account> sourceMatcher =
                argument -> argument.getId().equals(1L) && argument.getAmount().equals(expectedAmount);

        accountServiceImpl.charge(1L, transferAmount);

        verify(accountDao).save(argThat(sourceMatcher));
    }

    private static Stream<? extends Arguments> provideParameters() {
        return Stream.of(
                Arguments.of(new BigDecimal(100), new BigDecimal(100), new BigDecimal(10), true, new BigDecimal(90), new BigDecimal(110)),
                Arguments.of(new BigDecimal(10), new BigDecimal(100), new BigDecimal(10), true, new BigDecimal(0), new BigDecimal(110)),
                Arguments.of(new BigDecimal(10), new BigDecimal(100), new BigDecimal(100), false, new BigDecimal(10), new BigDecimal(100)),
                Arguments.of(new BigDecimal(100), new BigDecimal(100), new BigDecimal(0), false, new BigDecimal(100), new BigDecimal(100)),
                Arguments.of(new BigDecimal(100), new BigDecimal(100), new BigDecimal(-1), false, new BigDecimal(100), new BigDecimal(100))
        );
    }

    private static Stream<? extends Arguments> provideParametersForCharge() {
        return Stream.of(
                Arguments.of(new BigDecimal(100), new BigDecimal(-50), true, new BigDecimal(50)),
                Arguments.of(new BigDecimal(100), new BigDecimal(50), true, new BigDecimal(150)),
                Arguments.of(new BigDecimal(0), new BigDecimal(50), true, new BigDecimal(50)),
                Arguments.of(new BigDecimal(0), new BigDecimal(-50), false, new BigDecimal(0)),
                Arguments.of(new BigDecimal(50), new BigDecimal(0), false, new BigDecimal(50))
        );
    }


    private static Stream<? extends Arguments> provideParametersForChargeWithVerify() {
        return Stream.of(
                Arguments.of(new BigDecimal(100), new BigDecimal(-50), new BigDecimal(50)),
                Arguments.of(new BigDecimal(100), new BigDecimal(50), new BigDecimal(150))
        );
    }
}
