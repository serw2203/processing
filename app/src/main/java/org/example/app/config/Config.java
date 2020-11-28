package org.example.app.config;


import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;

@Configuration
@Slf4j
public class Config implements InitializingBean {

    @Bean
    public EventBus bus() {
        return new EventBus();
    }

    @Component
    public static class BeanPP implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof BeanList) log.info(">>>>>>> postProcessBeforeInitialization - {}", beanName);
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof BeanList) log.info("<<<<<<< postProcessAfterInitialization - {}", beanName);
            return bean;
        }
    }

    //@Bean
//    public BeanPP beanPP() {
//        return new BeanPP();
//    }


    public interface BeanList {
        String toString();
    }

    public static class BeanListImpl implements BeanList{
        @Override
        public String toString() {
            return "BeanList{} " + new Random().nextInt();
        }
    }

    @Bean
    public BeanList list() {
        return new BeanListImpl();
    }

    @Autowired
    private BeanList list;

    public Config() {
        System.out.println("Constructor ---->>>>>>>>");
        System.out.println(log);
        System.out.println(list);
    }

    @PostConstruct
    public void init() {
        log.info("Init config");
        log.info("{}", list);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("After property set");
        log.info("{}", list);
    }

//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        binder.setMessageCodesResolver(new DefaultMessageCodesResolver());
//    }

}
