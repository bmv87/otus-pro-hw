package ru.otus.pro.hw.bank.service;

import ru.otus.pro.hw.bank.entity.Agreement;

import java.util.Optional;

public interface AgreementService {
    Agreement addAgreement(String name);

    Optional<Agreement> findByName(String name);
}
