package com.alextomala.searchDemo.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.RuntimeException

@ControllerAdvice
class ControllerAdvice : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [(IllegalArgumentException::class)])
    fun handleIllegalArgumentException(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [(EntityNotFoundException::class)])
    fun handleIllegalArgumentException(ex: EntityNotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message), HttpStatus.NOT_FOUND)
    }
}

data class ErrorResponse(val message: String?)

open class EntityNotFoundException(msg: String = "") : RuntimeException(msg)