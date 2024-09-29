package ru.otus.pro.hw.bank.service.impl;

import ru.otus.pro.hw.bank.entity.Account;
import ru.otus.pro.hw.bank.entity.Agreement;
import ru.otus.pro.hw.bank.service.AccountService;
import ru.otus.pro.hw.bank.service.PaymentProcessor;
import ru.otus.pro.hw.bank.service.exception.AccountException;

import java.math.BigDecimal;

public class PaymentProcessorImpl implements PaymentProcessor {
    private AccountService accountService;

    public PaymentProcessorImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    public boolean makeTransfer(Agreement source, Agreement destination, int sourceType,
                                int destinationType, BigDecimal amount) {

        Account sourceAccount = accountService.getAccounts(source).stream()
                .filter(account -> account.getType() == sourceType)
                .findAny()
                .orElseThrow(() -> new AccountException("No source account"));

        Account destinationAccount = accountService.getAccounts(destination).stream()
                .filter(account -> account.getType() == destinationType)
                .findAny()
                .orElseThrow(() -> new AccountException("No destination account"));

        return accountService.makeTransfer(sourceAccount.getId(), destinationAccount.getId(), amount);
    }

    @Override
    public boolean makeTransferWithComission(Agreement source,
                                             Agreement destination,
                                             int sourceType,
                                             int destinationType,
                                             BigDecimal amount,
                                             BigDecimal comissionPercent) {

        Account sourceAccount = accountService.getAccounts(source).stream()
                .filter(account -> account.getType() == sourceType)
                .findAny()
                .orElseThrow(() -> new AccountException("No source account"));

        Account destinationAccount = accountService.getAccounts(destination).stream()
                .filter(account -> account.getType() == destinationType)
                .findAny()
                .orElseThrow(() -> new AccountException("No destination account"));
        var commissionValue = amount.multiply(comissionPercent);
        var amountWithCommition = amount.add(commissionValue);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        if (sourceAccount.getAmount().compareTo(amountWithCommition) < 0) {
            return false;
        }

        if (!accountService.charge(sourceAccount.getId(), commissionValue.negate())) {
            return false;
        }

        return accountService.makeTransfer(sourceAccount.getId(), destinationAccount.getId(), amount);
    }
}
