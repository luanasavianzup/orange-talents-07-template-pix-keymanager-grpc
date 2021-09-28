package br.com.zup.client.bcb.dto

import br.com.zup.model.Conta
import io.micronaut.core.annotation.Introspected

@Introspected
data class DeletePixKeyRequest (
    val key: String,
    val participant: String = Conta.ITAU_UNIBANCO_ISPB
)