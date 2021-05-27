package br.com.zup.edu.register

import br.com.zup.edu.KeyManagerServiceGrpc
import br.com.zup.edu.RegisterKeyResponse
import br.com.zup.edu.grpc.KeyManagerGrpcFactory
import br.com.zup.edu.register.TypeAccountRequest.CONTA_CORRENTE
import br.com.zup.edu.register.TypeKeyRequest.*
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RegisterPixKeyControllerTest {

    /**
     * Simulates remote communication of api rest with external gRPC service
     */
    @field:Inject
    lateinit var registerStub: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `must register a new pix key`() {

        val customerId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val grpcResponse = RegisterKeyResponse.newBuilder()
            .setUserId(customerId)
            .setPixId(pixId)
            .build()

        given(registerStub.registerKey(Mockito.any())).willReturn(grpcResponse)

        val newPixKey =
            NewPixKeyRequest(typeKey = EMAIL, keyValue = "foo@mail.com", typeAccount = CONTA_CORRENTE)

        val request = HttpRequest.POST("/api/v1/customers/$customerId/pix", newPixKey)
        val response = client.toBlocking().exchange(request, NewPixKeyRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.contains(pixId))

    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() = Mockito.mock(KeyManagerServiceGrpc.KeyManagerServiceBlockingStub::class.java)
    }

}