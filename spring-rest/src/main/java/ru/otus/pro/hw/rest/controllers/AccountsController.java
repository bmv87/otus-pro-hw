package ru.otus.pro.hw.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.pro.hw.rest.models.AccountVM;
import ru.otus.pro.hw.rest.services.AccountsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountsController {
    private final AccountsService accountsService;

    @GetMapping
    public List<AccountVM> getAllAccountsByClientId(@RequestHeader(name = "client-id") String clientId) {
        return accountsService.getAllAccountsByClientId(clientId);
    }

    @PostMapping
    public String create(@RequestHeader(name = "client-id") String clientId) {
        return accountsService.addAccount(clientId);
    }
}
