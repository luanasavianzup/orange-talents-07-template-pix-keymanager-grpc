package br.com.zup.client.bcb.dto

import io.micronaut.core.annotation.Introspected

@Introspected
data class OwnerRequest(
    val type: String,
    val name: String,
    val taxIdNumber: String
)
