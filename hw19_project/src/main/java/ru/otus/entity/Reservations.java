package ru.otus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.enums.ReservationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservations {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_seq")
    @SequenceGenerator(name = "reservation_seq", sequenceName = "reservation_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "guest_name")
    private String guestName;

    @Column(name = "guest_phone")
    private String guestPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private Tables table;

    @Column(name = "persons")
    private Integer persons;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;
}
