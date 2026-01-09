package ru.otus.model;


import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.List;

@Table("client")
@Getter
public class Client implements Persistable<String> {

    @Id
    private final Long id;

    private final String name;

    @MappedCollection(idColumn = "client_id")
    private final Address address;

    @MappedCollection(idColumn = "client_id", keyColumn = "ordering")
    private final List<Phone> phones;

    @Transient
    private final boolean isNew;

    public Client(Long id, String name, Address address, List<Phone> phones, boolean isNew) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        this.isNew = isNew;
    }

    @PersistenceCreator
    private Client(Long id, String name, Address address, List<Phone> phones) {
        this(id, name, address, phones, false);
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public String getId() {
        return id != null ? String.valueOf(id) : null;
    }

    @Override
    public String toString() {
        var delim = "," + '\n';
        return "Client{id=" + id + delim + "    name='" + name + '\'' + delim
                + "    address=" + address + delim
                + "    phones=" + phones
                + delim
                + '}';
    }
}