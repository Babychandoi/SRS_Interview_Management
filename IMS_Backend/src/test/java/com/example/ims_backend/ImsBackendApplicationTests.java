package com.example.ims_backend;

import com.example.ims_backend.config.RSAKeyRecord;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableConfigurationProperties(RSAKeyRecord.class)
class ImsBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
