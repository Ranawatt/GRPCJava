package com.sugandh.grpcjava.grpc.greet.server;

import com.proto.greet.*;
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

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {
//        super.greetManyTimes(request, responseObserver);
        String firstName = request.getGreeting().getFirstName();
        try {
            for(int i=0; i<10; i++){
                String result = "Hello "+ firstName +", response number " + i;
                GreetManyTimesResponse manyTimesResponse =
                        GreetManyTimesResponse.newBuilder().setResult(result).build();
                System.out.println(manyTimesResponse);
                responseObserver.onNext(manyTimesResponse);
                Thread.sleep(1000);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            responseObserver.onCompleted();
        }
    }
}
