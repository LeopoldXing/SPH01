package com.hilda.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "url.pattern")
public class AuthUrlProperties {

    String loginPage;

    List<String> directPassUrl;

    List<String> requiredAuthUrl;

    List<String> innerUrl;

}
