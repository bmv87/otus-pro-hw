package hw.jdbc.repositories;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public class EntityInfo<T> {

    @Getter
    @AllArgsConstructor
    public static class FieldInfo {
        private final String name;
        private final Class<?> type;
        private final Method getter;
        private final Method setter;
    }

    private Class<T> cls;
    private String tableName;
    private Map<String, FieldInfo> fields = new HashMap<>();
    private Map<String, Method> keyFields = new HashMap<>();

    public EntityInfo(Class<T> cls) {
        this.cls = cls;
        setTableName();
        setFieldsGetters();
    }

    private void setTableName() {
        var title = cls.getAnnotation(RepositoryTable.class).title();
        if (title == null || title.isBlank()) {
            throw new ApplicationInitializationException("Имя таблицы не указано: " + cls.getName());
        }
        tableName = title;
    }

    private void setFieldsGetters() {
        var cachedFields = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryField.class))
                .toList();
        var methods = Arrays.stream(cls.getDeclaredMethods()).toList();
        for (var field : cachedFields) {
            var getterName = (field.getType() == boolean.class || field.getType() == Boolean.class ? "is" : "get")
                    + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            var setterName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            var getterMethod = methods.stream().filter(m -> m.getName().equals(getterName)).findFirst()
                    .orElseThrow(() -> new ApplicationInitializationException("Метод " + getterName + " не найден"));
            var setterMethod = methods.stream().filter(m -> m.getName().equals(setterName)).findFirst()
                    .orElseThrow(() -> new ApplicationInitializationException("Метод " + setterName + " не найден"));

            // System.out.println(getterName);
            // System.out.println(getterMethod);
            // System.out.println(setterName);
            // System.out.println(setterMethod);
            var fieldAnnotation = field.getDeclaredAnnotation(RepositoryField.class);
            var columnName = fieldAnnotation != null && fieldAnnotation.name() != null
                    && !fieldAnnotation.name().isBlank()
                            ? fieldAnnotation.name()
                            : field.getName();
            var info = new FieldInfo(field.getName(), field.getType(), getterMethod, setterMethod);
            fields.put(columnName, info);
            if (field.getDeclaredAnnotation(RepositoryIdField.class) != null) {
                keyFields.put(columnName, getterMethod);
            }
        }
        if (keyFields.isEmpty()) {
            throw new ApplicationInitializationException("Крючевое поле сущности не задано: " + cls.getName());
        }
    }
}
