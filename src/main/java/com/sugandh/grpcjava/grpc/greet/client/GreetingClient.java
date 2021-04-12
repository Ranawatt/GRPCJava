package com.sugandh.grpcjava.grpc.greet.client;

import com.proto.greet.*;
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
        doServerStreamingCall(managedChannel);
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

    private void doServerStreamingCall(ManagedChannel channel){
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        GreetManyTimesRequest manyTimesRequest = GreetManyTimesRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Sugandh")).build();
        greetClient.greetManyTimes(manyTimesRequest).forEachRemaining(greetManyTimesResponse -> {
            System.out.println(greetManyTimesResponse.getResult());
        });
    }
}
