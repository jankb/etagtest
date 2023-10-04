package net.polvott.etagtest

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
@ImportAutoConfiguration(ExposedAutoConfiguration::class)
class EtagtestApplication

fun main(args: Array<String>) {
	runApplication<EtagtestApplication>(*args)
}
