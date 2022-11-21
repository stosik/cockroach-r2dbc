package com.example.cockroach.demo.payment.adapter.controller

data class CreatePaymentRequest(
    val amount: Long,
    val data: DataRequest
) {
    data class DataRequest(val information: String)
}
