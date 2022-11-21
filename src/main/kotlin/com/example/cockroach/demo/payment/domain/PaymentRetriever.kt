package com.example.cockroach.demo.payment.domain

import com.example.cockroach.demo.payment.infrastracture.persistence.PaymentRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

internal class PaymentRetriever(private val paymentRepository: PaymentRepository) {

    suspend fun retrieveById(id: UUID): Payment? {
        return paymentRepository.findById(id)
    }

    suspend fun retrieveAll(limit: Int?, offset: Int?): List<Payment> {
        return paymentRepository.findAll(limit, offset)
    }
}