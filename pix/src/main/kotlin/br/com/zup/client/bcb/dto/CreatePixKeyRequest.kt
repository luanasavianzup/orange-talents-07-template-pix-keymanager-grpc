package br.com.zup.client.bcb.dto

import io.micronaut.core.annotation.Introspected

@Introspected
data class CreatePixKeyRequest(
    val keyType: String,
    val key: String,
    val bankAccount: BankAccountRequest,
    val owner: OwnerRequest
)
