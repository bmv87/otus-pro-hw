package ru.otus.pro.hw.bank.service.impl;

import ru.otus.pro.hw.bank.dao.AgreementDao;
import ru.otus.pro.hw.bank.entity.Agreement;
import ru.otus.pro.hw.bank.service.AgreementService;

import java.util.Optional;

public class AgreementServiceImpl implements AgreementService {

    private AgreementDao agreementDao;

    public AgreementServiceImpl(AgreementDao agreementDao) {
        this.agreementDao = agreementDao;
    }

    @Override
    public Agreement addAgreement(String name) {
        Agreement agreement = new Agreement();
        agreement.setName(name);

        return agreementDao.save(agreement);
    }

    public Optional<Agreement> findByName(String name) {
        return agreementDao.findByName(name);
    }


}
