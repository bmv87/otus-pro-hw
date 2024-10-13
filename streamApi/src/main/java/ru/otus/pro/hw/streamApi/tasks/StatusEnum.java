package ru.otus.pro.hw.streamApi.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    OPENED("Открыта"),
    IN_PROCESS("В работе"),
    CLOSED("Закрыта");

    private final String description;
}
