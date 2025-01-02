package ru.otus.pro.hw.rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.pro.hw.rest.entities.Account;

import java.util.List;
import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Account, String> {
    Optional<Account> findByIdAndClientId(String id, String clientId);

    List<Account> findByClientIdAndLocked(String clientId, boolean locked);
}
