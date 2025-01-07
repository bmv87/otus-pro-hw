package ru.otus.pro.hw.webServer.processors;

import ru.otus.pro.hw.webServer.exceptions.UnprocessableEntityException;
import ru.otus.pro.hw.webServer.http.HttpMethod;
import ru.otus.pro.hw.webServer.models.CalculatorResultVM;
import ru.otus.pro.hw.webServer.routing.ParamVariable;
import ru.otus.pro.hw.webServer.routing.Processor;
import ru.otus.pro.hw.webServer.routing.RoutePath;

@Processor()
public class CalculatorProcessor {
    @RoutePath(method = HttpMethod.GET, path = "calculator")
    public CalculatorResultVM calculate(
            @ParamVariable(name = "argLeft", required = true) Integer argLeft,
            @ParamVariable(name = "argRight", required = true) Integer argRight,
            @ParamVariable(name = "operation", required = true) String operation
    ) {
        var result = new CalculatorResultVM();
        result.setArgLeft(argLeft);
        result.setArgRight(argRight);
        result.setOperation(operation);
        switch (operation) {
            case "multiplication":
                result.setResult(argLeft * argRight);
                break;
            case "division":
                if (argRight == 0) {
                    throw new UnprocessableEntityException("Деление на 0 не поддерживается!");
                }
                result.setResult(argLeft / argRight);

                break;
            case "summ":
                result.setResult(argLeft + argRight);
                break;
            case "subtraction":
                result.setResult(argLeft - argRight);
                break;
            default:
                throw new UnprocessableEntityException("Операция не поддерживается!");
        }

        return result;
    }
}
