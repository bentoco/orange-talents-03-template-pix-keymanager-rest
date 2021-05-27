package br.com.zup.edu.validators

import br.com.zup.edu.register.NewPixKeyRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
annotation class ValidPixKey(
    val message: String = "invalid pix key ( \${validatedValue.typeKey}",
    val groups: Array<KClass<Any>> =[],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class ValidPixKeyValidator: ConstraintValidator<ValidPixKey, NewPixKeyRequest> {
    override fun isValid(
        value: NewPixKeyRequest?,
        annotationMetadata: AnnotationValue<ValidPixKey>,
        context: ConstraintValidatorContext
    ): Boolean {
        if(value?.typeKey == null){
            return false
        }
        return value.typeKey.validate(value.keyValue)
    }

}
