package br.com.zup.client.response

import io.micronaut.core.annotation.Introspected

@Introspected
data class InstituicaoErpResponse(
    val nome: String,
    val ispb: String
)