package br.com.zup.model

import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Embeddable
class Conta(
    @field:NotNull @Embedded val instituicao: Instituicao,
    @field:NotBlank val agencia: String,
    @field:NotBlank val numero: String,
    @field:NotBlank @Embedded val titular: Titular
) {
    companion object {
        public val ITAU_UNIBANCO_ISPB: String = "60701190"
    }
}