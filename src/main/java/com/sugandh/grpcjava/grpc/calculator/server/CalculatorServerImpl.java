package com.sugandh.grpcjava.grpc.calculator.server;

import com.proto.calculator.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class CalculatorServerImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {

        SumResponse sumResponse = SumResponse.newBuilder()
                .setSumResult(request.getFirstNumber() + request.getSecondNumber())
                .build();

        responseObserver.onNext(sumResponse);
        responseObserver.onCompleted();

    }

    @Override
    public void primeNumber(PrimeRequest request, StreamObserver<PrimeResponse> responseObserver) {

        Long inputNumber = request.getInputNumber();
        Long k = 2L;

        while(inputNumber > 1){
            if(inputNumber%k == 0){
                System.out.println(k);
                inputNumber = inputNumber / k;
                PrimeResponse manyTimesResponse = PrimeResponse.newBuilder()
                        .setPrimeResult(k).build();
                responseObserver.onNext(manyTimesResponse);
            }else{
                k = k + 1;
            }
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<ComputeAverageRequest> computeAverage(StreamObserver<ComputeAverageResponse> responseObserver) {

        StreamObserver<ComputeAverageRequest> requestStreamObserver = new
                StreamObserver<ComputeAverageRequest>() {
                int sum = 0;
                int count = 0;
                    @Override
                    public void onNext(ComputeAverageRequest value) {
                        System.out.println("Number: "+value.getInputNumber());
                        sum += value.getInputNumber();
                        count++;
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onCompleted() {
                        int result = sum/count;
                        responseObserver.onNext(ComputeAverageResponse
                                .newBuilder().setAverage(result).build());
                        responseObserver.onCompleted();
                    }
                };
        return requestStreamObserver;
    }

    @Override
    public StreamObserver<FindMaxRequest> findMaxNumber(StreamObserver<FindMaxResponse> responseObserver) {
        StreamObserver<FindMaxRequest> requestStreamObserver = new StreamObserver<FindMaxRequest>() {
            int currentNumber = 0;
            int currentMaximum = 0;
            @Override
            public void onNext(FindMaxRequest value) {
                System.out.println(value.getInputNumber());
                currentNumber = value.getInputNumber();
                if(currentNumber > currentMaximum) {
                    currentMaximum = currentNumber;
                    responseObserver.onNext(FindMaxResponse.newBuilder()
                            .setMaxNumber(currentMaximum).build());
                }

            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(FindMaxResponse.newBuilder()
                        .setMaxNumber(currentMaximum).build());
                responseObserver.onCompleted();
            }
        };
        return requestStreamObserver;
    }

    @Override
    public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {
        Integer number  = request.getInputNumber();
        if (number > 0){
            double numberRoot = Math.sqrt(number);
            responseObserver.onNext(SquareRootResponse.newBuilder()
                    .setNumberRoot(numberRoot).build());
            System.out.println(numberRoot);
        }else{
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("The number being sent is not positive")
                    .augmentDescription("Number sent "+ number).asRuntimeException());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void findNthSum(FindNthSumRequest request, StreamObserver<FindNthSumResponse> responseObserver) {
        Integer number = request.getInputNumber();
        if(number > 0){
            long nthSum = (number * (number+1)/2);
            responseObserver.onNext(
                    FindNthSumResponse.newBuilder().setNthSUM(nthSum).build()
            );
            System.out.println(nthSum);
        }else {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Given input is not a positive number")
                    .augmentDescription("Number sent : "+number).asRuntimeException()
            );
            responseObserver.onCompleted();
        }
    }
}
