package br.com.zup.client.bcb.dto

import br.com.zup.client.bcb.TipoContaBanco
import io.micronaut.core.annotation.Introspected

@Introspected
data class BankAccountRequest(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: TipoContaBanco
) {}