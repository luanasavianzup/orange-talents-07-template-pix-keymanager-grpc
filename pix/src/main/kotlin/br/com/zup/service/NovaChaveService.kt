package br.com.zup.service

import br.com.zup.client.ErpItauClient
import br.com.zup.client.response.ContaClienteErpResponse
import br.com.zup.dto.NovaChaveRequest
import br.com.zup.exception.ChaveExistenteException
import br.com.zup.exception.ClienteNaoEncontradoException
import br.com.zup.model.NovaChave
import br.com.zup.repository.ChaveRepository
import io.micronaut.http.HttpResponse
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class NovaChaveService (@Inject val chaveRepository: ChaveRepository,
                        @Inject val erpItauClient: ErpItauClient
){
    @Transactional
    fun registra(@Valid novaChave: NovaChaveRequest): NovaChave{
        val response: HttpResponse<ContaClienteErpResponse>
        try {
           response = erpItauClient.consulta(novaChave.clienteId!!, novaChave.tipoConta!!)
        } catch(e: Exception){
            throw ClienteNaoEncontradoException()
        }
        if(chaveRepository.existsByChave(novaChave.chave)){
            throw ChaveExistenteException()
        }

        val chave = novaChave.toModel()

        chaveRepository.save(chave)

        return chave
    }
}
