package ru.otus.model;

import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
public record Phone(@Id Long id, @Column("client_id") Long clientId, Integer ordering, @Nonnull String number) {
    @Override
    public String toString() {
        return "Phone{id=" + id + "' number='" + number + "'}'";
    }
}