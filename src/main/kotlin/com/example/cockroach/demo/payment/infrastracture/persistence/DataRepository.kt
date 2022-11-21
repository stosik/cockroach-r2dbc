package com.example.cockroach.demo.payment.infrastracture.persistence

import com.example.cockroach.demo.payment.domain.Payment

interface DataRepository {
    suspend fun insert(data: Payment.Data): Int
}