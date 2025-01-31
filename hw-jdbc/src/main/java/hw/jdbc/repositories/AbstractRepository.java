package hw.jdbc.repositories;

import hw.jdbc.suorce.MyDataSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AbstractRepository<T> {
    private MyDataSource dataSource;

    private String sCreate;
    private String sUpdate;
    private String sDelete;
    private String sFindById;
    private String sSelectAll;

    private EntityInfo<T> entityInfo;

    public AbstractRepository(MyDataSource dataSource, Class<T> cls) {
        this.dataSource = dataSource;
        entityInfo = new EntityInfo<>(cls);
        prepareCRUD();
    }

    public void create(T entity) {
        try (var conn = dataSource.getConnection(); var psCreate = conn.prepareStatement(sCreate)) {
            conn.setAutoCommit(false);
            var createFields = getCreateFields().entrySet();
            int paramIndex = 0;

            for (var field : createFields) {
                psCreate.setObject(paramIndex + 1, field.getValue().invoke(entity));
                ++paramIndex;
            }
            psCreate.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            throw new DBOperationException(e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ApplicationInitializationException();
        }
    }

    public List<T> findAll() {
        try (var conn = dataSource.getConnection(); var psSelectAll = conn.prepareStatement(sSelectAll)) {
            var fields = entityInfo.getFields().entrySet();

            var resultSet = psSelectAll.executeQuery();
            List<T> list = new ArrayList<>();
            while (resultSet.next()) {
                var newEntity = entityInfo.getCls().getConstructor().newInstance();
                for (var field : fields) {
                    field.getValue().getSetter().invoke(newEntity,
                            resultSet.getObject(field.getKey(), field.getValue().getType()));
                }
                list.add(newEntity);
                // System.out.println(newEntity);
            }
            return list;
        } catch (SQLException e) {
            throw new DBOperationException(e);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException
                 | NoSuchMethodException e) {
            throw new ApplicationInitializationException();
        }
    }

    public Optional<T> findById(Object... keys) {
        try (var conn = dataSource.getConnection(); var psFindById = conn.prepareStatement(sFindById)) {
            var fields = entityInfo.getFields().entrySet();
            int paramIndex = 0;

            for (var key : keys) {
                psFindById.setObject(paramIndex + 1, key);
                ++paramIndex;
            }
            var resultSet = psFindById.executeQuery();
            while (resultSet.next()) {
                var newEntity = entityInfo.getCls().getConstructor().newInstance();
                for (var field : fields) {
                    field.getValue().getSetter().invoke(newEntity,
                            resultSet.getObject(field.getKey(), field.getValue().getType()));
                }
                return Optional.of(newEntity);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DBOperationException(e);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException
                 | NoSuchMethodException e) {
            throw new ApplicationInitializationException();
        }
    }

    public void update(T entity) {
        try (var conn = dataSource.getConnection(); var psUpdate = conn.prepareStatement(sUpdate)) {
            conn.setAutoCommit(false);
            var updateFields = getUpdateFields().entrySet();
            int paramIndex = 0;

            for (var field : updateFields) {
                psUpdate.setObject(paramIndex + 1, field.getValue().invoke(entity));
                ++paramIndex;
            }
            for (var keyField : entityInfo.getKeyFields().entrySet()) {
                psUpdate.setObject(paramIndex + 1, keyField.getValue().invoke(entity));
            }
            psUpdate.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            throw new DBOperationException(e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ApplicationInitializationException();
        }
    }

    public void delete(Object... keys) {
        try (var conn = dataSource.getConnection(); var psDelete = conn.prepareStatement(sDelete)) {
            conn.setAutoCommit(false);
            int paramIndex = 0;

            for (var key : keys) {
                psDelete.setObject(paramIndex + 1, key);
                ++paramIndex;
            }
            psDelete.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            throw new DBOperationException(e);
        }
    }

    private void prepareCRUD() {
        prepareInsert();
        prepareUpdate();
        prepareDelete();
        prepareSelectAll();
        prepareFindById();
    }

    private Map<String, Method> getCreateFields() {
        return entityInfo.getKeyFields().size() == 1
                ? entityInfo.getFields().entrySet().stream()
                .filter(f -> !entityInfo.getKeyFields().containsKey(f.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, f -> f.getValue().getGetter()))
                : entityInfo.getFields().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, f -> f.getValue().getGetter()));
    }

    private Map<String, Method> getUpdateFields() {
        return entityInfo.getFields().entrySet().stream()
                .filter(f -> !entityInfo.getKeyFields().containsKey(f.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, f -> f.getValue().getGetter()));
    }

    private void prepareSelectAll() {
        String tableName = entityInfo.getTableName();
        StringBuilder query = new StringBuilder("SELECT * FROM ");
        query.append(tableName);
        query.append(";");
        sSelectAll = query.toString();
    }

    private void prepareDelete() {
        String tableName = entityInfo.getTableName();
        StringBuilder query = new StringBuilder("DELETE FROM ");
        query.append(tableName).append(" WHERE ");
        for (var keyField : entityInfo.getKeyFields().keySet()) {
            query.append(keyField).append("=? AND ");
        }
        query.setLength(query.length() - 5);
        query.append(";");
        sDelete = query.toString();
    }

    private void prepareFindById() {
        String tableName = entityInfo.getTableName();
        StringBuilder query = new StringBuilder("SELECT * FROM ");
        query.append(tableName).append(" WHERE ");
        for (var keyField : entityInfo.getKeyFields().keySet()) {
            query.append(keyField).append("=? AND ");
        }
        query.setLength(query.length() - 5);
        query.append(";");
        sFindById = query.toString();
    }

    private void prepareUpdate() {
        String tableName = entityInfo.getTableName();
        var updateFields = getUpdateFields();
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(tableName).append(" SET ");
        for (var f : updateFields.entrySet()) {
            query.append(f.getKey()).append("=?").append(", ");
        }
        query.setLength(query.length() - 2);
        query.append(" WHERE ");
        for (var keyField : entityInfo.getKeyFields().keySet()) {
            query.append(keyField).append("=? AND ");
        }
        query.setLength(query.length() - 5);
        query.append(";");
        System.out.println(query.toString());
        sUpdate = query.toString();
    }

    private void prepareInsert() {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        String tableName = entityInfo.getTableName();
        query.append(tableName).append(" (");
        // 'insert into users ('
        var createFields = entityInfo.getKeyFields().size() == 1
                ? entityInfo.getFields().entrySet().stream()
                .filter(f -> !entityInfo.getKeyFields().containsKey(f.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                : entityInfo.getFields();

        for (var f : createFields.entrySet()) {
            query.append(f.getKey()).append(", ");
        }
        // 'insert into users (login, password, nickname, '
        query.setLength(query.length() - 2);
        // 'insert into users (login, password, nickname'
        query.append(") VALUES (");
        for (var f : createFields.entrySet()) {
            query.append("?, ");
        }
        // 'insert into users (login, password, nickname) values (?, ?, ?, '
        query.setLength(query.length() - 2);
        // 'insert into users (login, password, nickname) values (?, ?, ?'
        query.append(");");
        System.out.println(query.toString());

        sCreate = query.toString();
    }
}
