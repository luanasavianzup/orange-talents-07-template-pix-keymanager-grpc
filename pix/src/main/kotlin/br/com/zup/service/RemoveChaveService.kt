package br.com.zup.service

import br.com.zup.client.bcb.BcbClient
import br.com.zup.client.bcb.dto.DeletePixKeyRequest
import br.com.zup.client.itau.ErpItauClient
import br.com.zup.client.bcb.dto.DeletePixKeyResponse
import br.com.zup.client.itau.response.TitularErpResponse
import br.com.zup.dto.RemoveChaveDto
import br.com.zup.exception.ChaveNaoEncontradaException
import br.com.zup.exception.ClienteNaoEncontradoException
import br.com.zup.model.Conta
import br.com.zup.repository.ChaveRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import java.util.*
import javax.validation.Valid

@Validated
@Singleton
class RemoveChaveService(val chaveRepository: ChaveRepository,
                         val erpItauClient: ErpItauClient,
                         val bcbClient: BcbClient
) {
    fun remove(@Valid removeChaveDto: RemoveChaveDto){

        try {
            val clientResponse: HttpResponse<TitularErpResponse> = erpItauClient.consulta(removeChaveDto.clienteId!!)
        }catch(e: Exception){
            throw ClienteNaoEncontradoException()
        }

        val possivelChave = chaveRepository.findByIdAndClienteId(
            removeChaveDto.pixId.toLong(),
            removeChaveDto.clienteId
        )
        if(possivelChave.isEmpty)
            throw ChaveNaoEncontradaException()

        val chave = possivelChave.get()

        val bcbResponse: HttpResponse<DeletePixKeyResponse> = bcbClient.remove(chave.chave, DeletePixKeyRequest(chave.chave,
            Conta.ITAU_UNIBANCO_ISPB))
        if (bcbResponse.status != HttpStatus.OK)
            throw IllegalStateException("Erro ao remover a chave do Banco Central")

        chaveRepository.delete(chave)
    }
}