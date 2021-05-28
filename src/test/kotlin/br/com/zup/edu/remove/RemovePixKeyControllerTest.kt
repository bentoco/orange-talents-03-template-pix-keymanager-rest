package br.com.zup.edu.remove

import br.com.zup.edu.KeyRemoveServiceGrpc
import br.com.zup.edu.RemoveKeyResponse
import br.com.zup.edu.grpc.KeyManagerGrpcFactory
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
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemovePixKeyControllerTest {

    @field:Inject
    lateinit var removeStub: KeyRemoveServiceGrpc.KeyRemoveServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `must remove key`() {
        val customerId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val grpcResponse = RemoveKeyResponse.newBuilder()
            .setMessage("resource deleted successfully")
            .setPixId(pixId)
            .setUserId(customerId)
            .build()

        given(removeStub.removeKey(Mockito.any())).willReturn(grpcResponse)

        val deleteKeyRequest = DeleteKeyRequest(pixId)

        val request = HttpRequest.DELETE("/api/v1/customers/$customerId/pix", deleteKeyRequest)
        val response = client.toBlocking().exchange(request, DeleteKeyRequest::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() = Mockito.mock(KeyRemoveServiceGrpc.KeyRemoveServiceBlockingStub::class.java)
    }
}