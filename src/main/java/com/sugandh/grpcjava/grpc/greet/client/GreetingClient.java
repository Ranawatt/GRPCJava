package com.sugandh.grpcjava.grpc.greet.client;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

import javax.net.ssl.SSLException;
import java.io.File;

public class GreetingClient {
    public static void main(String[] args) throws SSLException {
        System.out.println("Hello I am a GRPC client");

        GreetingClient mainClient = new GreetingClient();
        mainClient.run();
    }

    private void run() throws SSLException {
        ManagedChannel managedChannel = NettyChannelBuilder.forAddress("localhost", 50051)
                .sslContext(GrpcSslContexts.forClient()
                        .trustManager(new File("ssl/ca.crt")).build()
                ).build();
        System.out.println("Creating Stub");

        doUnaryCall(managedChannel);
    }

    private void doUnaryCall(ManagedChannel managedChannel) {
        GreetServiceGrpc.GreetServiceBlockingStub greetClient =
                GreetServiceGrpc.newBlockingStub(managedChannel);
        // Created protocol buffer greeting message
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Sugandh")
                .setLastName("Ranawatt")
                .build();
        //Unary RPC Calls
        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting).build();
        GreetResponse greetResponse = greetClient.greet(greetRequest);
        System.out.println(greetResponse.getResult());

    }
}
