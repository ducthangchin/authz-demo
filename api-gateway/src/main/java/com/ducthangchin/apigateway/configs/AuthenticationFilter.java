package com.ducthangchin.apigateway.configs;

import com.ducthangchin.clientfeign.salary.SalaryClient;
import com.ducthangchin.clientfeign.user.AuthClient;
import com.ducthangchin.commons.models.UserDetails;
import com.ducthangchin.commons.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RefreshScope
@Component
@Slf4j
public class AuthenticationFilter implements GatewayFilter {
    @Value("${auth.jwt.secret}")
    private String jwtSecret;

    private final RouterValidator routerValidator;
    private final JwtUtils jwtUtils;

    public AuthenticationFilter(RouterValidator routerValidator, JwtUtils jwtUtils) {
        this.routerValidator = routerValidator;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request)) {
                return this.onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            final String token = this.getAuthHeader(request);

            try {
                if (!jwtUtils.isValidToken(token, jwtSecret)) {
                    return this.onError(exchange, HttpStatus.UNAUTHORIZED);
                }
            } catch (Exception e) {
                return this.onError(exchange, HttpStatus.FORBIDDEN);
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

//    private void updateRequest(ServerWebExchange exchange, UserDetails userDetails) {
//
//        exchange.getRequest().mutate()
//                .header("id", String.valueOf(userDetails.getId()))
//                .header("email", userDetails.getEmail())
//                .header("fullName", userDetails.getFullName())
//                .header("roles", String.join(",", userDetails.getRoles()))
//                .build();
//    }
}
