package com.sugandh.grpcjava.grpc.greet.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.File;
import java.io.IOException;

public class GreetServer {
    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("Hello GRPC from GreetServer");
        Server server = ServerBuilder.forPort(50051)
                .addService(new GreetServiceImpl())
                .useTransportSecurity(new File("ssl/server.crt"),
                        new File("ssl/server.pem")).build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            System.out.println("Received Shutdown request");
            server.shutdown();
            System.out.println("Successfully shutdown the server");
        }));

        server.awaitTermination();
    }
}
