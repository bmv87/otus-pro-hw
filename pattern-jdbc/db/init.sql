\connect pattern_jdbc;
 
CREATE TABLE IF NOT EXISTS Items (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100),
    price DECIMAL(9,3)
);

-- INSERT INTO Items (title,price) VALUES ( 'Item--1', 3.3),
--  ( 'Item--2', 2.2),
--  ( 'Item--3', 2.2),
--  ( 'Item--4', 2.2),
--  ( 'Item--5', 2.2),
--  ( 'Item--6', 2.2),
--  ( 'Item--7', 2.2),
--  ( 'Item--8', 2.2),
--  ( 'Item--9', 2.2),
--  ( 'Item--10', 2.3),
--  ( 'Item--11', 2.3);
  
 GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
