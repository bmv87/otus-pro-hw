package ru.otus.pro.hw.jee.web.calculator;

import javax.naming.OperationNotSupportedException;

public class Calculator {
    public enum Operator {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIV;
    }

    public static int calculate(Operator operator, Integer left, Integer right) throws OperationNotSupportedException {

        if (operator == null) {
            throw new IllegalArgumentException("Invalid operator");
        }

        switch (operator) {
            case ADD:
                return left + right;
            case SUBTRACT:
                return left - right;
            case MULTIPLY:
                return left * right;
            case DIV:
                if (right == 0) {
                    throw new OperationNotSupportedException("division by zero");
                }
                return left / right;
            default:
                throw new OperationNotSupportedException(operator.toString());
        }
    }
}
