package ru.otus.pro.hw.bank.service.exception;

public class AccountException extends RuntimeException {
    public AccountException(String message) {
        super(message);
    }
}
