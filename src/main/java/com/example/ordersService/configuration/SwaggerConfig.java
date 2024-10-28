package com.example.ordersService.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Order service API",
                description = "Number service", version = "1.0.0",
                contact = @Contact(
                        name = "Renat Mingazov",
                        email = "mingazofff@gmail.com"
                )
        )
)
public class SwaggerConfig {

}
