package com.project.chart_server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages = "com.project.common")
@EnableJpaRepositories(basePackages = "com.project.common")
@ComponentScan(basePackages = {"com.project.chart_server","com.project.common"})
@EnableEurekaClient
@EnableJpaAuditing
@EnableScheduling
public class ChartApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChartApplication.class, args);
    }
}
