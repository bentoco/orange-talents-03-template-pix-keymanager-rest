package br.com.zup.edu.list

import br.com.zup.edu.*
import br.com.zup.edu.grpc.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ListPixKeyControllerTest {

    @field:Inject
    lateinit var listStub: KeyListServiceGrpc.KeyListServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `must list all keys`() {
        val customerId = UUID.randomUUID().toString()
        val grpcResponse = listPixKeyResponse(customerId)

        given(listStub.listKey(Mockito.any())).willReturn(grpcResponse)

        val request = HttpRequest.GET<Any>("/api/v1/customers/$customerId/pix/")
        val response = client.toBlocking().exchange(request, List::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(response.body()!!.size, 2)
    }

    val TYPE_EMAIL_KEY = TypeKey.EMAIL
    val EMAIL_KEY = "foo@mail.com"
    val CONTA_CORRENTE = TypeAccount.CONTA_CORRENTE
    val CREATED_AT = LocalDateTime.now()

    private fun listPixKeyResponse(customerId: String): ListKeyResponse {
        val email = ListKeyResponse.PixKey.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setType(TYPE_EMAIL_KEY)
            .setKey(EMAIL_KEY)
            .setTypeAccount(CONTA_CORRENTE)
            .setCreatedAt(CREATED_AT.let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        val phone = ListKeyResponse.PixKey.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setType(TypeKey.PHONE)
            .setKey("+55912351123")
            .setTypeAccount(CONTA_CORRENTE)
            .setCreatedAt(CREATED_AT.let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        return ListKeyResponse.newBuilder()
            .setUserId(customerId)
            .addAllKeys(listOf(email, phone))
            .build()
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() = Mockito.mock(KeyListServiceGrpc.KeyListServiceBlockingStub::class.java)
    }
}