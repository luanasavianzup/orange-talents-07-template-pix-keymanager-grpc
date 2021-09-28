package br.com.zup.client.itau.response

import br.com.zup.model.Instituicao
import io.micronaut.core.annotation.Introspected

@Introspected
data class InstituicaoErpResponse(
    val nome: String,
    val ispb: String
) {
    fun toModel(): Instituicao {
        return Instituicao(
            nomeInstituicao = nome,
            ispb = ispb
        )
    }
}