package br.com.zup.service

import br.com.zup.client.bcb.BcbClient
import br.com.zup.client.itau.ErpItauClient
import br.com.zup.dto.ConsultaChaveDto
import br.com.zup.exception.ChaveNaoEncontradaException
import br.com.zup.model.NovaChave
import br.com.zup.repository.ChaveRepository
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import jakarta.inject.Singleton

@Validated
@Singleton
class ConsultaChaveService (val chaveRepository: ChaveRepository,
                            val erpItauClient: ErpItauClient,
                            val bcbClient: BcbClient
) {
    fun consulta(consultaChave: ConsultaChaveDto): NovaChave {
        return if (consultaChave.chave.isBlank()) {
            val chaveBanco = chaveRepository.findByIdAndClienteId(consultaChave.pixId, consultaChave.clienteId)
            if (chaveBanco.isEmpty)
                throw ChaveNaoEncontradaException()

            return chaveBanco.get()
        } else {
            val chaveBanco = chaveRepository.findByChave(consultaChave.chave)
            if (chaveBanco.isPresent) {
                return chaveBanco.get()
            } else {
                val chaveDetalhada = bcbClient.consulta(consultaChave.chave)
                if(chaveDetalhada.status != HttpStatus.OK)
                    throw IllegalStateException("Erro ao detalhar chave Pix pelo Banco Central do Brasil")

                return chaveDetalhada.body().toModel()
            }
        }
    }
}