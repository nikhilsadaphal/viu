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

import com.viu.test2.entity.TestEntity2;

@Configuration
//@PropertySource({ "classpath:persistence-multiple-db-boot.properties" })
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.viu.test2.repo", entityManagerFactoryRef = "test2EntityManager", transactionManagerRef = "test2TransactionManager")
public class Test2Config {
	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.test2.datasource")
	public DataSource test2DataSources() {
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "test2EntityManager")
	public LocalContainerEntityManagerFactoryBean test2EntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(test2DataSources()).properties(hibernateProperties()).packages("com.viu.test2.entity").persistenceUnit("test2PU").build();
	}

	@Primary
	@Bean(name = "test2TransactionManager")
	public PlatformTransactionManager test2TransactionManager(@Qualifier("test2EntityManager") EntityManagerFactory test2EntityManagerFactory) {
		return new JpaTransactionManager(test2EntityManagerFactory);
	}

	private Map hibernateProperties() {

		Resource resource = new ClassPathResource("application.properties");

		try {
			Properties properties = PropertiesLoaderUtils.loadProperties(resource);

			return properties.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue()));
		} catch (IOException e) {
			return new HashMap();
		}
	}

}
