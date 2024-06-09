package com.softwareplace.fileserver

import com.softwareplace.fileserver.properties.AppProperties
import com.softwareplace.springsecurity.SpringSecurityInit
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.softwareplace.fileserver"])
@EnableConfigurationProperties(value = [AppProperties::class])
@ImportAutoConfiguration(classes = [SpringSecurityInit::class])
class FileServerApplication

fun main(args: Array<String>) {
    runApplication<FileServerApplication>(*args)
}
