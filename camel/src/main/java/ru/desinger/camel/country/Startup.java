package ru.desinger.camel.country;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.desinger.springsoap.gen.CountriesPort;
import ru.desinger.springsoap.gen.CountriesPortServiceLocator;
import ru.desinger.springsoap.gen.Country;
import ru.desinger.springsoap.gen.GetCountryRequest;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

@SpringBootApplication
public class Startup {


    public interface CountryService {
        Country country(String name) throws RemoteException;
    }

    @Service
    public static class CountryServiceImpl implements CountryService {

        @Autowired
        public CountriesPort countriesPort;

        @Override
        public Country country(String name) throws RemoteException {
            GetCountryRequest request = new GetCountryRequest();
            request.setName(name);
            return countriesPort.getCountry(request).getCountry();
        }
    }

    @Configuration
    public static class CountriesServiceConfig {

        @Bean
        public CountriesPort countriesPort() throws ServiceException {
            return new CountriesPortServiceLocator(
                "http://minikube:32589/ws/countries.wsdl",
                new QName("http://www.desinger.ru/springsoap/gen", "CountriesPortService")).getCountriesPortSoap11();
        }
    }

    @Component
    public static class CountryRouter extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            onException(RuntimeException.class)
                // here we tell Camel to continue routing
                .continued(false)
                // after it has built this special timeout error message body
                .setBody(simple("Time out error!!!"));

            errorHandler(defaultErrorHandler());

            rest("/hello").get()
                .route().id("hello")
                .removeHeaders("*")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/xml"))
                .setHeader("Accept", constant("text/xml"))
                .process(exchange -> {
                    Object body  = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
                        "                  xmlns:gs=\"http://www.desinger.ru/springsoap/gen\">" +
                        "    <soapenv:Header/>" +
                        "    <soapenv:Body>" +
                        "        <gs:getCountryRequest>" +
                        "            <gs:name>Spain</gs:name>" +
                        "        </gs:getCountryRequest>" +
                        "    </soapenv:Body>" +
                        "</soapenv:Envelope>";

                    exchange.getIn().setBody(body);
                })
                .to("http://minikube:32589/ws")

//                .removeHeaders("*")
//                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
//                .setHeader(Exchange.CONTENT_TYPE, constant("text/xml"))
//                .to("http://minikube:31160/info1")

                .endRest();
        }
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        //ServletRegistrationBean servlet = new ServletRegistrationBean(new CamelHttpTransportServlet(), "/*");
        //servlet.setName("CamelServlet");
        //return servlet;
        return null;
    }

/*    @Component
    public static class MyRoute extends RouteBuilder {
        @Override
        public void configure() {
            from("timer:foo?delay=5000")
                .to("http://minikube:31160/info1")
                .process(e -> System.out.println(e.getIn().getBody(String.class)));
        }
    }*/

    public static void main(String[] args) throws RemoteException {
        ConfigurableApplicationContext context = SpringApplication.run(Startup.class, args);

        RouteBuilder b = context.getBean(RouteBuilder.class);

        System.out.println(b);
//        CountryService countryService = context.getBean(CountryService.class);
//        Country country = countryService.country("Spain");
//        System.out.println("\n---\n");
//        System.out.println(country.getCapital());
    }
}
