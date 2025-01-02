package ru.otus.pro.hw.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.pro.hw.rest.entities.Transfer;
import ru.otus.pro.hw.rest.exceptions.NotFoundException;
import ru.otus.pro.hw.rest.models.ExecuteTransferVM;
import ru.otus.pro.hw.rest.models.Paginated;
import ru.otus.pro.hw.rest.models.TransferVM;
import ru.otus.pro.hw.rest.services.TransfersService;

import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers")
public class TransfersController {
    private final TransfersService transfersService;

    private static final Function<Transfer, TransferVM> ENTITY_TO_DTO = t -> new TransferVM(t.getId(), t.getSourceAccount().getClientId(), t.getTargetAccount().getClientId(), t.getSourceAccount().getAccountNumber(), t.getTargetAccount().getAccountNumber(), t.getMessage(), t.getAmount());

    @GetMapping
    public Paginated<TransferVM> getAllTransfers(@RequestHeader(name = "client-id") String clientId) {
        var transfers = transfersService
                .getAllTransfers(clientId)
                .stream()
                .map(ENTITY_TO_DTO).collect(Collectors.toList());
        return new Paginated<>(
                transfers.size(),
                transfers
        );
    }

    @GetMapping("/{id}")
    public TransferVM getTransferById(@RequestHeader(name = "client-id") String clientId, @PathVariable String id) {
        return ENTITY_TO_DTO.apply(transfersService.getTransferById(id, clientId).orElseThrow(() -> new NotFoundException("Перевод не найден")));
    }

    @PostMapping
    public void executeTransfer(@RequestHeader(name = "client-id") String clientId, @Valid @RequestBody ExecuteTransferVM executeTransferVM) {
        transfersService.execute(clientId, executeTransferVM);
    }
}
