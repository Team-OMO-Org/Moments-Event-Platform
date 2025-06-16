package io.github.teamomo.gateway.routes;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;

@Configuration
public class Routes {

    @Bean
    public RouterFunction<ServerResponse> momentServiceRoute() {
        return GatewayRouterFunctions.route("moment_service")
                .route(RequestPredicates.path("/api/v1/moments")
                        .or(RequestPredicates.path("/api/v1/moments/**")),
                    HandlerFunctions.http())
                .filter(lb("moment-service"))
                // use the circuit breaker to handle failures, forward to fallbackRoute
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> momentServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("moment_service_swagger")
                .route(RequestPredicates.path("/aggregate/moment-service/v3/api-docs"),
                        HandlerFunctions.http())
                .filter(lb("moment-service"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("momentServiceSwaggerCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))   // replaces the path
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        return GatewayRouterFunctions.route("order_service")
                .route(RequestPredicates.path("/api/v1/orders")
                        .or(RequestPredicates.path("/api/v1/orders/**")),
                    HandlerFunctions.http())
                .filter(lb("order-service"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("order_service_swagger")
                .route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"),
                        HandlerFunctions.http())
                .filter(lb("order-service"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceSwaggerCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))   // replaces the path
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> customerServiceRoute() {
        return GatewayRouterFunctions.route("customer_service")
            .route(RequestPredicates.path("/api/v1/customers")
                    .or(RequestPredicates.path("/api/v1/customers/**")),
                HandlerFunctions.http())
            .filter(lb("customer-service"))
            .filter(CircuitBreakerFilterFunctions.circuitBreaker("customerServiceCircuitBreaker",
                URI.create("forward:/fallbackRoute")))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> customerServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("customer_service_swagger")
            .route(RequestPredicates.path("/aggregate/customer-service/v3/api-docs"),
                HandlerFunctions.http())
            .filter(lb("customer-service"))
            .filter(CircuitBreakerFilterFunctions.circuitBreaker("customerServiceSwaggerCircuitBreaker",
                URI.create("forward:/fallbackRoute")))
            .filter(setPath("/api-docs"))   // replaces the path
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return GatewayRouterFunctions.route("fallback")
                .GET("/fallbackRoute",
                        request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service unavailable, please try again later"))
                .build();
    }

}
