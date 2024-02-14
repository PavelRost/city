package org.rostfactory.sharemodule.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "url")
public class UrlConfigProperties {
    private String authServiceUrl;
    private String bankServiceUrl;
    private String carServiceUrl;
    private String citizenServiceUrl;
    private String houseServiceUrl;
    private String logServiceUrl;
    private String passportServiceUrl;
    private String policeServiceUrl;
    private String schoolServiceUrl;
    private String logServiceUrlForWebSocket;
}
