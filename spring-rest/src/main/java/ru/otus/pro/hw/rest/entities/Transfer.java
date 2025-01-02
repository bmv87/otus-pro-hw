package ru.otus.pro.hw.rest.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "source_account_id", nullable = false)
    private String sourceAccountId;

    @Column(name = "target_account_id", nullable = false)
    private String targetAccountId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_account_id", insertable = false, updatable = false)
    private Account sourceAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_account_id", insertable = false, updatable = false)
    private Account targetAccount;

    @Column(name = "message")
    private String message;

    @Column(name = "amount")
    private int amount;

    public Transfer(String id, String sourceAccountId, String targetAccountId, String message, int amount) {
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.message = message;
        this.amount = amount;
    }
}
