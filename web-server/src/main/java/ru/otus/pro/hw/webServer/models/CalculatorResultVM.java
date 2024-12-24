package ru.otus.pro.hw.webServer.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CalculatorResultVM {
    private int argLeft;
    private int argRight;
    private String operation;
    private int result;
}
