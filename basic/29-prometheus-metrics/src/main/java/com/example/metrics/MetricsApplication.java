package com.example.metrics;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MetricsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetricsApplication.class, args);
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    PrometheusScrapeEndpoint prometheusEndpoint(CollectorRegistry collectorRegistry) {
        return new PrometheusScrapeEndpoint(collectorRegistry);
    }

    @Bean
    PrometheusMeterRegistry prometheusMeterRegistry(PrometheusConfig prometheusConfig, CollectorRegistry collectorRegistry, Clock clock) {
        return new PrometheusMeterRegistry(prometheusConfig, collectorRegistry, clock);
    }

    @Bean
    PrometheusConfig prometheusConfig() {
        return PrometheusConfig.DEFAULT;
    }

    @Bean
    CollectorRegistry collectorRegistry() {
        return new CollectorRegistry(true);
    }

    @Bean
    Clock clock() {
        return Clock.SYSTEM;
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "prometheus-metrics");
    }

}
