package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.core.repository.ClientRepository;
import ru.otus.model.Client;
import ru.otus.sessionmanager.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbServiceClientImpl implements DBServiceClient {
    private final ClientRepository clientRepository;
    private final TransactionManager transactionManager;

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> clientRepository.save(client));
    }

    @Override
    public Optional<Client> findByName(String name) {
        return clientRepository.findByName(name);
    }

    @Override
    public List<Client> findAll() {
        return new ArrayList<>(clientRepository.findAll());
    }
}
