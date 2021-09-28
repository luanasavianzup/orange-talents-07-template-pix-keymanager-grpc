package br.com.zup.repository

import br.com.zup.model.NovaChave
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChaveRepository : JpaRepository<NovaChave, UUID> {

    fun existsByChave(chave: String?) : Boolean

   fun findByIdAndClienteId(pixId: UUID, clienteId: UUID) : Optional<NovaChave>
}