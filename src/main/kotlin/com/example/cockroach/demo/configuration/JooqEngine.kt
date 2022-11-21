package com.example.cockroach.demo.configuration

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD
import io.r2dbc.spi.ConnectionFactoryOptions.USER
import kotlinx.coroutines.reactive.awaitLast
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.withContext
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.impl.DSL
import reactor.core.publisher.Flux
import reactor.core.publisher.Flux.from
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

internal class JooqEngine(
    databaseConfig: ConfigurationProperties.Database
) {
    private data class JooqConfigurationElement(
        val value: Configuration,
    ) : AbstractCoroutineContextElement(JooqConfigurationElement) {
        companion object Key : CoroutineContext.Key<JooqConfigurationElement>
    }

    private val dsl = DSL.using(
        ConnectionFactories.get(
            ConnectionFactoryOptions
                .parse(databaseConfig.url)
                .mutate()
                .option(USER, databaseConfig.username)
                .build()
        )
    )

    private suspend fun configuration(): Configuration {
        val jooqElement = coroutineContext[JooqConfigurationElement]
        return jooqElement?.value ?: dsl.configuration()
    }

    suspend fun <R> query(block: suspend DSLContext.() -> R): R {
        return block(DSL.using(configuration()))
    }

    suspend fun <R> transaction(block: suspend DSLContext.() -> R): R =
        DSL.using(configuration()).transactionPublisher { t ->
            from(
                mono {
                    withContext(JooqConfigurationElement(t)) {
                        block(DSL.using(t))
                    }
                }
            )
        }.awaitLast()
}