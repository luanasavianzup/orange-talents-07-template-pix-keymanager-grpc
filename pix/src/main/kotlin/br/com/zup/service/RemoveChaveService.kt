package br.com.zup.service

import br.com.zup.client.bcb.BcbClient
import br.com.zup.client.itau.ErpItauClient
import br.com.zup.client.bcb.dto.DeletePixKeyResponse
import br.com.zup.dto.RemoveChaveDto
import br.com.zup.exception.ChaveNaoEncontradaException
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

        val possivelChave = chaveRepository.findByIdAndClienteId(
            UUID.fromString(removeChaveDto.pixId.toString()),
            removeChaveDto.clienteId
        )
        if(possivelChave.isEmpty)
            throw ChaveNaoEncontradaException()

        val chave = possivelChave.get()

        val bcbResponse: HttpResponse<DeletePixKeyResponse> = bcbClient.remove(chave.chave, chave.conta.instituicao.ispb)
        if (bcbResponse.status != HttpStatus.OK)
            throw ChaveNaoEncontradaException()

        chaveRepository.delete(chave)
    }
}