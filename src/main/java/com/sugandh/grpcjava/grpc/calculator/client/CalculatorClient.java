package com.sugandh.grpcjava.grpc.calculator.client;

import com.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
        doClientStreamingCall(channel);
        doBiDiStreamingCall(channel);
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

    private void doClientStreamingCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<ComputeAverageRequest> computeAverageRequestStreamObserver =
                asyncClient.computeAverage(new StreamObserver<ComputeAverageResponse>() {
                    @Override
                    public void onNext(ComputeAverageResponse value) {
                        System.out.println("Response received from the server");
                        System.out.println(value.getAverage());
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onCompleted() {
                        //Server is done, sending us data
                        System.out.println("Server has completed, sending us something");
                        latch.countDown();
                    }
                });

        System.out.println(" Sending Message 1");
        computeAverageRequestStreamObserver.onNext(ComputeAverageRequest
                .newBuilder().setInputNumber(5).build());

        System.out.println(" Sending Message 2");
        computeAverageRequestStreamObserver.onNext(ComputeAverageRequest
                .newBuilder().setInputNumber(16).build());
        try {
            latch.await(30, TimeUnit.SECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void doBiDiStreamingCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncClient =
                CalculatorServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<FindMaxRequest> requestStreamObserver = asyncClient.findMaxNumber(
                new StreamObserver<FindMaxResponse>() {
                    @Override
                    public void onNext(FindMaxResponse value) {
                        System.out.println(value.getMaxNumber());
                    }

                    @Override
                    public void onError(Throwable t) {
                        latch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("Server is done with sending messages");
                    }
                }
        );
        Arrays.asList(2,3,5,3,6,5,6,8,6,10).forEach(number ->{
            System.out.println("Sending "+number);
            requestStreamObserver.onNext(FindMaxRequest.newBuilder()
                    .setInputNumber(number).build());
            try {
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        });

        try {
            latch.await();
        }catch (InterruptedException e){
          e.printStackTrace();
        }
    }

}
