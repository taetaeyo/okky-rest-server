package com.okky.restserver.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j; 
/**
 * @author taekwon
 * @description dynamic data source configuration (Hikari connection pool)
 */

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = {"com.okky.restserver.repository"},
		entityManagerFactoryRef = "jpaManagerFactory",
		transactionManagerRef  = "jpaTm"
)
class DataSourceConfig {

	@Bean(name="primaryHikariConfig")
    @ConfigurationProperties(prefix="spring.datasource.hikari.primary")
    HikariConfig primaryHikariConfig() {
        return new HikariConfig();
    }

    @Bean(name="primaryDataSource")
    DataSource primaryDataSource() {
        return new HikariDataSource(primaryHikariConfig());
    }
    
    @Bean(name = "dynamicDataSource")
    @Primary
    DynamicDataSource dynamicDataSource(@Qualifier("primaryDataSource") DataSource primaryDataSource) {
    	
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put("primary", primaryDataSource);

        DynamicDataSource routingDataSource = new DynamicDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(primaryDataSource);

        return routingDataSource;
    }

    @Bean(name="jpaManagerFactory")
    LocalContainerEntityManagerFactoryBean entityManagerFactory( EntityManagerFactoryBuilder builder, @Qualifier("dynamicDataSource") DynamicDataSource dynamicDataSource) {
    	
    	Map<String,Object> prop = new HashMap<>();
    	
    	prop.put("hibernate.physical_naming_strategy", CamelCaseToUnderscoresNamingStrategy.class.getName());

    	return builder
    			.dataSource(dynamicDataSource)
    			.packages("com.okky.restserver.domain")
    			.properties(prop)
    			.build();
    }

    @Bean(name="jpaTm")
    JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
    	return new JpaTransactionManager(entityManagerFactory);
    }
}