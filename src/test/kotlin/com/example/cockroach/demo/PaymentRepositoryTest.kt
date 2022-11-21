package com.example.cockroach.demo

import com.example.cockroach.demo.payment.domain.Payment
import com.example.cockroach.demo.payment.infrastracture.persistence.PaymentRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

internal class PaymentRepositoryTest : TestContainersInitializer() {

    @Autowired
    private lateinit var paymentRepository: PaymentRepository

    @Test
    fun `should add payment`(): Unit = runBlocking {
        val payment = aPayment()

        paymentRepository.insertPayment(payment)
    }

    private fun aPayment(): Payment {
        return Payment(
            id = UUID.randomUUID(),
            amount = 10L,
            data = Payment.Data(
                id = UUID.randomUUID(),
                information = "Data"
            ))
    }
}