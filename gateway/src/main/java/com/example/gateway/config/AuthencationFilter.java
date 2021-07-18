package com.example.gateway.config;

import com.example.gateway.util.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Component
@RefreshScope
@Slf4j
public class AuthencationFilter implements GatewayFilter {
    @Autowired
    private JwtProvider jwtProvider;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        log.info("path: " + path);
        try {
            switch (path) {
                case "/auth/login":
                    return chain.filter(exchange);
                case "/users/":
                case "/departments/":
                    return isValidRole(request, "ADMIN") ?
                            chain.filter(exchange) :
                            onError(exchange, "No Permission!", HttpStatus.UNAUTHORIZED);
                default:
                    return onError(exchange, "Path Url does not exist", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return onError(exchange, "Error at filter authencation gateway API", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getTokenInHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isInvalidToken(ServerHttpRequest request) {
        if (!request.getHeaders().containsKey("Authorization")) {
            return true;
        }
        String token = getTokenInHeader(request);
        if (token != null && token.startsWith("Bearer"))
            return !jwtProvider.validateToken(token.substring(7));
        else
            return true;
    }

    private boolean isValidRole(ServerHttpRequest request, String role) {
        if (isInvalidToken(request))
            return false;
        String token = getTokenInHeader(request);
        Claims claims = jwtProvider.getAllClaimsFromToken(token.substring(7));
        String tokenRole = (String) claims.get("role");
        if (StringUtils.hasText(tokenRole) && tokenRole.toUpperCase().equals(role))
            return true;
        else
            return false;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
