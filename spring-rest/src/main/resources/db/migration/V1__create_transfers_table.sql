create table accounts (
    id varchar(36) primary key,
    account_number varchar(12) NOT NULL,
    client_id varchar(36) NOT NULL,
    balance int NOT NULL,
    locked boolean NOT NULL
);
create table transfers (
    id varchar(36) primary key,
    source_account_id varchar(36) NOT NULL,
    target_account_id varchar(36) NOT NULL,
    amount int NOT NULL,
    message varchar(255),
    CONSTRAINT FK_source_transfer_account FOREIGN KEY(source_account_id) REFERENCES accounts(id),
    CONSTRAINT FK_target_transfer_account FOREIGN KEY(target_account_id) REFERENCES accounts(id)
);


insert into accounts (id, account_number, client_id, balance, locked) values
('bde76ffa-f133-4c23-9bca-03618b2a94b3', '000000000001', 'cfe76ffa-f133-4c23-9bca-03618b2a94b4', 1000, false),
('bde76ffa-f133-4c23-9bca-03618b2a94b4', '000000000002', 'cfebb2eb-ed35-4baa-b500-b7f6535e4c22', 500, false);

insert into transfers (id, source_account_id, target_account_id, amount, message) values
('bde76ffa-f133-4c23-9bca-03618b2a94b2', 'bde76ffa-f133-4c23-9bca-03618b2a94b3', 'bde76ffa-f133-4c23-9bca-03618b2a94b4', 100, 'Тестовый перевод'),
('32ebb2eb-ed35-4baa-b500-b7f6535e4c88', 'bde76ffa-f133-4c23-9bca-03618b2a94b4', 'bde76ffa-f133-4c23-9bca-03618b2a94b3', 50, 'Обратный тестовый перевод');