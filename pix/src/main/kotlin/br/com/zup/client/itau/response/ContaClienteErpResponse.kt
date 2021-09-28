package br.com.zup.client.itau.response

import br.com.zup.TipoConta
import br.com.zup.model.Conta
import io.micronaut.core.annotation.Introspected
import java.lang.reflect.Array.get

@Introspected
data class ContaClienteErpResponse(
    val tipo: String,
    val instituicao: InstituicaoErpResponse,
    val agencia: String,
    val numero: String,
    val titular: TitularErpResponse
) {
    fun toModel(): Conta {
        return Conta(
            instituicao = instituicao.toModel(),
            agencia = agencia,
            numero = numero,
            titular = titular.toModel()
        )
    }
}