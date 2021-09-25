package br.com.zup.utils

import br.com.zup.PixRequest
import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.dto.NovaChaveRequest

fun PixRequest.toModel(): NovaChaveRequest {
    return NovaChaveRequest(
        clienteId = clienteId,
        tipoChave = when (tipoChave) {
            TipoChave.UNKNOWN_CHAVE -> null
            else -> TipoChave.valueOf(tipoChave.name)
        },
        chave = chave,
        tipoConta = when (tipoConta) {
            TipoConta.UNKNOWN_CONTA -> null
            else -> TipoConta.valueOf(tipoConta.name)
        }
    )
}
