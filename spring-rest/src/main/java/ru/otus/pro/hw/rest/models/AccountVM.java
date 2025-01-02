package ru.otus.pro.hw.rest.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountVM {
    private String id;
    private String clientId;
    private String accountNumber;
}
