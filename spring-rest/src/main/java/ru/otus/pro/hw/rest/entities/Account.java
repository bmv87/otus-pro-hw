package ru.otus.pro.hw.rest.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "balance", nullable = false)
    private Integer balance;

    @Column(name = "locked", nullable = false)
    private Boolean locked;

    @OneToMany(mappedBy = "sourceAccount")
    private List<Transfer> outTransfers;

    @OneToMany(mappedBy = "targetAccount")
    private List<Transfer> inTransfers;
}
