package br.com.zup.edu.list

import br.com.zup.edu.ListKeyResponse
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class PixKeyResponse(pixKey: ListKeyResponse.PixKey) {

    val pixId: String = pixKey.pixId
    val key: String = pixKey.key
    val type = pixKey.type.name
    val accountType = pixKey.typeAccount.name
    val createdAt: LocalDateTime = pixKey.createdAt.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
}
