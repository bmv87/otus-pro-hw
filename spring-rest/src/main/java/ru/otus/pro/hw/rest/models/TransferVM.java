package ru.otus.pro.hw.rest.models;

public record TransferVM(String id, String clientId, String targetClientId, String sourceAccount, String targetAccount, String message,
                         int amount) {
}