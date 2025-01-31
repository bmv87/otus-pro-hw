package hw.jdbc.entities;

import hw.jdbc.repositories.RepositoryField;
import hw.jdbc.repositories.RepositoryIdField;
import hw.jdbc.repositories.RepositoryTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RepositoryTable(title = "accounts")
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @RepositoryIdField()
    @RepositoryField()
    private Long id;

    @RepositoryField
    private Long amount;

    @RepositoryField(name = "account_type")
    private String accountType;

    @RepositoryField
    private String status;

    public Account(Long amount, String accountType, String status) {
        this.amount = amount;
        this.accountType = accountType;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", amount=" + amount +
                ", accountType='" + accountType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
