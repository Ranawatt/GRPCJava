package com.sugandh.grpcjava.grpc.blog.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;

public class BlogServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Blog Server Started");

        Server server = ServerBuilder.forPort(50053)
                .addService(new BlogServiceImpl())
                .addService(ProtoReflectionService.newInstance())
                .build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread( ()->{
            System.out.println("Received Shutdown Request");
            server.shutdown();
            System.out.println("Successfully Shutdown the server");
        }));

        server.awaitTermination();
    }
}
