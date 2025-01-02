package ru.otus.pro.hw.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {
	/*
	1. Добавьте сущность СчетКлиента/Account (id PK, номерСчета, клиентИд, баланс, заблокированЛи)
	2. Клиент может запросить информацию по счетам/счету(поиск по ИД)
	3. Доделайте процесс перевода, деньги должны быть списаны с одного счета и зачислены на другой, добавьте
	все необходимые, как вам кажется, проверки (+само собой в БД должен быть создан новый Transfer)
	4. Добавьте обработку BusinessLogicException (оно должно оборачиваться в 400)
	5. Метод GET /api/v1/transfers должен возвращать не только переводы, где клиент является инициатором, но
	и где он является получателем
	 */

    private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
