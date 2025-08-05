package com.senai.oficina_teste_swagger.infrastructure.config;


import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.*;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI oficinaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Oficina Mecânica")
                        .description("Cadastro e gestão de serviços de uma oficina.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Equipe Oficina")
                                .email("suporte@oficina.com"))
                );
    }
}
