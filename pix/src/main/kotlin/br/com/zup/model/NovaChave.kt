package br.com.zup.model

import br.com.zup.TipoChave
import br.com.zup.TipoConta
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class NovaChave(
    @field: NotNull val clienteId: UUID,
    @field: NotNull @Enumerated(EnumType.STRING) val tipoChave: TipoChave,
    @field: Size(max = 77) val chave: String,
    @field: NotNull @field: Enumerated(EnumType.STRING) val tipoConta: TipoConta
) {
    @field: Id
    @field: GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}