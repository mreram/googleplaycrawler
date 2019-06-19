package com.eram.googleplaycrawler.data

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.env.Environment

@Configuration
@PropertySource("crypt.properties")
class KeyConfiguration {

    @Autowired
    private val env: Environment? = null

    fun getProperty(pPropertyKey: String): String? {
        return env!!.getProperty(pPropertyKey)
    }
}
