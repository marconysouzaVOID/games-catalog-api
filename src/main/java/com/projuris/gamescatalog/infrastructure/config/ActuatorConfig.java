package com.projuris.gamescatalog.infrastructure.config;

import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração adicional para o Spring Boot Actuator
 */
@Configuration
public class ActuatorConfig {

    private final ApplicationProperties applicationProperties;

    public ActuatorConfig(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public InfoContributor applicationInfoContributor() {
        return builder -> builder
                .withDetail("name", applicationProperties.getName())
                .withDetail("version", applicationProperties.getVersion())
                .withDetail("description", "API REST para gerenciamento de catálogo de games");
    }
}
