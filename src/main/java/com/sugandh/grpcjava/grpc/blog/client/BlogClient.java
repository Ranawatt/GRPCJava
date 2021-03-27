package com.sugandh.grpcjava.grpc.blog.client;

import com.proto.blog.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogClient {

    public static void main(String[] args) {
        System.out.println("Hello, I am A Grpc Client for Blog");
        BlogClient blogClient = new BlogClient();
        blogClient.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50053)
                .usePlaintext()
                .build();

        System.out.println("Creating Stub");

        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);
        Blog blog = Blog.newBuilder().setAuthorId("Stephane")
                .setContent("New BLOG! ")
                .setTitle("Hello World, This is my first Blog .").build();

        CreateBlogResponse response = blogClient.createBlog(CreateBlogRequest.newBuilder()
                .setBlog(blog).build());

        System.out.println("Received Create Blog Response");
        System.out.println(response.toString());

        String blogId = response.getBlog().getId();
        ReadBlogResponse readBlogResponse = blogClient.readBlog(
                ReadBlogRequest.newBuilder().setBlogId(blogId).build());

        System.out.println("Received Read Blog Response");
        System.out.println(readBlogResponse.toString());

    }
}
