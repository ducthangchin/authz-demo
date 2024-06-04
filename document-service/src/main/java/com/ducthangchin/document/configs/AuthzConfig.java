package com.ducthangchin.document.configs;

import com.ducthangchin.commons.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthzConfig {
    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }
}
