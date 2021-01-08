package org.example.app.grpc;

import com.google.common.base.Joiner;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testng.util.Strings;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class GrpcConfiguration {

    private @Value("${proto-service.port}")
    Integer port;

    @Bean
    public Server protoServiceGrpcServer() {
        return NettyServerBuilder.forPort(port).addService(new ProtoServiceGrpc.ProtoServiceImplBase() {
            @Override
            public void handle(ProtoRequest request, StreamObserver<ProtoResponse> responseObserver) {
                ProtoResponse response = ProtoResponse.newBuilder()
                    .setGreeting(Strings.join(" ",
                        new String[]{"Hello", request.getName(),
                            request.getLastNameList() == null ? "" : Joiner.on(" ").join(request.getLastNameList())})
                    ).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        }).build();
    }

    @PostConstruct
    public void init() throws InterruptedException, IOException {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                protoServiceGrpcServer().shutdown().awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }));
        protoServiceGrpcServer().start();
    }
}
