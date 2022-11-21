package com.example.cockroach.demo.payment.adapter

import com.example.cockroach.demo.payment.adapter.controller.ClientErrorException
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ClientErrorException::class)
    fun clientException(e: ClientErrorException) = ResponseEntity(e, INTERNAL_SERVER_ERROR)
}