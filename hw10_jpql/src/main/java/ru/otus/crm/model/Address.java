package ru.otus.crm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address {

    @Id
    @SequenceGenerator(name = "address_gen", sequenceName = "address_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_gen")
    private Long id;

    @Column(name = "street")
    private String street;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "address")
    private Client client;
    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    protected Address clone() {
        return new Address(this.id, this.street);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
