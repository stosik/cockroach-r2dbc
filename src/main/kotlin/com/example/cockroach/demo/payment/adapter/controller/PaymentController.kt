package com.example.cockroach.demo.payment.adapter.controller

import com.example.cockroach.demo.payment.domain.PaymentFacade
import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/payments")
internal class PaymentController(private val paymentFacade: PaymentFacade) {

    @GetMapping
    suspend fun findPayments(@RequestParam limit: Int?, @RequestParam offset: Int?): List<PaymentResponse> {
        return paymentFacade.retrievePayments(limit, offset)
    }

    @GetMapping("/{id}")
    suspend fun findPaymentById(@PathVariable id: UUID): PaymentResponse {
        return paymentFacade.retrievePayment(id)
    }

    @PostMapping
    suspend fun createPayment(@RequestBody createPaymentRequest: CreatePaymentRequest): UUID {
        return paymentFacade.createPayment(createPaymentRequest)
    }
}