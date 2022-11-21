package com.example.cockroach.demo.payment.infrastracture

import com.example.cockroach.demo.configuration.JooqEngine
import com.example.cockroach.demo.payment.domain.*
import com.example.cockroach.demo.payment.domain.PaymentFacade
import com.example.cockroach.demo.payment.domain.PaymentCreator
import com.example.cockroach.demo.payment.infrastracture.persistence.JooqDataRepository
import com.example.cockroach.demo.payment.infrastracture.persistence.JooqPaymentRepository
import com.example.cockroach.demo.payment.infrastracture.persistence.PaymentRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class PaymentConfiguration {

    @Bean
    fun paymentRepository(jooqEngine: JooqEngine): PaymentRepository = JooqPaymentRepository(
        JooqDataRepository(jooqEngine),
        jooqEngine)

    @Bean
    fun paymentFacade(paymentRepository: PaymentRepository): PaymentFacade {
        val paymentCreator = PaymentCreator(paymentRepository)
        val paymentRetriever = PaymentRetriever(paymentRepository)

        return PaymentFacade(paymentRetriever, paymentCreator)
    }
}