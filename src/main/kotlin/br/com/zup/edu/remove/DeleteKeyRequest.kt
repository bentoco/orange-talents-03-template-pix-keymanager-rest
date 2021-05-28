package br.com.zup.edu.remove

import br.com.zup.edu.RemoveKeyRequest
import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class DeleteKeyRequest(
    @field:NotBlank @field:JsonProperty val pixId: String?,
) {
    fun toGrpcRequest(customerId: String): RemoveKeyRequest {
        return RemoveKeyRequest.newBuilder()
            .setPixId(pixId ?: "")
            .setUserId(customerId)
            .build()
    }
}
