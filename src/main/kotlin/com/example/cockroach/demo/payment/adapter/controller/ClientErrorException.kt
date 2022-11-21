package com.example.cockroach.demo.payment.adapter.controller

data class ClientErrorException(
    override val message: String?,
) : Exception()