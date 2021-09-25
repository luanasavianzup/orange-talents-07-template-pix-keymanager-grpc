package br.com.zup.client.response

import io.micronaut.core.annotation.Introspected

@Introspected
data class TitularErpResponse(
    val id: String,
    val nome: String,
    val cpf: String
)
