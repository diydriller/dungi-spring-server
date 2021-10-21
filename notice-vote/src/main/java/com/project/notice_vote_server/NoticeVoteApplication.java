package com.project.notice_vote_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.project.common")
@EnableJpaRepositories(basePackages = {"com.project.common","com.project.notice_vote_server"})
@ComponentScan(basePackages = {"com.project.notice_vote_server","com.project.common"})
@EnableJpaAuditing
@EnableEurekaClient
public class NoticeVoteApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoticeVoteApplication.class, args);
    };
}
