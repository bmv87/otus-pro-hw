package ru.otus.pro.hw.rest.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.otus.pro.hw.rest.entities.Account;
import ru.otus.pro.hw.rest.exceptions.BusinessLogicException;
import ru.otus.pro.hw.rest.exceptions.NotFoundException;
import ru.otus.pro.hw.rest.models.AccountVM;
import ru.otus.pro.hw.rest.repositories.AccountsRepository;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class AccountsService {
    private final AccountsRepository accountsRepository;

    public List<AccountVM> getAllAccountsByClientId(String clientId) {
        var accounts = accountsRepository.findByClientIdAndLocked(clientId, false);
        return accounts.stream()
                .map(a -> new AccountVM(a.getId(), a.getClientId(), a.getAccountNumber()))
                .toList();
    }

    public String addAccount(String clientId) {
        String randomNumber = String.format("%012d", (int) (Math.random() * 100) + 1);
        var account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setClientId(clientId);
        account.setAccountNumber(randomNumber);
        account.setLocked(false);
        account.setBalance(0);
        accountsRepository.save(account);
        return account.getId();
    }

    public void addFunds(String accountId, Integer amount) {
        var targetAccount = accountsRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Target account not found"));
        if (targetAccount.getLocked()) {
            throw new BusinessLogicException("Target Account is locked");
        }
        targetAccount.setBalance(targetAccount.getBalance() + amount);
        accountsRepository.save(targetAccount);
        log.debug(String.format("try add funds %s %d rub", targetAccount.getAccountNumber(), amount));
    }

    public void withdrawFunds(String clientId, String accountId, Integer amount) {
        var sourceAccount = accountsRepository.findByIdAndClientId(accountId, clientId)
                .orElseThrow(() -> new NotFoundException("Source account not found"));
        if (sourceAccount.getLocked()) {
            throw new BusinessLogicException("Source Account is locked");
        }
        if (sourceAccount.getBalance() < amount) {
            throw new BusinessLogicException("Not enough money on source account");
        }
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        accountsRepository.save(sourceAccount);
        log.debug(String.format("try withdraw funds %s %d rub", sourceAccount.getAccountNumber(), amount));
    }

}
