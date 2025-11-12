package ru.otus.jdbc.mapper;

import jakarta.persistence.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Класс не содержит конструктора без параметров", e);
        }
    }

    @Override
    public Field getIdField() {
        for (Field field: clazz.getDeclaredFields()) {
            if(field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        throw new IllegalStateException("Класс должен содержать поле с аннотацией @Id");
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        List<Field> fields = new ArrayList<>();
        for (Field field: clazz.getDeclaredFields()) {
            if(!field.isAnnotationPresent(Id.class)) {
                fields.add(field);
            }
        }
        return fields;
    }
}
