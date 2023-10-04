package net.polvott.etagtest

import org.springframework.beans.factory.annotation.Autowired
import java.time.Duration
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.filter.ShallowEtagHeaderFilter


@Configuration
class ETagConfiguration(private val corsConfig: CorsConfig) {

    @Bean
    fun shallowEtagHeaderFilter(): FilterRegistrationBean<ShallowEtagHeaderFilter> {
        val filterRegBean = FilterRegistrationBean(ShallowEtagHeaderFilter())
        filterRegBean.addUrlPatterns("/values/*", "/country/*")
        filterRegBean.setName("eTagFilter")
        return filterRegBean
    }

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowedOrigins = corsConfig.allowedOrigins
        corsConfiguration.allowedHeaders = listOf("*")
        corsConfiguration.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        corsConfiguration.allowCredentials = true
        corsConfiguration.setMaxAge(Duration.ofMinutes(30))
        source.registerCorsConfiguration("/**", corsConfiguration)
        return CorsFilter(source)
    }
}

