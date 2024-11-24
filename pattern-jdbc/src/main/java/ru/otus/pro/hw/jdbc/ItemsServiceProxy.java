package ru.otus.pro.hw.jdbc;

import ru.otus.pro.hw.jdbc.entities.Item;

import java.sql.SQLException;
import java.util.List;

public class ItemsServiceProxy implements IItemsService {
    private ItemsService service;

    public ItemsServiceProxy(ItemsService service) {
        this.service = service;
        this.service.getDao().setUseOneConnection(true);
    }

    @Override
    public void saveRange(List<Item> items) throws SQLException {
        try (var conn = this.service.getDao().getConnection()) {
            conn.setAutoCommit(false);
            try {
                service.saveRange(items);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    @Override
    public void editItemsPrice() throws SQLException {
        try (var conn = this.service.getDao().getConnection()) {
            conn.setAutoCommit(false);
            try {
                service.editItemsPrice();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
