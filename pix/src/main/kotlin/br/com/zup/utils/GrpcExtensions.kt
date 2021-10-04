package br.com.zup.utils

import br.com.zup.*
import br.com.zup.dto.ConsultaChaveDto
import br.com.zup.dto.NovaChaveDto
import br.com.zup.dto.RemoveChaveDto

fun PixRequest.toModel(): NovaChaveDto {
    return NovaChaveDto(
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

fun RemoveChaveRequest.toModel(): RemoveChaveDto {
    return RemoveChaveDto(
        clienteId = clienteId,
        pixId = pixId
    )
}

fun ConsultaChaveRequest.toModel(): ConsultaChaveDto {
    return ConsultaChaveDto(
        clienteId = pixId.clienteId,
        pixId = pixId.pixId,
        chave = chave
    )
}
