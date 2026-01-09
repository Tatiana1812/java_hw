package ru.otus.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.NumberRequest;
import ru.otus.protobuf.NumberResponse;
import ru.otus.protobuf.NumbersServiceGrpc;

@SuppressWarnings({"squid:S2142", "squid:S106"})
public class NumberServiceImpl extends NumbersServiceGrpc.NumbersServiceImplBase {
    @Override
    public void getNumbers(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        for (int i = request.getFirstValue(); i <= request.getLastValue(); i++) {
            NumberResponse response = NumberResponse.newBuilder().setValue(i).build();
            responseObserver.onNext(response);
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                responseObserver.onError(e);
            }
        }
        responseObserver.onCompleted();
    }
}
