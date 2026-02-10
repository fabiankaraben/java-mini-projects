package com.fabiankaraben.reverseproxy.config;

import com.fabiankaraben.reverseproxy.filter.CustomHeaderFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${app.upstream.service-url}")
    private String serviceUrl;

    @Value("${app.upstream.echo-url}")
    private String echoUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, CustomHeaderFilter customHeaderFilter) {
        return builder.routes()
                .route("service_route", r -> r.path("/api/service/**")
                        .filters(f -> f.stripPrefix(2)
                                .filter(customHeaderFilter.apply(config -> config.setHeaderValue("Gateway-Processed"))))
                        .uri(serviceUrl))
                .route("echo_route", r -> r.path("/echo/**")
                        .filters(f -> f.stripPrefix(1)
                                .addResponseHeader("X-Echo-Response", "True"))
                        .uri(echoUrl))
                .build();
    }
}
