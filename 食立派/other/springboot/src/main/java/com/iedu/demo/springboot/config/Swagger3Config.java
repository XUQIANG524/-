package com.iedu.demo.springboot.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class Swagger3Config {
    @Bean
    public OpenAPI springOpenApi() {
        return new OpenAPI()
                .info(
                        new Info().title("互联网实训")
                                .contact(new Contact())
                                .description("22级互联网实训")
                                .version("1.0")
                                .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("外部文档"));
    }
}
