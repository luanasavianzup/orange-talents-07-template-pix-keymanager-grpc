package br.com.zup.client.bcb.dto

import br.com.zup.client.bcb.TipoChaveBanco
import br.com.zup.client.bcb.TipoContaBanco
import br.com.zup.client.bcb.TipoUsuario
import br.com.zup.model.Conta
import br.com.zup.model.NovaChave
import io.micronaut.core.annotation.Introspected

@Introspected
data class CreatePixKeyRequest(
    val keyType: TipoChaveBanco,
    val key: String,
    val bankAccount: BankAccountRequest,
    val owner: OwnerRequest
) {
    companion object {
        fun of(chave: NovaChave): CreatePixKeyRequest {
            return CreatePixKeyRequest(
                keyType = TipoChaveBanco.by(chave.tipoChave),
                key = chave.chave,
                bankAccount = BankAccountRequest(
                    participant = Conta.ITAU_UNIBANCO_ISPB,
                    branch = chave.conta.agencia,
                    accountNumber = chave.conta.numero,
                    accountType = TipoContaBanco.by(chave.tipoConta)
                ),
                owner = OwnerRequest(
                    type = TipoUsuario.NATURAL_PERSON,
                    name = chave.conta.titular.nomeCliente,
                    taxIdNumber = chave.conta.titular.cpfCliente
                )
            )
        }
    }
}