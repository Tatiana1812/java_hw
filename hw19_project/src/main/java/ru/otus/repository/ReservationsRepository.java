package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.entity.Reservations;

import java.util.List;
import java.util.Optional;

public interface ReservationsRepository extends JpaRepository<Reservations, Long> {

    @Query("""
        select r
        from Reservations r
        join fetch r.table t
        where r.user.login = :login
        order by r.startTime desc
    """)
    List<Reservations> findAllReservations(@Param("login") String login);


    @Query("""
        select r
        from Reservations r
        join fetch r.user u
        join fetch r.table t
        where r.id = :id and u.login = :login
    """)
    Optional<Reservations> findReservation(@Param("id") Long id, @Param("login") String login);

}