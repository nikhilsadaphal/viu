package com.viu.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@PropertySource({ "classpath:persistence-multiple-db-boot.properties" })
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.viu.test1.repo", entityManagerFactoryRef = "test1EntityManager", transactionManagerRef = "test1TransactionManager")
public class Test1Config {
	@Primary
	@Bean()
	@ConfigurationProperties(prefix = "spring.test1.datasource")
	public DataSource test1DataSource() {
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "test1EntityManager")
	public LocalContainerEntityManagerFactoryBean test1EntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(test1DataSource()).properties(hibernateProperties()).packages("com.viu.test1.entity").persistenceUnit("test1Pu").build();
	}

	@Primary
	@Bean(name = "test1TransactionManager")
	public PlatformTransactionManager test1TransactionManager(@Qualifier("test1EntityManager") EntityManagerFactory test1EntityManagerFactory) {
		return new JpaTransactionManager(test1EntityManagerFactory);
	}
	
	private Map hibernateProperties() {

		Resource resource = new ClassPathResource("application.properties");

		try {
			Properties properties = PropertiesLoaderUtils.loadProperties(resource);
			System.out.println("properties are" +properties);

			return properties.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue()));
		} catch (IOException e) {
			return new HashMap();
		}
	}
	
	@Bean
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
	    return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
	}
}
