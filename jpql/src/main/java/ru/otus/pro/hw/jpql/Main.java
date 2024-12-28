package ru.otus.pro.hw.jpql;

import jakarta.persistence.EntityManager;
import ru.otus.pro.hw.jpql.entities.Address;
import ru.otus.pro.hw.jpql.entities.Client;
import ru.otus.pro.hw.jpql.entities.Phone;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        EntityManager entityManager = null;
        try {
            entityManager = FactoryManager.getEntityManager();
            System.out.println(entityManager.getClass().getName());
            entityManager.getTransaction().begin();
            Set<Phone> phoneList = new HashSet<>();

            var client = new Client();

            client.setName("client1");
            phoneList.add(new Phone(client, "999 999 99 99"));
            phoneList.add(new Phone(client, "888 888 88 88"));

            var address = new Address();
            address.setClient(client);
            address.setStreet("street 1");

            client.setAddress(address);
            client.setPhones(phoneList);
            entityManager.persist(client);
            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            FactoryManager.close();
        }
    }
}
