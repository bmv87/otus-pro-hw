package ru.otus.pro.hw.jdbc;

import ru.otus.pro.hw.jdbc.entities.Item;

import java.sql.SQLException;
import java.util.List;

public interface IItemsService {
    void saveRange(List<Item> items) throws SQLException;

    void editItemsPrice() throws SQLException;
}
