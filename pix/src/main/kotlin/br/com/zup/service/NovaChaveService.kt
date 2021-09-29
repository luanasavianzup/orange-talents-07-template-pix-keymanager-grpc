package br.com.zup.service

import br.com.zup.client.bcb.BcbClient
import br.com.zup.client.bcb.TipoUsuario
import br.com.zup.client.bcb.dto.BankAccountRequest
import br.com.zup.client.bcb.dto.CreatePixKeyRequest
import br.com.zup.client.bcb.dto.OwnerRequest
import br.com.zup.client.itau.ErpItauClient
import br.com.zup.client.itau.response.ContaClienteErpResponse
import br.com.zup.dto.NovaChaveDto
import br.com.zup.exception.ChaveExistenteException
import br.com.zup.exception.ClienteNaoEncontradoException
import br.com.zup.model.NovaChave
import br.com.zup.repository.ChaveRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class NovaChaveService(
    @Inject val chaveRepository: ChaveRepository,
    @Inject val erpItauClient: ErpItauClient,
    @Inject val bcbClient: BcbClient
) {
    @Transactional
    fun registra(@Valid novaChave: NovaChaveDto): NovaChave {
        var response: HttpResponse<ContaClienteErpResponse>
        try {
            response = erpItauClient.consulta(novaChave.clienteId!!, novaChave.tipoConta!!)
        } catch(e: Exception){
            throw ClienteNaoEncontradoException()
        }

        if (chaveRepository.existsByChave(novaChave.chave)) {
            throw ChaveExistenteException()
        }
        val chave = novaChave.toModel(response.body().toModel())

        val bcbRequest = CreatePixKeyRequest.of(chave)

        val bcbResponse = bcbClient.cria(bcbRequest)

        if (bcbResponse.status != HttpStatus.CREATED) {
            throw IllegalStateException("Falha ao registrar a chave Pix")
        }

        chave.chaveAleatoria(bcbResponse.body().key)

        chaveRepository.save(chave)

        return chave
    }
}
