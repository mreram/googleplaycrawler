package com.eram.googleplaycrawler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import javax.jws.WebService
import javax.servlet.Servlet
import javax.servlet.annotation.WebServlet


@SpringBootApplication
@EnableConfigurationProperties(CryptProperties::class)
class GoogleplaycrawlerApplication

fun main(args: Array<String>) {
    runApplication<GoogleplaycrawlerApplication>(*args)
}

