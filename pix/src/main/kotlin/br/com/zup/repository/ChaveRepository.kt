package br.com.zup.repository

import br.com.zup.model.NovaChave
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChaveRepository : JpaRepository<NovaChave, Long> {

    fun existsByChave(chave: String?) : Boolean

   fun findByIdAndClienteId(pixId: Long, clienteId: String) : Optional<NovaChave>
}