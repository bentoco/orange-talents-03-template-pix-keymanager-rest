package br.com.zup.edu.register

import br.com.zup.edu.KeyManagerServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.validation.Valid

@Validated
@Controller("/api/v1/customers/{customerId}")
class RegisterPixKeyController(
    private val registerPixKeyClient: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub
) {
    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Post("/pix")
    fun create(customerId: String, @Valid @Body request: NewPixKeyRequest): HttpResponse<Any> {

        LOGGER.info("[$customerId] creating new pix key with $request")

        val registerKeyResponse = registerPixKeyClient.registerKey(request.toGrpcModel(customerId))

        return HttpResponse.created(location(customerId, registerKeyResponse.pixId))

    }

    private fun location(customerId: String, pixId: String?) =
        HttpResponse.uri("/api/v1/customers/$customerId/pix/${pixId}")

}
