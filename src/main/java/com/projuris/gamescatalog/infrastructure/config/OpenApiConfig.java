package com.projuris.gamescatalog.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do OpenAPI/Swagger para documentação da API
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Games Catalog API")
                        .version("1.0.0")
                        .description(
                                "API REST para gerenciamento de catálogo de games. Desenvolvida seguindo Arquitetura Hexagonal e Programação Orientada a Eventos."));
    }
}
