package com.example.cockroach.demo

import com.example.cockroach.demo.TestContainersInitializer.DataSourceInitializer
import org.junit.runner.RunWith
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.TestPropertySourceUtils
import org.testcontainers.containers.CockroachContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@RunWith(SpringRunner::class)
@EnableAutoConfiguration(exclude = [R2dbcAutoConfiguration::class])
@Testcontainers
@ContextConfiguration(initializers = [DataSourceInitializer::class])
internal class TestContainersInitializer {

    companion object {

        @Container
        private val cocroachContainer: CockroachContainer = CockroachContainer("cockroachdb/cockroach:v22.1.11")
    }

    internal class DataSourceInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.test.database.replace=none",
                "database.url=${cocroachContainer.r2dbcUrl()}",
                "database.username=${cocroachContainer.username}",
                "database.password=${cocroachContainer.password}",

                "spring.liquibase.url=${cocroachContainer.jdbcUrl()}",
                "spring.liquibase.user=${cocroachContainer.username}",
                "spring.liquibase.password=${cocroachContainer.password}",
                "spring.liquibase.change-log=classpath:/db/changelog.sql"
            )
        }

        private fun CockroachContainer.jdbcUrl() = "jdbc:postgresql://$host:${firstMappedPort}/$databaseName"

        private fun CockroachContainer.r2dbcUrl() =
            "r2dbc:pool:postgresql://$host:$firstMappedPort/$databaseName?autoReconnect=true&sslEnabled=false"
    }
}