package br.com.zup.client.bcb.dto

import br.com.zup.client.bcb.TipoChaveBanco
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime

@Introspected
data class CreatePixKeyResponse(
    val keyType: TipoChaveBanco,
    val key: String,
    val bankAccount: BankAccountRequest,
    val owner: OwnerRequest,
    val createdAt: LocalDateTime
)