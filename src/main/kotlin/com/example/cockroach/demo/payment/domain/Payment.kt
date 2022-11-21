package com.example.cockroach.demo.payment.domain

import java.util.*

data class Payment(
    val id: UUID,
    val data: Data,
    val amount: Long
) {

    data class Data(
        val id: UUID,
        val information: String
    )
}
