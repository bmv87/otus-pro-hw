package ru.otus.pro.hw.jdbc;

import lombok.Getter;
import ru.otus.pro.hw.jdbc.entities.Item;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ItemsService implements IItemsService {
    @Getter
    private ItemsDao dao;

    public ItemsService(ItemsDao dao) throws SQLException {
        this.dao = dao;
    }

    public void saveRange(List<Item> items) throws SQLException {
        for (var item : items) {
            dao.create(item);
        }
    }

    public void editItemsPrice() throws SQLException {
        var items = dao.getAll();
        var multiplicand = new BigDecimal(2);
        for (var item : items) {
            item.setPrice(item.getPrice().multiply(multiplicand));
            dao.update(item);
        }
    }
}
