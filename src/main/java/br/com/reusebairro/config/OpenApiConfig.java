package br.com.reusebairro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI reUseBairroOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("ReUseBairro API")
                .version("1.0.0")
                .description("Backend do ReUseBairro (CitizenTech): anuncie móveis e eletrônicos que seriam descartados "
                        + "na rua e permita que outra pessoa faça a coleta."));
    }
}
