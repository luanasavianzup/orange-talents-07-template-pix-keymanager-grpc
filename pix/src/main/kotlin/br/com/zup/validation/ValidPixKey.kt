package br.com.zup.validation

import kotlin.annotation.AnnotationTarget.*
import kotlin.annotation.AnnotationRetention.RUNTIME
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Target(CLASS, CONSTRUCTOR)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
annotation class ValidPixKey(
    val message: String = "Chave pix inválida",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)
