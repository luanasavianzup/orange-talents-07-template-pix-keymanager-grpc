package br.com.zup.model

import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank

@Embeddable
class Instituicao (
    @field:NotBlank
    val nomeInstituicao: String,

    @field:NotBlank
    val ispb: String
)