package org.example.app.converters.config;

import lombok.AllArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@AllArgsConstructor
public class TypeConversionConfig {

    private final Set<Converter<?, ?>> converters;
    private final Date2LocalDateConverter date2LocalDateConverter;
    private final LocalDate2DateConverter localDate2DateConverter;

    @Bean
    @Primary
    public ConversionService conversionService() {
        DefaultConversionService converterService = new DefaultConversionService();
        ConversionServiceFactory.registerConverters(converters, converterService);
        return converterService;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setImplicitMappingEnabled(true)
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
            .setMatchingStrategy(MatchingStrategies.STRICT)
        ;
        modelMapper.addConverter(this.date2LocalDateConverter);
        modelMapper.addConverter(this.localDate2DateConverter);
        return modelMapper;
    }

    @Component
    public static class Date2LocalDateConverter extends AbstractConverter<Date, LocalDate> {
        @Override
        protected LocalDate convert(Date date) {
            return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

    @Component
    public static class LocalDate2DateConverter extends AbstractConverter<LocalDate, Date> {
        @Override
        protected Date convert(LocalDate date) {
            return date == null ? null :
                Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        }
    }
}
