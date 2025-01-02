package ru.otus.pro.hw.rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.pro.hw.rest.entities.Transfer;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransfersRepository extends JpaRepository<Transfer, String> {
    @Query("select t from Transfer t join t.sourceAccount sa where t.id = :id and sa.clientId = :clientId")
    Optional<Transfer> findByIdAndClientId(@Param("id") String id, @Param("clientId") String clientId);
    @Query("select t from Transfer t join t.sourceAccount sa join t.targetAccount ta where sa.clientId = :id or ta.clientId = :id")
    List<Transfer> findAllByClientId(@Param("id") String clientId);
}
