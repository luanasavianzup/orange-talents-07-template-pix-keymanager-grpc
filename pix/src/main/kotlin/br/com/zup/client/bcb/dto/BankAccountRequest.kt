package br.com.zup.client.bcb.dto

import io.micronaut.core.annotation.Introspected

@Introspected
data class BankAccountRequest(
    val participant: Int,
    val branch: String,
    val accountNumber: Int,
    val accountType: String
) {}