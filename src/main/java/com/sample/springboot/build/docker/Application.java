package com.sample.springboot.build.docker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * spring-boot: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle
 * spring-boot-docker: https://spring.io/guides/gs/spring-boot-docker
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
