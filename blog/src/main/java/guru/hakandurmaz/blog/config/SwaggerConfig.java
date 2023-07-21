package guru.hakandurmaz.blog.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer")
@OpenAPIDefinition(
    info =
        @Info(
            title = "User Management API",
            version = "${api.version}",
            contact = @Contact(name = "Hakan Durmaz", email = "durmazhakan@iclod.com"),
            description = "${api.description}"))
public class SwaggerConfig {}
