package com.game.apigatawey;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    
    private final Environment environment;

    public AuthorizationHeaderFilter(Environment environment) {
        super(Config.class);
        this.environment = environment;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return getError(exchange, HttpStatus.UNAUTHORIZED);
            }
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");
            if (!isJwtValid(jwt)) {
                return getError(exchange, HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        });
    }

    private boolean isJwtValid(String jwt) {
        
        String tokenSecret = environment.getProperty("token.secret");
        Algorithm algorithm = Algorithm.HMAC256(tokenSecret);

        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(jwt);

        String subject = null;
        try {
            subject = decodedJWT.getSubject();
        } catch (Exception ex) {
            return false;
        }

        if (subject == null || subject.isEmpty()) {
            return false;
        }
        
        return true;
    }

    private Mono<Void> getError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse httpResponse = exchange.getResponse();
        httpResponse.setStatusCode(httpStatus);
        return httpResponse.setComplete();
    }

    public static class Config {

    }
}
