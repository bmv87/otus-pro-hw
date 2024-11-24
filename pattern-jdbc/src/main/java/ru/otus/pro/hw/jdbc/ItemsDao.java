package ru.otus.pro.hw.jdbc;

import lombok.Setter;
import ru.otus.pro.hw.jdbc.entities.Item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemsDao implements AutoCloseable {
    private MyDataSource dataSource;
    @Setter
    private boolean useOneConnection;
    private Connection connection;

    public ItemsDao(MyDataSource source) {
        dataSource = source;
    }

    public Connection getConnection() throws SQLException {

        if (!useOneConnection) {
            connection = dataSource.getConnection();
            return connection;
        }
        if (connection == null || connection.isClosed()) {
            connection = dataSource.getConnection();
        }

        return connection;
    }

    public int create(Item item) throws SQLException {
        var conn = getConnection();
        System.out.println("Connection: " + conn.hashCode());
        try (var statement = conn.prepareStatement("INSERT INTO Items (title,price) VALUES (?, ?);",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.getTitle());
            statement.setBigDecimal(2, item.getPrice());
            statement.executeUpdate();
            ResultSet tableKeys = statement.getGeneratedKeys();
            tableKeys.next();
            return tableKeys.getInt(1);
        } finally {
            if (!useOneConnection) {
                conn.close();
            }
        }

    }

    public void update(Item item) throws SQLException {
        var conn = getConnection();
        System.out.println("Connection: " + conn.hashCode());
        try (var statement = conn.prepareStatement("UPDATE Items SET title=?, price=? WHERE id = ?;")) {
            statement.setString(1, item.getTitle());
            statement.setBigDecimal(2, item.getPrice());
            statement.setInt(3, item.getId());
            statement.executeUpdate();
        } finally {
            if (!useOneConnection) {
                conn.close();
            }
        }
    }

    public List<Item> getAll() throws SQLException {
        var conn = getConnection();
        System.out.println("Connection: " + conn.hashCode());
        try (var statement = conn.prepareStatement("SELECT * FROM Items;")) {
            ResultSet resultSet = statement.executeQuery();
            var list = new ArrayList<Item>();
            while (resultSet.next()) {
                var item = new Item();
                item.setId(resultSet.getInt("id"));
                item.setTitle(resultSet.getString("title"));
                item.setPrice(resultSet.getBigDecimal("price"));
                list.add(item);
            }
            return list;
        } finally {
            if (!useOneConnection) {
                conn.close();
            }
        }
    }

    public Optional<Item> get(int id) throws SQLException {
        var conn = getConnection();
        System.out.println("Connection: " + conn.hashCode());
        try (var statement = conn.prepareStatement("SELECT * FROM Items WHERE id = ?;")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var item = new Item();
                item.setId(resultSet.getInt("id"));
                item.setTitle(resultSet.getString("title"));
                item.setPrice(resultSet.getBigDecimal("price"));
                return Optional.of(item);
            }
            return Optional.empty();
        } finally {
            if (!useOneConnection) {
                conn.close();
            }
        }
    }

    public void delete(int id) throws SQLException {
        var conn = getConnection();
        System.out.println("Connection: " + conn.hashCode());
        try (var statement = conn.prepareStatement("DELETE FROM Items WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } finally {
            if (!useOneConnection) {
                conn.close();
            }
        }
    }

    @Override
    public void close() throws Exception {
        if (useOneConnection && connection != null) {
            connection.close();
        }
    }
}
