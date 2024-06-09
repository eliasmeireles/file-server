import com.github.softwareplace.springboot.buildconfiguration.applyGraalvm
import com.github.softwareplace.springboot.kotlin.kotlinReactive
import com.github.softwareplace.springboot.kotlin.openapi.kotlinOpenApiSettings
import com.github.softwareplace.springboot.kotlin.testKotlinMockito
import com.github.softwareplace.springboot.utils.jsonLogger
import com.github.softwareplace.springboot.utils.logstashLogbackEncoderVersion
import com.github.softwareplace.springboot.utils.springBootSecurityUtil
import com.github.softwareplace.springboot.utils.springBootStartWeb

plugins {
    id("com.github.softwareplace.springboot.kotlin")
}

group = "com.softwareplace.fileserver"
version = "1.0.0"

kotlinOpenApiSettings()

applyGraalvm()

dependencies {
    logstashLogbackEncoderVersion()
    springBootSecurityUtil()
    springBootStartWeb()
    kotlinReactive()
//    TODO: Fixe bug with jetty api
//    springJettyApi()
    jsonLogger()

    testKotlinMockito()
}

