package org.rostfactory.citizen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.rostfactory")
public class CitizenApplication {
    public static void main(String[] args) {
        SpringApplication.run(CitizenApplication.class, args);
    }
}
