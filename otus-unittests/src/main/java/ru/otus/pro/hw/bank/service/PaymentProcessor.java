package ru.otus.pro.hw.bank.service;

import ru.otus.pro.hw.bank.entity.Agreement;

import java.math.BigDecimal;


public interface PaymentProcessor {

    boolean makeTransfer(Agreement source, Agreement destination, int sourceType, int destinationType, BigDecimal amount);

    boolean makeTransferWithComission(Agreement source, Agreement destination,
                                      int sourceType, int destinationType,
                                      BigDecimal amount,
                                      BigDecimal comissionPercent);
}

