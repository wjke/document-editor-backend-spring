package com.natj.documents.configs;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = {"com.natj.documents.models"})
@EnableJpaRepositories(basePackages = {"com.natj.documents.repositories"})
public class RepositoryConfiguration {
}
