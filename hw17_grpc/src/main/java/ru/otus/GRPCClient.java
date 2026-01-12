package ru.otus;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.NumberRequest;
import ru.otus.protobuf.NumberResponse;
import ru.otus.protobuf.NumbersServiceGrpc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings({"squid:S106", "squid:S2142"})
public class GRPCClient {
    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        long currentValue = 0;
        AtomicLong latestReceivedFromServer = new AtomicLong(-1);
        AtomicLong newValue = new AtomicLong(0);

        var latch = new CountDownLatch(1);
        var stub = NumbersServiceGrpc.newStub(channel);
        var request = NumberRequest.newBuilder().setFirstValue(1).setLastValue(30).build();
        stub.getNumbers(
                request,
                new StreamObserver<NumberResponse>() {
                    @Override
                    public void onNext(NumberResponse response) {
                        newValue.set(response.getValue());
                        logger.info("Получено число от сервера: {}", newValue);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println("Ошибка при получении чисел: " + t.getMessage());
                        logger.error("Ошибка при получении чисел: {}", t.getMessage());
                        latch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        logger.info("Поток чисел завершён.");
                        latch.countDown();
                    }
                });

        while (latestReceivedFromServer.get() != request.getLastValue()) {
            if (latestReceivedFromServer.get() < newValue.get()) {
                currentValue += newValue.getAndSet(0) + 1;
                latestReceivedFromServer = newValue;
            } else {
                currentValue++;
            }
            logger.info("Текущее значение: {}", currentValue);

            delay();
        }
        latch.await();

        channel.shutdown();
    }
    private static void delay() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            logger.info("Thread interrupted!");
        }
    }
}
