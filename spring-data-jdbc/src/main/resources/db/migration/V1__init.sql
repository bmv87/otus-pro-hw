create table products (
    id bigserial primary key,
    title varchar(255),
    price int
);

insert into products (title, price) values ('Milk', 80), ('Bread', 35), ('Cheese', 320);