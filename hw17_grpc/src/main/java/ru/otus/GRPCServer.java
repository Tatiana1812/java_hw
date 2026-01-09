package ru.otus;

import io.grpc.ServerBuilder;
import ru.otus.service.NumberServiceImpl;

import java.io.IOException;

@SuppressWarnings({"squid:S106"})
public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var remoteDBService = new NumberServiceImpl();

        var server =
                ServerBuilder.forPort(SERVER_PORT).addService(remoteDBService).build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
