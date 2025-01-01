package ru.otus.pro.hw.crud.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class ErrorVM {
    private int code;
    private String message;
    private String stackTrace;
}