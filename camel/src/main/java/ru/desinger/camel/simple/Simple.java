package ru.desinger.camel.simple;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.webjars.WebJarAssetLocator;

import java.io.IOException;
import java.util.Set;

@SpringBootApplication
public class Simple {

    @Component
    public static class HelloBean {
        public String hello() {
            return "Hello from bean";
        }
    }

    @Component
    public static class MyRoute2 extends RouteBuilder {
        @Override
        public void configure() {
            restConfiguration().component("servlet")
                .host("localhost")
                .port(8081).bindingMode(RestBindingMode.auto);

            rest("/hello").get()
                .route().id("hello")
                .log("${headers}")
                .to("direct:start")
                .endRest();

            // START SNIPPET: e1
            from("direct:start")
                .to("log:started")
                .transform().simple("Hello ${header.param1} ${header.param2}")
                .log("---- ${body}")
                .to("mock:result");
            // END SNIPPET: e1
        }
    }

    //@Component
    public static class MyRoute1 extends RouteBuilder {
        @Override
        public void configure() {
            restConfiguration().component("undertow")
                .host("localhost")
                .port(8081).bindingMode(RestBindingMode.auto);

            rest("/hello").get()
                .route().id("hello")
                .to("direct:start")
                .endRest();

            // START SNIPPET: e1
            from("direct:start")
                .to("log:foo")
                .wireTap("direct:tap")
                .to("log:foo1")
                .to("mock:result");
            // END SNIPPET: e1

            from("direct:tap")
                .to("log:foo_1")
                .delay(10000).setBody().constant("Tapped")
                .to("log:foo_0")
                .to("mock:result", "mock:tap");

            from("direct:test").wireTap("direct:a").id("wiretap_1").to("mock:a");
            from("direct:a").to("mock:b");
        }
    }


    //@Component
    public static class MyRoute extends RouteBuilder {
        @Override
        public void configure() {
            restConfiguration().component("undertow")
                .host("localhost")
                .port(8081).bindingMode(RestBindingMode.auto);

            restConfiguration().component("undertow")
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "User API").apiProperty("api.version", "1.2.3")
                // and enable CORS
                .apiProperty("cors", "true");

//            from("timer:myTimer?delay=2000")
//                .log("Hello ${body}")
//                .to("log:?level=INFO")
//                .process(e ->
//                    System.out.println("")
//                )
//                .end();

            rest("/hello").get()
                .route().id("hello")
                .to("log:l1")
                .process(e -> {
                        System.out.println("");
                        e.getOut().setBody("OK");
                    }
                )
                .endRest();

            rest("/hello-bean").get()
                .route().id("hello-bean")
                .bean(Simple.HelloBean.class, "hello")
                .endRest();

            WebJarAssetLocator locator = new WebJarAssetLocator();
            Set<String> m = locator.listAssets("springfox-swagger-ui");
            for (String r : m) {
                String asset = r.replaceAll("META-INF/resources/webjars/springfox-swagger-ui", "");
                from("direct:" + asset)
                    .routeId(asset).setBody().simple("resource:classpath:" + r);

                rest(asset).get()
                    .produces("*")
                    .responseMessage().code(200)
                    .endResponseMessage()
                    .to("direct:" + asset);
            }
        }

    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Simple.class, args);

//        ClassLoader cl = URLClassLoader.class.getClassLoader();
//        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
//        Resource[] resources = resolver.getResources("classpath:META-INF/resources/webjars/springfox-swagger-ui/*") ;
//        for (Resource resource: resources){
//            System.out.println(resource.getFilename());
//        }

//        WebJarAssetLocator locator = new WebJarAssetLocator();
//        Set<String> m = locator.listAssets("springfox-swagger-ui");
//        for (String r : m) {
//            System.out.println(r.replaceAll("META-INF/resources/webjars/springfox-swagger-ui", ""));
//
//        }
    }
}
