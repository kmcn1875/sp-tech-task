package com.mcn.sp.tech.task.it.cucumber.config;

import com.mcn.sp.tech.task.it.utils.DatabaseUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class IntegrationTestConfig {

    @Bean
    public DatabaseUtils databaseUtils(EntityManager entityManager) {
        return new DatabaseUtils(entityManager);
    }

}
