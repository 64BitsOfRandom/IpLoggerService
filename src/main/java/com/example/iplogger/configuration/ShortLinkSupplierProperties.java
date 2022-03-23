package com.example.iplogger.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "application.short-link-supplier")
public class ShortLinkSupplierProperties {
    private int length;
}
