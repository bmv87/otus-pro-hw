package ru.otus.pro.hw.serialization.models.in;

import lombok.Getter;

public enum SmsStatus {
    DELIVERED(0),
    DELIVERY_ERROR(1);
    @Getter
    private final int value;

    SmsStatus(int val) {
        value = val;
    }
}
