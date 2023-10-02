package net.polvott.etagtest

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ShallowEtagHeaderFilter

@Configuration
class ETagConfiguration {

    @Bean
    fun shallowEtagHeaderFilter(): FilterRegistrationBean<ShallowEtagHeaderFilter> {
        val filterRegBean = FilterRegistrationBean(ShallowEtagHeaderFilter())
        filterRegBean.addUrlPatterns("/values/*")
        filterRegBean.setName("eTagFilter")
        return filterRegBean
    }
}
