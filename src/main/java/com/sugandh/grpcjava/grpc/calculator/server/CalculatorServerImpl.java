package com.sugandh.grpcjava.grpc.calculator.server;

import com.proto.calculator.*;
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
}
