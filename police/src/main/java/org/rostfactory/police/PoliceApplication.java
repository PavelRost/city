package org.rostfactory.police;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.rostfactory")
public class PoliceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PoliceApplication.class, args);
    }
}
