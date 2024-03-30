package org.example;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.*;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
public class FirebirdConfiguration {

    @Bean
    public DataSource dataSource(@Value("${firebirdsql.jdbc.url}") String jdbcUrl) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(org.firebirdsql.jdbc.FBDriver.class.getName());
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername("SYSDBA");
        dataSource.setPassword("masterkey");
        return dataSource;
    }

    @Bean("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean containerEntityManagerFactoryBean(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("org.example.entity");
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties properties = new Properties();
        properties.setProperty(Environment.DRIVER, org.firebirdsql.jdbc.FBDriver.class.getName());
        properties.put(Environment.HBM2DDL_AUTO, "update");
        properties.put("javax.persist.validation.mode", "none");
        properties.put(Environment.SHOW_SQL, true);
        properties.setProperty(Environment.IMPLICIT_NAMING_STRATEGY, "component-path");
        properties.setProperty(Environment.HBM2DDL_CHARSET_NAME, "UTF-8");
        properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.FirebirdDialect");
        entityManagerFactoryBean.setJpaProperties(properties);
        return entityManagerFactoryBean;
    }

    @Bean("transactionManager")
    public PlatformTransactionManager platformTransactionManager() {
        return new JpaTransactionManager();
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory (EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public UserDao userDao () {
        return new UserDaoImpl();
    }
}
