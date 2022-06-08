package ru.desinger.camel.axis;

import ru.desinger.springsoap.gen.CountriesPortService;
import ru.desinger.springsoap.gen.CountriesPortServiceLocator;
import ru.desinger.springsoap.gen.Country;
import ru.desinger.springsoap.gen.GetCountryRequest;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

public class StartupAxisClient {

    public static void main(String[] args) throws ServiceException, RemoteException, MalformedURLException {
        GetCountryRequest request = new GetCountryRequest();
        request.setName("Spain");

        CountriesPortService service = new CountriesPortServiceLocator(
            "classpath:countries.wsdl",
            //"http://minikube:32589/ws/countries.wsdl",
            new QName("http://www.desinger.ru/springsoap/gen", "CountriesPortService"));

        Country country = service.getCountriesPortSoap11(new URL(service.getCountriesPortSoap11Address())).getCountry(request).getCountry();

        System.out.println(country.getCapital());
        System.out.println(country.getPopulation());
        System.out.println(country.getCurrency());
    }
}
