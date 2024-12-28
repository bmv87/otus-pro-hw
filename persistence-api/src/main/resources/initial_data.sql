
INSERT INTO students (name, id) VALUES ('Alexander', 1);
INSERT INTO students (name, id) VALUES ('Bob', 2);
INSERT INTO students (name, id) VALUES ('John', 3);

INSERT INTO products (title, id) VALUES ('product 1', 1);
INSERT INTO products (title, id) VALUES ('product 2', 2);

INSERT INTO student_products (student_id, product_id, created_at) VALUES (2, 2, '2023-10-10 11:30:30');
INSERT INTO student_products (student_id, product_id, created_at) VALUES (2, 2, '2024-10-10 11:30:30');
INSERT INTO student_products (student_id, product_id, created_at) VALUES (3, 1, '2022-10-10 11:30:30');
INSERT INTO student_products (student_id, product_id, created_at) VALUES (3, 2, '2021-10-10 11:30:30');
INSERT INTO student_products (student_id, product_id, created_at) VALUES (1, 2, '2022-10-10 11:30:30');
INSERT INTO student_products (student_id, product_id, created_at) VALUES (1, 1, '2023-10-10 11:30:30');
