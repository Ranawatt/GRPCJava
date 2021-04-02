package com.sugandh.grpcjava.grpc.calculator.client;

import com.sugandh.grpcjava.grpc.calculator.server.CalculatorServer;

public class CalculatorClient {

    public static void main(String[] args) {
        System.out.println("Hello, I am Grpc Client for Calculator module");
        CalculatorClient mainClient = new CalculatorClient();
        mainClient.run();
    }

    private void run() {
    }
}
