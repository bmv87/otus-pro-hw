package ru.otus.pro.hw.rest.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.pro.hw.rest.entities.Transfer;
import ru.otus.pro.hw.rest.exceptions.ValidationException;
import ru.otus.pro.hw.rest.models.ExecuteTransferVM;
import ru.otus.pro.hw.rest.repositories.TransfersRepository;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class TransfersService {
    private final TransfersRepository transfersRepository;
    private final AccountsService accountsService;

    public Optional<Transfer> getTransferById(String id, String clientId) {
        return transfersRepository.findByIdAndClientId(id, clientId);
    }

    public List<Transfer> getAllTransfers(String clientId) {
        return transfersRepository.findAllByClientId(clientId);
    }

    @Transactional
    public void execute(String clientId, ExecuteTransferVM executeTransferVM) {
        log.debug(String.format("transfer funds client %s %d rub", clientId, executeTransferVM.amount()));
        validateExecuteTransferVM(executeTransferVM);
        accountsService.withdrawFunds(clientId, executeTransferVM.sourceAccountId(), executeTransferVM.amount());
        accountsService.addFunds(executeTransferVM.targetAccountId(), executeTransferVM.amount());
        var transfer = new Transfer(
                UUID.randomUUID().toString(),
                executeTransferVM.sourceAccountId(),
                executeTransferVM.targetAccountId(),
                executeTransferVM.message(),
                executeTransferVM.amount());

        transfersRepository.save(transfer);
    }

    private void validateExecuteTransferVM(ExecuteTransferVM executeTransferVM) {
        Map<String, List<String>> errors = new HashMap<>();

        if (executeTransferVM.amount() <= 0) {
            errors.put("amount", List.of("Сумма перевода должна быть больше 0"));
        }
        if (!errors.isEmpty()) {
            throw new ValidationException("Проблемы заполнения полей перевода", errors);
        }
    }
}
