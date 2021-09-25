package br.com.zup.client.response

import io.micronaut.core.annotation.Introspected

@Introspected
data class ContaClienteErpResponse(
    val tipo: String,
    val instituicao: InstituicaoErpResponse,
    val agencia: String,
    val numero: String,
    val titular: TitularErpResponse
)