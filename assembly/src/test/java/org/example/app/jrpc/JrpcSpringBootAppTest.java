package org.example.app.jrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.example.app.grpc.ProtoRequest;
import org.example.app.grpc.ProtoServiceGrpc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest(classes = {JrpcConfiguration.class})
@Slf4j
public class JrpcSpringBootAppTest {

    private @Value("${proto-service.port}")
    Integer port;

    public String greeting(String name, String... lastName) {
        ProtoRequest request = ProtoRequest.newBuilder()
            .setName(name)
            .addAllLastName(Lists.newArrayList(lastName))
            .build();

        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:" + this.port)
            .usePlaintext()
            .build();

        ProtoServiceGrpc.ProtoServiceBlockingStub stub = ProtoServiceGrpc.newBlockingStub(channel);

        return stub.handle(request).getGreeting();
    }

    @Test
    public void test_0() {
        String greeting = this.greeting("Sergey", "Vladimirovich", "Barkhatov");
        log.info(greeting);
        Assertions.assertEquals("Hello Sergey Vladimirovich Barkhatov", greeting);
    }
    @Test
    public void test_1() {
        String greeting = this.greeting("Sergey", "Vladimirovich", "Barkhatov", "Mr");
        log.info(greeting);
        Assertions.assertEquals("Hello Sergey Vladimirovich Barkhatov Mr", greeting);
    }
}
