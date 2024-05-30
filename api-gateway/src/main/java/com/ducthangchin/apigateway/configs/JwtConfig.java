package com.ducthangchin.apigateway.configs;

import com.ducthangchin.commons.utils.JwtUtils;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtConfig {
    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }
}
