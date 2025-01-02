package ru.otus.pro.hw.rest.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
public class ErrorVM {
    protected int code;
    protected String message;
    protected String stackTrace;
    protected LocalDateTime dateTime;
}