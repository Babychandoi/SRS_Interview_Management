package com.example.ims_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

import com.example.ims_backend.config.RSAKeyRecord;

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyRecord.class)
@EnableAsync
public class ImsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImsBackendApplication.class, args);
    }

}
