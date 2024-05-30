package com.ducthangchin.apigateway.configs;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class GatewayConfig {
    private final AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user", r -> r.path("/api/v1/user/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://USER"))
                .route("document", r -> r.path("/api/v1/document/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://DOCUMENT"))
                .route("salary", r -> r.path("/api/v1/salary/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://SALARY"))
                .build();
    }
}