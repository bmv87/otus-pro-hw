package hw.jdbc;

import hw.jdbc.entities.Account;
import hw.jdbc.entities.User;
import hw.jdbc.migrator.DbMigrator;
import hw.jdbc.migrator.MigratorOptions;
import hw.jdbc.repositories.AbstractRepository;
import hw.jdbc.suorce.MyDataSource;
import hw.jdbc.suorce.SourceOptions;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args)
            throws ClassNotFoundException, SQLException, IOException, NoSuchAlgorithmException {
        MyDataSource.setConfig(new SourceOptions(
                "org.postgresql.Driver",
                "jdbc:postgresql://localhost:5432/",
                "postgres",
                "postgres",
                "test_db"));
        try (var dataSource = MyDataSource.getInstance()) {
            var migrator = new DbMigrator(
                    dataSource,
                    new MigratorOptions(
                            "migration_history",
                            "public",
                            "hw-jdbc\\db",
                            "migration_log.txt"));
            migrator.migrate();

            AbstractRepository<User> userRepository = new AbstractRepository<>(dataSource, User.class);
            AbstractRepository<Account> accountRepository = new AbstractRepository<>(dataSource, Account.class);
            Account account = new Account(25470l, "debit", "active");

            accountRepository.create(account);
            System.out.println();
            System.out.println(accountRepository.findAll());
            System.out.println();

            User user = new User("user_100", "123", "bob_2");
            userRepository.create(user);
            var list = userRepository.findAll();
            System.out.println();
            System.out.println(list);
            System.out.println();

            userRepository.delete(list.get(0).getId());
            list = userRepository.findAll();
            System.out.println();
            System.out.println(list);
            System.out.println();

            var some = list.get(0);
            System.out.println();
            System.out.println(some);
            System.out.println();
            
            some.setNickname(some.getNickname() + "++++edited++++");
            userRepository.update(some);
            System.out.println(userRepository.findById(some.getId()));
        }

    }
}
