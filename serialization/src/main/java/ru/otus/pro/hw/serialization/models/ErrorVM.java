package ru.otus.pro.hw.serialization.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class ErrorVM {
    protected int code;
    protected String message;
    protected String stackTrace;
}