package com.ducthangchin.user.configs;

import com.ducthangchin.user.utils.DTOConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DTOConfig {
    @Bean
    public DTOConverter dtoConverter() {
        return new DTOConverter();
    }
}
