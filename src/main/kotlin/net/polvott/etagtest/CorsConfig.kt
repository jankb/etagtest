package net.polvott.etagtest

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "corsconfig")
data class CorsConfig(
    val allowedOrigins: List<String>
)
