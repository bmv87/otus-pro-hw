package ru.otus.pro.hw.persistence;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.otus.pro.hw.persistence.entities.Product;
import ru.otus.pro.hw.persistence.entities.Student;
import ru.otus.pro.hw.persistence.entities.StudentProdact;

public class Main {

    private static SessionFactory factory;

    public static void init() {
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    public static void main(String[] args) {
        try {
            init();
            var repo1 = new AbstractRepository<>(factory, Student.class, Long.class);
            var repo2 = new AbstractRepository<>(factory, Product.class, Long.class);
            var repo3 = new AbstractRepository<>(factory, StudentProdact.class, Long.class);
            var student = repo1.get(1L).get();
            var product = repo2.get(1L).get();
            System.out.println(student);
            System.out.println(product);
            var sp = new StudentProdact();
            sp.setProduct(product);
            sp.setStudent(student);
            sp = repo3.add(sp);
            System.out.println(sp);

            System.out.println("Get created StudentProdact:");
            var result = repo3.get(sp.getId());
            System.out.println(result);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } finally {
            close();
        }
    }

    public static void close() {
        factory.close();
    }
}
