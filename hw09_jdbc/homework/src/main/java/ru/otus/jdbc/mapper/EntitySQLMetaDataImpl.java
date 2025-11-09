package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(entityClassMetaData.getName()).append(";");
        System.out.println(sql);
        return sql.toString();
    }

    @Override
    public String getSelectByIdSql() {
        StringBuilder sql = new StringBuilder("SELECT ").append(getFields(entityClassMetaData.getAllFields()));
        sql.append(" FROM ").append(entityClassMetaData.getName()).append(" WHERE ").append(entityClassMetaData.getIdField().getName()).append(" = ?;");
        System.out.println(sql);
        return sql.toString();
    }

    @Override
    public String getInsertSql() {
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(entityClassMetaData.getName()).append("(").append(getFields(entityClassMetaData.getFieldsWithoutId()));
        sql.append(")").append(" VALUES (");
        boolean first = true;
        int count = entityClassMetaData.getFieldsWithoutId().size();
        for (int i = 0; i < count; i++) {
            if (!first) {
                sql.append(", ");
            }
            sql.append("?");
            first = false;
        }

        sql.append(");");
        System.out.println(sql);
        return sql.toString();
    }
//"update client set name = ? where id = ?"
    @Override
    public String getUpdateSql() {
        StringBuilder sql = new StringBuilder("UPDATE ").append(entityClassMetaData.getName()).append(" SET ");
        boolean first = true;
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            if (!first) {
                sql.append(" = ?, ");
            }
            sql.append(field.getName());
            first = false;
        }
        sql.append(" WHERE ").append(entityClassMetaData.getIdField().getName()).append(" = ?;");
        System.out.println(sql);
        return sql.toString();
    }

    private String getFields(List<Field> fieldList){
        StringBuilder allFields = new StringBuilder();
        boolean first = true;
        for (Field field : fieldList) {
            if (!first) {
                allFields.append(", ");
            }
            allFields.append(field.getName());
            first = false;
        }
        return allFields.toString();
    }
}
