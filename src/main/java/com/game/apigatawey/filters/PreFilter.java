package com.game.apigatawey.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;


@Component
public class PreFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(PreFilter.class);
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Pre filter executed...");
        String requestPath =  exchange.getRequest().getPath().toString();
        logger.info("Request path: {}", requestPath);
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        Set<String> headersNames = httpHeaders.keySet();
        headersNames.forEach(headersName -> {
            String headerValue = httpHeaders.getFirst(headersName);
            logger.info("{} - {} ",headersName, headerValue);
        });
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
