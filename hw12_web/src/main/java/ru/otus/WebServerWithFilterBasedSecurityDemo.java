package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.helpers.MigrationsExecutorFlyway;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.server.ClientsWebServer;
import ru.otus.server.ClientsWebServerWithFilterBasedSecurity;
import ru.otus.services.*;

import java.util.List;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        addDefaultClients(dbServiceClient);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        ClientAuthService authService = new ClientAuthServiceImpl(dbServiceClient);

        ClientsWebServer usersWebServer = new ClientsWebServerWithFilterBasedSecurity(
                WEB_SERVER_PORT, authService, dbServiceClient, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
    private static void addDefaultClients(DBServiceClient dbServiceClient) {
        dbServiceClient.saveClient(new Client(null,
                "Vasya",
                new Address(null, "Ukhtomskogo"),
                List.of(new Phone(null, "88005553535"),
                new Phone(null, "788199")),
                "user7",
                "11111"));

        dbServiceClient.saveClient((new Client(null,
                "Katya",
                new Address(null, "Tikhomirnova"),
                List.of(new Phone(null, "89159987654")),
                "katya11!",
                "22:22")));

        dbServiceClient.saveClient((new Client(null,
                "Olga",
                new Address(null, "Zelinskogo"),
                List.of(new Phone(null, "89209127711")),
                "Kotik",
                "9119")));
    }

}
