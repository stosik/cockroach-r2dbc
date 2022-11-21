package com.example.cockroach.demo.payment.domain

import com.example.cockroach.demo.payment.adapter.controller.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

internal class PaymentFacade(
    private val paymentRetriever: PaymentRetriever,
    private val paymentCreator: PaymentCreator
) {
    suspend fun createPayment(request: CreatePaymentRequest): UUID {
        val payment = Payment(
            id = UUID.randomUUID(),
            amount = request.amount,
            data = Payment.Data(
                id = UUID.randomUUID(),
                information = request.data.information
            )
        )
        return paymentCreator(payment)
    }

    suspend fun retrievePayment(id: UUID): PaymentResponse {
        val payment = paymentRetriever.retrieveById(id) ?: throw ClientErrorException(
            "Could not retrieve payment with data"
        )
        return PaymentResponse.from(payment)
    }

    suspend fun retrievePayments(limit: Int?, offset: Int?): List<PaymentResponse> {
        return paymentRetriever
            .retrieveAll(limit, offset)
            .map { PaymentResponse.from(it) }
    }
}