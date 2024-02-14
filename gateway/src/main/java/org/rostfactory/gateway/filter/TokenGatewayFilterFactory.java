package org.rostfactory.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TokenGatewayFilterFactory extends
        AbstractGatewayFilterFactory<TokenGatewayFilterFactory.Config> {

    @Value("${auth-api-url}")
    private String authUrl;
    public final String AUTHORIZATION = "Authorization";

    @Autowired
    private RestTemplate authService;

    public TokenGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            String token = getTokenFromRequest(exchange.getRequest());
            if (StringUtils.hasText(token)) {
                Boolean isValidToken = authService.getForObject("%s/validateToken?token=%s".formatted(authUrl, token), Boolean.class);
                if (isValidToken) return chain.filter(exchange);
            }
            return this.onErrorUnauthorized(exchange);
        });
    }

    public static class Config {
    }

    private Mono<Void> onErrorUnauthorized(ServerWebExchange exchange)  {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private String getTokenFromRequest(ServerHttpRequest request) {
        final String bearer = request.getHeaders().getFirst(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return "";
    }
}
