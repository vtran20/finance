package com.easysoft.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
@EnableScheduling
@EntityScan("com.easysoft.finance.domain")
@EnableJpaRepositories("com.easysoft.finance.repository")
@EnableJpaAuditing /*Spring Boot uses AuditingEntityListener to automatically populate the createdAt and updatedAt fields*/
@EnableCaching
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}