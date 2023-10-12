package com.game.apigatawey.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Configuration
public class FiltersConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(FiltersConfiguration.class);
    
    @Order(1)
    @Bean
    public GlobalFilter secondPreFilter() {
        return ((exchange, chain) -> {
            logger.info("Second pre filter executed...");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> logger.info("Second post filter executed...")));
        });
    }

    @Order(2)
    @Bean
    public GlobalFilter thirdPreFilter() {
        return ((exchange, chain) -> {
            logger.info("Third pre filter executed...");
            return chain.filter(exchange);
        });
    }
}
