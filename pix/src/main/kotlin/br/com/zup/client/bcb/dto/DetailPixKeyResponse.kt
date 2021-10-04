package br.com.zup.client.bcb.dto

import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.client.bcb.TipoChaveBanco
import br.com.zup.client.bcb.TipoContaBanco
import br.com.zup.model.Conta
import br.com.zup.model.Instituicao
import br.com.zup.model.NovaChave
import br.com.zup.model.Titular
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime

@Introspected
data class DetailPixKeyResponse (
    val keyType: TipoChaveBanco,
    val key: String,
    val bankAccount: BankAccountRequest,
    val owner: OwnerRequest,
    val createdAt: LocalDateTime
) {
    fun toModel(): NovaChave {
        var tipoConta: TipoConta
        if (bankAccount.accountType == TipoContaBanco.CACC) {
            tipoConta = TipoConta.CONTA_CORRENTE
        } else {
            tipoConta = TipoConta.CONTA_POUPANCA
        }
        return NovaChave(
            clienteId = null,
            tipoChave = TipoChave.UNKNOWN_CHAVE, //
            chave = key,
            tipoConta = tipoConta,
            conta = Conta(instituicao = Instituicao(nomeInstituicao = "", ispb = ""),
                agencia = bankAccount.branch,
                numero = bankAccount.accountNumber,
                titular = Titular(nomeCliente = owner.name,
                    cpfCliente = owner.taxIdNumber)
            )
        )
    }
}