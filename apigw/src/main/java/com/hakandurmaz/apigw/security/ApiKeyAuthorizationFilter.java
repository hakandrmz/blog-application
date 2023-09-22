package com.hakandurmaz.apigw.security;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyAuthorizationFilter implements GlobalFilter, Ordered {

  private final ApiKeyAuthorizationChecker apiKeyAuthorizationChecker;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    log.info("ApiKeyAuthorizationFilter checking the key.");

    Route attribute = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
    String application = attribute.getId();

    List<String> apiKey = exchange.getRequest().getHeaders().get("ApiKey");

    if (application == null
        || Objects.requireNonNull(apiKey).isEmpty()
        || !apiKeyAuthorizationChecker.isAuthorized(apiKey.get(0), application)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized.");
    }

    return chain.filter(exchange);
  }

  @Override
  public int getOrder() {
    return Ordered.LOWEST_PRECEDENCE;
  }
}
