package com.example.cockroach.demo.payment.domain

import com.example.cockroach.demo.payment.infrastracture.persistence.PaymentRepository
import java.util.UUID

internal class PaymentCreator(private val paymentRepository: PaymentRepository) {

    suspend operator fun invoke(payment: Payment): UUID {
        return paymentRepository.insertPayment(payment)
    }
}