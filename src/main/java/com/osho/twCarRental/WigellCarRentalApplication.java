package com.osho.twCarRental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


// Version 221027 with all methods, including redundant and non-required methods;
// and with microservice configured


@EnableEurekaClient // Manually added; might need
@SpringBootApplication
public class WigellCarRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(WigellCarRentalApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
