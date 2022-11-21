package com.example.cockroach.demo.payment.adapter.controller

import com.example.cockroach.demo.payment.domain.Payment
import java.util.*

data class PaymentResponse(
    val id: UUID,
    val data: DataResponse,
    val amount: Long
) {

    data class DataResponse(
        val id: UUID,
        val information: String
    )

    companion object {
       fun from(payment: Payment): PaymentResponse {
            return PaymentResponse(
                id = payment.id,
                amount = payment.amount,
                data =  DataResponse(
                    id = payment.data.id,
                    information = payment.data.information
                )
            )
        }
    }
}