package ru.otus.pro.hw.rest.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ValidationErrorVM extends ErrorVM {

    private Map<String, List<String>> errors;
}
