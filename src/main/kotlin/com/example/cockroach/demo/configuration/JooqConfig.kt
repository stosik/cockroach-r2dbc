package com.example.cockroach.demo.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class JooqConfig {
    
    @Bean
    fun getJooqEngine(config: ConfigurationProperties.Database) = JooqEngine(databaseConfig = config)
}