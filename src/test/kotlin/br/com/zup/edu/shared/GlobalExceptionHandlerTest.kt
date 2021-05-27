package br.com.zup.edu.shared

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GlobalExceptionHandlerTest {

    val requestGeneric = HttpRequest.GET<Any>("/")

    @Test
    internal fun `must return status 404 when not found statusException`() {

        val message = "not found"
        val notFoundException = StatusRuntimeException(Status.NOT_FOUND.withDescription(message))

        val response = GlobalExceptionHandler().handle(requestGeneric, notFoundException)

        assertEquals(HttpStatus.NOT_FOUND, response.status)
        assertNotNull(response.body())
        assertEquals(message, (response.body() as JsonError).message)
    }

    @Test
    internal fun `must return status 422 when already exists statusException`() {

        val message = "already exists"
        val notFoundException = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(message))

        val response = GlobalExceptionHandler().handle(requestGeneric, notFoundException)

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.status)
        assertNotNull(response.body())
        assertEquals(message, (response.body() as JsonError).message)
    }

    @Test
    internal fun `must return status 400 when invalid argument statusException`() {

        val message = "invalid input data"
        val notFoundException = StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(message))

        val response = GlobalExceptionHandler().handle(requestGeneric, notFoundException)

        assertEquals(HttpStatus.BAD_REQUEST, response.status)
        assertNotNull(response.body())
        assertEquals(message, (response.body() as JsonError).message)
    }

    @Test
    internal fun `must return status 500 when unknown error`() {

        val message = "unexpected"
        val notFoundException = StatusRuntimeException(Status.UNKNOWN.withDescription(message))

        val response = GlobalExceptionHandler().handle(requestGeneric, notFoundException)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.status)
        assertNotNull(response.body())
    }
}