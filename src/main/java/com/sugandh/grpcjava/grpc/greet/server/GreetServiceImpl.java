package com.sugandh.grpcjava.grpc.greet.server;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {
    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
//        super.greet(request, responseObserver);
        // extract the fields we need
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();
        // create the response
        String result = "Hello " + firstName;
        GreetResponse response = GreetResponse.newBuilder().setResult(result).build();
        // send the response
        responseObserver.onNext(response);
        // complete the GRPC CALL
        responseObserver.onCompleted();
    }
}
