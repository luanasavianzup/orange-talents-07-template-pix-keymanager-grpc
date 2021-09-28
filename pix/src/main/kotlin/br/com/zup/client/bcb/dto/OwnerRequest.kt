package br.com.zup.client.bcb.dto

import br.com.zup.client.bcb.TipoUsuario
import io.micronaut.core.annotation.Introspected

@Introspected
data class OwnerRequest(
    val type: TipoUsuario,
    val name: String,
    val taxIdNumber: String
)
