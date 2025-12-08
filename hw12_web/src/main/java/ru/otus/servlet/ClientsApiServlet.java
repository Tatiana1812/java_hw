package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.services.DbServiceClientImpl;

import java.io.IOException;
import java.util.Arrays;

@SuppressWarnings({"java:S1989"})
public class ClientsApiServlet extends HttpServlet {

    private static final String PARAM_NAME = "name";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_PHONES = "phones";

    private final transient DbServiceClientImpl clientService;

    public ClientsApiServlet(DbServiceClientImpl clientService) {
        this.clientService = clientService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var name = request.getParameter(PARAM_NAME);
        var address = request.getParameter(PARAM_ADDRESS);
        var phones = request.getParameter(PARAM_PHONES);
        var client = new Client(
                null,
                name,
                address.isEmpty() ? null : new Address(null, address),
                phones.isEmpty()
                        ? null
                        : Arrays.stream(phones.split("\n"))
                        .map(item -> new Phone(null, item))
                        .toList());

        clientService.saveClient(client);
        response.sendRedirect("/clients");
    }
}
