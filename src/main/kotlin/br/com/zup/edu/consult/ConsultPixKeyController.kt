package br.com.zup.edu.consult

import br.com.zup.edu.ConsultKeyRequest
import br.com.zup.edu.KeyConsultServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory

@Controller("/api/v1/customers/{customerId}")
class ConsultPixKeyController(
    val consultStub: KeyConsultServiceGrpc.KeyConsultServiceBlockingStub
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Get("/pix/{pixId}")
    fun consult(customerId: String, pixId: String): HttpResponse<Any> {

        LOGGER.info("[$customerId] consulting new pix key id: [$pixId]...")

        val grpcResponse = consultStub.consultKey(
            ConsultKeyRequest.newBuilder()
                .setPixId(
                    ConsultKeyRequest.PixIdFilter.newBuilder()
                        .setPixId(pixId)
                        .setUserId(customerId)
                        .build()
                )
                .build()
        )
        return HttpResponse.ok(DetailsPixKeyResponse(grpcResponse))
    }
}