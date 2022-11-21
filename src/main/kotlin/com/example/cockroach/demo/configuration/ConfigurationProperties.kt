package com.example.cockroach.demo.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class ConfigurationProperties {

    @Bean
    fun database(
        @Value("\${database.url}") url: String,
        @Value("\${database.username}") username: String
    ) = Database(
        url,
        username,
    )

    data class Database(
        val url: String,
        val username: String
    )
}