package br.com.zup.edu.remove

import br.com.zup.edu.KeyRemoveServiceGrpc
import br.com.zup.edu.RemoveKeyResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.validation.Valid

@Validated
@Controller("/api/v1/customers/{customerId}")
class RemovePixKeyController(
    private val removePixKeyClient: KeyRemoveServiceGrpc.KeyRemoveServiceBlockingStub
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Delete("/pix")
    fun remove(customerId: String, @Valid @Body request: DeleteKeyRequest): HttpResponse<Any> {

        LOGGER.info("[$customerId] removing pix key with $request")

        val response = removePixKeyClient.removeKey(request.toGrpcRequest(customerId))

        return HttpResponse.ok(response.toResponse())
    }
}

private fun RemoveKeyResponse.toResponse(): DeleteKeyResponse {
    return DeleteKeyResponse(
        message = this.message,
        pixId = this.pixId,
        customerId = this.userId
    )
}


