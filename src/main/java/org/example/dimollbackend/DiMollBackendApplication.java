package org.example.dimollbackend;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DiMollBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiMollBackendApplication.class, args);
    }

}
