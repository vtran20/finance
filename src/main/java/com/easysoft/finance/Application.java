package com.easysoft.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
@EnableScheduling
@EntityScan("com.easysoft.finance.domain")
@EnableJpaRepositories("com.easysoft.finance.repository")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}