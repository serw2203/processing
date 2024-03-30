package org.example.app.rmi;

import lombok.extern.slf4j.Slf4j;
import org.example.app.service.calculator.CalculateAction;
import org.example.app.service.calculator.CalculateActionAdd;
import org.example.app.service.calculator.CalculateActionMultiple;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class CalculateConfiguration {

    @Bean
    public CalculateAction calculateActionAdd() {
        return new CalculateActionAdd();
    }

    @Bean
    public CalculateAction calculateActionMultiple() {
        return new CalculateActionMultiple();
    }

//    @Bean
//    public RmiServiceExporter calculateActionAddExporter(CalculateActionAdd actionAdd) {
//        RmiServiceExporter exporter = new RmiServiceExporter();
////        exporter.setRegistryHost("localhost");
//        exporter.setServiceInterface(CalculateAction.class);
//        exporter.setService(actionAdd);
//        exporter.setServiceName("CalculateActionAdd");
//        exporter.setRegistryPort(1099);
//        return exporter;
//    }
//
//    @Bean
//    public RmiServiceExporter calculateActionMultipleExporter(CalculateActionMultiple actionAdd) {
//        RmiServiceExporter exporter = new RmiServiceExporter();
////        exporter.setRegistryHost("localhost");
//        exporter.setServiceInterface(CalculateAction.class);
//        exporter.setService(actionAdd);
//        exporter.setServiceName("CalculateActionMultiple");
//        exporter.setRegistryPort(1099);
//        return exporter;
//    }
}
