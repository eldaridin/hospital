package com.hospital.hospitalization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HospitalizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalizationApplication.class, args);
    }
}