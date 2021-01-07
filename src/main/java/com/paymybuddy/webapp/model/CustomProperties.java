package com.paymybuddy.webapp.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class CustomProperties {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

}
