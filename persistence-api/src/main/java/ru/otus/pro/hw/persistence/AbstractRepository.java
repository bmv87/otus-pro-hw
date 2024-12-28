package ru.otus.pro.hw.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.Optional;

public class AbstractRepository<T, ID extends Serializable> implements Repository<T, ID> {

    protected SessionFactory sessionFactory;
    protected Class<T> typeClass;
    protected Class<ID> idClass;

    public AbstractRepository(SessionFactory sessionFactory, Class<T> typeClass, Class<ID> idClass) {
        this.sessionFactory = sessionFactory;
        this.typeClass = typeClass;
        this.idClass = idClass;
    }

    @Override
    public T add(T entity) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            return entity;
        }
    }

    @Override
    public Optional<T> get(ID id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            T result = session.get(typeClass, id);
            session.getTransaction().commit();
            return Optional.ofNullable(result);
        }
    }
}
