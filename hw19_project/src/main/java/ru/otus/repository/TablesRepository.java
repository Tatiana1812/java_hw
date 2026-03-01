package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.entity.Tables;

import java.time.LocalDateTime;
import java.util.List;


public interface TablesRepository extends JpaRepository<Tables, Long> {
    @Query(value = """
    select t.*
    from tables t
    where t.capacity >= :persons
      and not exists (
        select 1
        from reservations r
        where r.table_id = t.id
          and r.status = 'CONFIRMED'
          and r.time_slot && tsrange(:startTime, :endTime, '[)')
      )
    order by t.capacity asc, t.id asc
    """, nativeQuery = true)
    List<Tables> findAvailableTables(@Param("persons") int persons,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime);
}