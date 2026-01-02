package ru.otus.core.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.otus.model.Client;

import java.util.Optional;

public interface ClientRepository extends ListCrudRepository<Client, Long> {

    Optional<Client> findByName(String name);
}
