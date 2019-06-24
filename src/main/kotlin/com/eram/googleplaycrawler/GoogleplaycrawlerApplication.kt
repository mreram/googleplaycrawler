package com.eram.googleplaycrawler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(CryptProperties::class)
class GoogleplaycrawlerApplication

fun main(args: Array<String>) {
    runApplication<GoogleplaycrawlerApplication>(*args)
}
