package com.softwareplace.fileserver.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class RedirectWebMvConfigurer() : WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/").setViewName("redirect:swagger-ui/index.html")
        registry.addViewController("/docs").setViewName("redirect:swagger-ui/index.html")
        registry.addViewController("/swagger").setViewName("redirect:swagger-ui/index.html")
    }
}
