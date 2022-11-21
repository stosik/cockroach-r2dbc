package com.example.cockroach.demo.payment.infrastracture.persistence

import com.example.cockroach.demo.payment.domain.Payment
import java.util.*

interface PaymentRepository {

    suspend fun insertPayment(payment: Payment): UUID
    suspend fun findById(uuid: UUID): Payment?
    suspend fun findAll(limit: Int?, offset: Int?): List<Payment>
}