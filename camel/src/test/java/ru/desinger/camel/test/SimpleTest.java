package ru.desinger.camel.test;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = SimpleTest.TestSimple.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = SimpleTest.TestConfiguration2.class)
public class SimpleTest extends Assert {

    @Autowired
    private CamelContext camelContext;

    @EndpointInject("mock:result")
    protected MockEndpoint resultEndpoint;

    @Produce("direct:start")
    protected ProducerTemplate template;

    @SpringBootApplication
    static class TestSimple {
        public static void main(String[] args) {
            SpringApplication.run(TestSimple.class, args);
        }
    }

    @Configuration
    static class TestConfiguration2 {
        @Bean
        public RouteBuilder routeBuilder() {
            return new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:start")
                        .log(" <><><><><><><> ${body}")
                        .filter((v) -> v.getIn().getBody(String.class).equals("<matched/>"))
                        .to("mock:result").id("channel2");

                    from("direct:start2")
                        .log(" <><><><><><><> ${body}")
                        .filter((v) -> v.getIn().getBody(String.class).equals("<matched/>"))
                        .to("mock:result").id("channel3");
                }
            };
        }
    }

    @Test
    public void t0() {
        assertNotNull(camelContext);
        assertNotNull(template);
        assertNotNull(resultEndpoint);
    }

    @Test
    public void testSendMatchingMessage() throws Exception {
        String expectedBody = "<matched/>";
        resultEndpoint.expectedBodiesReceived(expectedBody);
        resultEndpoint.expectedHeaderReceived("foo", "bar");
        template.sendBodyAndHeader("direct:start", expectedBody, "foo", "bar");
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void testSendNotMatchingMessage() throws Exception {
        resultEndpoint.expectedMessageCount(0);
        template.sendBodyAndHeader("direct:start", "<notMatched/>", "foo", "notMatchedHeaderValue");
        resultEndpoint.assertIsSatisfied();
    }

}
