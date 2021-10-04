package br.com.zup.model

import br.com.zup.TipoChave
import br.com.zup.TipoConta
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class NovaChave(
    @field: NotNull val clienteId: String?,
    @field: NotNull @Enumerated(EnumType.STRING) val tipoChave: TipoChave,
    @field: Size(max = 77) var chave: String = UUID.randomUUID().toString(),
    @field: NotNull @field: Enumerated(EnumType.STRING) val tipoConta: TipoConta,
    @Embedded val conta: Conta
) {
    @field: Id
    @field: GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val criadaEm: LocalDateTime = LocalDateTime.now()

    fun chaveAleatoria(chave: String): Boolean {
        if (tipoChave == TipoChave.ALEATORIA) {
            this.chave = chave
            return true
        }
        return false
    }
}