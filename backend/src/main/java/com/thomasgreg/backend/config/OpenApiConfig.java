package com.thomasgreg.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.parser.OpenAPIV3Parser;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() throws IOException {

        InputStream inputStream = new ClassPathResource("static/openapi-editado.json").getInputStream();
        String openApiContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        
        return new OpenAPIV3Parser().readContents(openApiContent).getOpenAPI()
                .info(new Info()
                        .title("API Gerenciamento de Clientes - Thomas Greg")
                        .version("0.0.1")
                        .description("Documentação inicial da API"));
    }

}
