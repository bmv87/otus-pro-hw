package ru.otus.pro.hw.jdbc;

import ru.otus.pro.hw.jdbc.entities.Item;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        MyDataSource.setConfig(
                new MyDataSource.SourceOptopns(
                        "jdbc:postgresql://localhost:5432/pattern_jdbc?user=postgres&password=postgres",
                        "org.postgresql.Driver"));
        var newItems = new ArrayList<Item>();
        for (int i = 0; i < 100; i++) {
            newItems.add(new Item(String.format("Item-%d", i), new BigDecimal(2.2 * Math.random() * 100)));
        }

        try {
            var itemsDAO = new ItemsDao(MyDataSource.getInstance());
            IItemsService itemsService = new ItemsService(itemsDAO);
            // IItemsService itemsService = new ItemsServiceProxy(new ItemsService(itemsDAO));
            itemsService.saveRange(newItems);
            itemsService.editItemsPrice();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
