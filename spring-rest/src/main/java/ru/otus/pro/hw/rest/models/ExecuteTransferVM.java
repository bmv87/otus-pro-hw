package ru.otus.pro.hw.rest.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ExecuteTransferVM(@NotEmpty String sourceAccountId, @NotEmpty String targetAccountId, String message,
                                @NotNull Integer amount) {
}