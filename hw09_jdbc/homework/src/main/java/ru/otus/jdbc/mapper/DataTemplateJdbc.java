package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import jakarta.persistence.Id;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.crm.model.Client;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    private final EntityClassMetaData<T> classMeta;

    public DataTemplateJdbc(
            DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> classMeta) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.classMeta = classMeta;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), this::mapRowToObject);
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), this::mapRowsToObjects).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            List<Object> fieldValues = extractFieldValues(client);
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), fieldValues);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            List<Object> fieldValues = extractFieldValues(client);
            fieldValues.add(extractIdFromObject(client));
            dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getUpdateSql(), fieldValues);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T mapRowToObject(ResultSet rs) {
        try {
            if (rs.next()) {
                T instance = classMeta.getConstructor().newInstance();
                for (Field field : classMeta.getAllFields()) {
                    field.setAccessible(true);
                    field.set(instance, rs.getObject(field.getName()));
                }
                return instance;
            } return null;
        } catch (ReflectiveOperationException | SQLException e) {
            throw new DataTemplateException(e);
        }
    }

    private List<T> mapRowsToObjects(ResultSet rs) {
        List<T> objects = new ArrayList<>();
        try {
            while (rs.next()) {
                objects.add(mapRowToObject(rs));
            }
            return objects;
        } catch (SQLException e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> extractFieldValues(T object) {
        List<Field> fields = classMeta.getFieldsWithoutId();
        List<Object> values = new ArrayList<>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                values.add(field.get(object));
            }
            return values;
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }

    private Long extractIdFromObject(T object) {
        try {
            Field idField = classMeta.getIdField();
            idField.setAccessible(true);
            return (Long) idField.get(object);
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }
}
