package land.land_be

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class LandBeApplication

fun main(args: Array<String>) {
    runApplication<LandBeApplication>(*args)
}
