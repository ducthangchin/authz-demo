package com.ducthangchin.salary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(
        basePackages = "com.ducthangchin.clientfeign"
)

@PropertySource("classpath:client-feign-${spring.profiles.active}.properties")
public class SalaryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SalaryServiceApplication.class);
    }
}
