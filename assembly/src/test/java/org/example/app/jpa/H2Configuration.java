package org.example.app.jpa;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.HSQLDialect;
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

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
class H2Configuration {

    @Bean
    public DataSource dataSource(@Value("${h2.jdbc.url}") String jdbcUrl) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(org.h2.Driver.class.getName());
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername("user");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean containerEntityManagerFactoryBean(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("org.example.app.jpa.h2entity");
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties properties = new Properties();
        properties.setProperty(Environment.DRIVER, org.h2.Driver.class.getName());
        properties.put(Environment.HBM2DDL_AUTO, "create-drop");
        properties.put("javax.persist.validation.mode", "none");
        properties.put(Environment.SHOW_SQL, false);
        properties.setProperty(Environment.IMPLICIT_NAMING_STRATEGY, "component-path");
        properties.setProperty(Environment.HBM2DDL_IMPORT_FILES, "legal-party.sql");
        properties.setProperty(Environment.HBM2DDL_CHARSET_NAME, "UTF-8");
        properties.setProperty(Environment.DIALECT, HSQLDialect.class.getName());
        entityManagerFactoryBean.setJpaProperties(properties);
        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new JpaTransactionManager();
    }
}
