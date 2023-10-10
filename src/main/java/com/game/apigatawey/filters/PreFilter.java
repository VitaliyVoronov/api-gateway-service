package com.game.apigatawey.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;


@Slf4j
@Component
public class PreFilter implements GlobalFilter {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Pre filter executed...");
        String requestPath =  exchange.getRequest().getPath().toString();
        log.info("Request path: " + requestPath);
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        Set<String> headersNames = httpHeaders.keySet();
        headersNames.forEach((headersName) -> {
            String headerValue = httpHeaders.getFirst(headersName);
            log.info(headersName + " - " + headerValue);
        });
        return chain.filter(exchange);
    }
}
