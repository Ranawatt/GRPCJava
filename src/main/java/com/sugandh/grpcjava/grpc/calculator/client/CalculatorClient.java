package com.sugandh.grpcjava.grpc.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.PrimeRequest;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {

    public static void main(String[] args) {
        System.out.println("Hello, I am Grpc Client for Calculator module");
        CalculatorClient mainClient = new CalculatorClient();
        mainClient.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50052).usePlaintext().build();

        System.out.println("Creating Stub");

        doUnaryCall(channel);
        doServerStreamingCall(channel);
    }

    private void doUnaryCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient =
                CalculatorServiceGrpc.newBlockingStub(channel);
        //Unary rpc Call
        SumRequest sumRequest = SumRequest.newBuilder()
                .setFirstNumber(3).setSecondNumber(5).build();

        SumResponse response = calculatorClient.sum(sumRequest);
        System.out.println(sumRequest.getFirstNumber()+" + "+sumRequest.getSecondNumber()
                +" = "+response.getSumResult());

    }

    private void doServerStreamingCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient =
                CalculatorServiceGrpc.newBlockingStub(channel);

        //Server streaming call
        PrimeRequest primeRequest  = PrimeRequest.newBuilder()
                .setInputNumber(567332542L).build();
        calculatorClient.primeNumber(primeRequest).forEachRemaining(
                primeResponse -> {
                    System.out.println(primeResponse.getPrimeResult());
                }
        );
    }

}
