package br.com.zup.edu.consult

import br.com.zup.edu.ConsultKeyResponse
import br.com.zup.edu.KeyConsultServiceGrpc
import br.com.zup.edu.TypeAccount
import br.com.zup.edu.TypeKey
import br.com.zup.edu.grpc.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ConsultPixKeyControllerTest {

    @field:Inject
    lateinit var consultStub: KeyConsultServiceGrpc.KeyConsultServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `must consult a pix key`() {
        val customerId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        given(consultStub.consultKey(Mockito.any())).willReturn(consultKeyResponse(customerId, pixId))

        val request = HttpRequest.GET<Any>("/api/v1/customers/$customerId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
    }

    val TYPE_EMAIL_KEY = TypeKey.EMAIL
    val EMAIL_KEY = "foo@mail.com"
    val CONTA_CORRENTE = TypeAccount.CONTA_CORRENTE
    val INSTITUTION = "Itau"
    val OWNER_NAME = "Foo"
    val OWNER_CPF = "34597563067"
    val BRANCH = "0001"
    val ACCOUNT_NUMBER = "1010-1"
    val CREATED_AT = LocalDateTime.now()

    private fun consultKeyResponse(customerId: String, pixId: String) =
        ConsultKeyResponse.newBuilder()
            .setUserId(customerId)
            .setPixId(pixId)
            .setKey(
                ConsultKeyResponse.PixKey
                    .newBuilder()
                    .setType(TYPE_EMAIL_KEY)
                    .setKey(EMAIL_KEY)
                    .setAccount(
                        ConsultKeyResponse.PixKey.AccountInfo.newBuilder()
                            .setType(CONTA_CORRENTE)
                            .setInstitution(INSTITUTION)
                            .setOwnerName(OWNER_NAME)
                            .setOwnerCpf(OWNER_CPF)
                            .setBranch(BRANCH)
                            .setAccountNumber(ACCOUNT_NUMBER)
                            .build()
                    )
                    .setCreatedAt(CREATED_AT.let {
                        val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                        Timestamp.newBuilder()
                            .setSeconds(createdAt.epochSecond)
                            .setNanos(createdAt.nano)
                            .build()
                    })
            )
            .build()


    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() = Mockito.mock(KeyConsultServiceGrpc.KeyConsultServiceBlockingStub::class.java)
    }
}