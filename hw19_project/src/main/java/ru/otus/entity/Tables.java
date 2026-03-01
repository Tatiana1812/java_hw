package ru.otus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tables")
public class Tables {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_seq")
    @SequenceGenerator(name = "table_seq", sequenceName = "table_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "capacity")
    private Integer capacity;
}
