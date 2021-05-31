package br.com.zup.edu.consult

import br.com.zup.edu.ConsultKeyResponse
import br.com.zup.edu.TypeAccount
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class DetailsPixKeyResponse(response: ConsultKeyResponse) {
    val pixId = response.pixId
    val userId = response.userId
    val type = response.key.type
    val key = response.key.key
    val accountType = when (response.key.account.type) {
        TypeAccount.CONTA_CORRENTE -> "CONTA_CORRENTE"
        TypeAccount.CONTA_POUPANCA -> "CONTA_POUPANCA"
        else -> "DESCONHECIDA"
    }
    val account = mapOf(
        Pair("type", accountType),
        Pair("institution", response.key.account.institution),
        Pair("ownerName", response.key.account.ownerName),
        Pair("ownerCpf", response.key.account.ownerCpf),
        Pair("branch", response.key.account.branch),
        Pair("accountNumber", response.key.account.accountNumber)
    )
    val createdAt = response.key.createdAt.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
}
