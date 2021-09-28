package br.com.zup.dto

import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.client.itau.response.ContaClienteErpResponse
import br.com.zup.client.itau.response.InstituicaoErpResponse
import br.com.zup.client.itau.response.TitularErpResponse
import br.com.zup.model.Conta
import br.com.zup.model.NovaChave
import br.com.zup.validation.ValidPixKey
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
@Introspected
class NovaChaveDto (
    @field: NotBlank val clienteId: String?,
    @field: NotNull val tipoChave: TipoChave?,
    @field: Size(max=77) val chave: String?,
    @field: NotNull val tipoConta: TipoConta?
) {
    fun toModel(conta: Conta): NovaChave {
        return NovaChave(
            clienteId = UUID.fromString(this.clienteId),
            tipoChave = TipoChave.valueOf(this.tipoChave!!.name),
            chave = if(this.tipoChave == TipoChave.ALEATORIA) UUID.randomUUID().toString() else chave!!,
            tipoConta = TipoConta.valueOf(this.tipoConta!!.name),
            conta = conta
        )
    }
}