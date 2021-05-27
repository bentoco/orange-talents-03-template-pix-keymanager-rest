package br.com.zup.edu.register

import br.com.zup.edu.RegisterKeyRequest
import br.com.zup.edu.TypeAccount
import br.com.zup.edu.TypeKey
import br.com.zup.edu.validators.ValidPixKey
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
@Introspected
class NewPixKeyRequest(
    @field:NotNull val typeKey: TypeKeyRequest?,
    @field:Size(max = 77) val keyValue: String?,
    @field:NotNull val typeAccount: TypeAccountRequest?
) {
    fun toGrpcModel(customerId: String): RegisterKeyRequest {
        return RegisterKeyRequest.newBuilder()
            .setUserId(customerId)
            .setTypeKey(typeKey?.attributeGrpc ?: TypeKey.UNKNOWN_TYPE_KEY)
            .setTypeAccount(typeAccount?.attributeGrpc ?: TypeAccount.UNKNOWN_TYPE_ACCOUNT)
            .setKeyValue(keyValue ?: "")
            .build()
    }
}

enum class TypeKeyRequest(val attributeGrpc: TypeKey) {
    CPF(TypeKey.CPF) {
        override fun validate(key: String?): Boolean {
            if (key.isNullOrBlank()) {
                return false
            }
            return key.matches("^[0-9]{11}\$".toRegex())
        }
    },
    EMAIL(TypeKey.EMAIL) {

        override fun validate(key: String?): Boolean {
            if (key.isNullOrBlank()) {
                return false
            }
            return key.matches("^[A-Za-z0-9+_.-]+@(.+)$".toRegex())
        }
    },
    PHONE(TypeKey.PHONE) {

        override fun validate(key: String?): Boolean {
            if (key.isNullOrBlank()) {
                return false
            }
            return key.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },
    RANDOM(TypeKey.RANDOM) {
        override fun validate(key: String?) = key.isNullOrBlank()
    };

    abstract fun validate(key: String?): Boolean
}

enum class TypeAccountRequest(val attributeGrpc: TypeAccount) {
    CONTA_CORRENTE(TypeAccount.CONTA_CORRENTE),
    CONTA_POUPANCA(TypeAccount.CONTA_POUPANCA)
}