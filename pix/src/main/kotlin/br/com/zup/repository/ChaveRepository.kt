package br.com.zup.repository

import br.com.zup.model.NovaChave
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ChaveRepository : JpaRepository<NovaChave, Long> {

    fun existsByChave(chave: String?) : Boolean

   // fun findByIdAndIdCliente(id: Long, idCliente: String) : Optional<NovaChave>
}