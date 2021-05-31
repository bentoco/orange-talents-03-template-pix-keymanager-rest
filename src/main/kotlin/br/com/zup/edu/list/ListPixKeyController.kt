package br.com.zup.edu.list

import br.com.zup.edu.KeyListServiceGrpc
import br.com.zup.edu.ListKeyRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/api/v1/customers/{customerId}")
class ListPixKeyController(private val listStub: KeyListServiceGrpc.KeyListServiceBlockingStub) {

    @Get("/pix/")
    fun list(customerId: String): HttpResponse<Any> {
        val response = listStub.listKey(ListKeyRequest.newBuilder().setUserId(customerId).build())
        val keys = response.keysList.map { PixKeyResponse(it) }
        return HttpResponse.ok(keys)
    }
}