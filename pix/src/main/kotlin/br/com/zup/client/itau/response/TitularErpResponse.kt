package br.com.zup.client.itau.response

import br.com.zup.model.Titular
import io.micronaut.core.annotation.Introspected
import java.util.*

@Introspected
data class TitularErpResponse(
    val nome: String,
    val cpf: String
) {
    fun toModel(): Titular {
        return Titular(
            nomeCliente = nome,
            cpfCliente = cpf
        )
    }
}
