package ru.desinger.ws;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import ru.desinger.springsoap.gen.Country;
import ru.desinger.springsoap.gen.Currency;
import ru.desinger.springsoap.gen.GetCountryRequest;
import ru.desinger.springsoap.gen.GetCountryResponse;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class StartupWs {

    @Component
    public static class CountryRepository {
        private static final List<Country> countries = new ArrayList<Country>();

        @PostConstruct
        public void initData() {
            Country spain = new Country();
            spain.setName("Spain");
            spain.setCapital("Madrid");
            spain.setCurrency(Currency.EUR);
            spain.setPopulation(46704314);

            countries.add(spain);
        }

        public Country findCountry(String name) {

            Country result = null;

            for (Country country : countries) {
                if (name.equals(country.getName())) {
                    result = country;
                }
            }

            return result;
        }
    }

    @Endpoint
    public static class CountryEndpoint {

        private static final String NAMESPACE_URI = "http://www.desinger.ru/springsoap/gen";

        private final CountryRepository countryRepository;

        @Autowired
        public CountryEndpoint(CountryRepository countryRepository) {
            this.countryRepository = countryRepository;
        }

        @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
        @ResponsePayload
        public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request) {
            GetCountryResponse response = new GetCountryResponse();
            response.setCountry(countryRepository.findCountry(request.getName()));

            return response;
        }
    }

    @EnableWs
    @Configuration
    public static class WebServiceConfig extends WsConfigurerAdapter {
        @Bean
        public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
            MessageDispatcherServlet servlet = new MessageDispatcherServlet();
            servlet.setApplicationContext(applicationContext);
            servlet.setTransformWsdlLocations(true);
            return new ServletRegistrationBean(servlet, "/ws/*");
        }

        @Bean(name = "countries")
        public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema) {
            DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
            wsdl11Definition.setPortTypeName("CountriesPort");
            wsdl11Definition.setLocationUri("/ws");
            wsdl11Definition.setTargetNamespace("http://www.desinger.ru/springsoap/gen");
            wsdl11Definition.setCreateSoap12Binding(true);
            wsdl11Definition.setSchema(countriesSchema);
            return wsdl11Definition;
        }

        @Bean
        public XsdSchema countriesSchema() {
            return new SimpleXsdSchema(new ClassPathResource("countries.xsd"));
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(StartupWs.class, args);
    }

}
