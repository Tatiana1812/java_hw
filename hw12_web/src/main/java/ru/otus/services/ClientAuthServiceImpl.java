package ru.otus.services;

import ru.otus.model.Client;

public class ClientAuthServiceImpl implements ClientAuthService {

    private final DbServiceClientImpl clientService;

    public ClientAuthServiceImpl(DbServiceClientImpl clientService) {
        this.clientService = clientService;
    }

    @Override
    public boolean authenticate(String login, String password) {
        Client client = clientService.findByLogin(login);
        return client.getPassword().equals(password);
    }
}
