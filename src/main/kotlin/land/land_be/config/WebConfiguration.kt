package land.land_be.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:3001",
                "http://localhost:3000"
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .exposedHeaders("X-AUTH-TOKEN")
            .allowCredentials(true)
    }
}