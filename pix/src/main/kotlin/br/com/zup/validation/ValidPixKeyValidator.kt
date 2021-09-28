package br.com.zup.validation

import br.com.zup.TipoChave
import br.com.zup.dto.NovaChaveDto
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton

@Singleton
open class ValidPixKeyValidator : ConstraintValidator<ValidPixKey, NovaChaveDto> {
    override fun isValid(
        value: NovaChaveDto?,
        annotationMetadata: AnnotationValue<ValidPixKey>,
        context: ConstraintValidatorContext
    ): Boolean {
        if(value?.chave == null) {
            return true
        }
        when(value?.tipoChave){
            TipoChave.CPF -> return value.chave.matches("^[0-9]{11}\$".toRegex())
            TipoChave.EMAIL -> return value.chave.matches("[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?".toRegex())
            TipoChave.CELULAR -> return value.chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
            TipoChave.ALEATORIA -> return true
        }
        return true
    }
}
