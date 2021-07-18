package com.example.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Autowired
    private AuthencationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("USER-SERVICE", r -> r.path("/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://USER-SERVICE"))
                .route("DEPARTMENT-SERVICE", r -> r.path("/departments/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://DEPARTMENT-SERVICE"))
                .route("AUTH-SERVICE", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://AUTH-SERVICE"))
                .build();

    }
}

//  cloud:
//          gateway:
//          routes:
//          - id: USER-SERVICE
//          uri: lb://USER-SERVICE
//          predicates:
//          - Path=/users/**
// - id: DEPARTMENT-SERVICE
// uri: lb://DEPARTMENT-SERVICE
// predicates:
// - Path=/departments/**
// - id: AUTH-SERVICE
// uri: lb://AUTH-SERVICE
// predicates:
// - Path=/auth/**
