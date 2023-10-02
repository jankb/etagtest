package net.polvott.etagtest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EtagtestApplication

fun main(args: Array<String>) {
	runApplication<EtagtestApplication>(*args)
}
