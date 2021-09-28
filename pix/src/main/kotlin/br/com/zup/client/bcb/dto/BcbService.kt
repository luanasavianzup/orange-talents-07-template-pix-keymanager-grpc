package br.com.zup.client.bcb.dto

import br.com.zup.TipoChave
import br.com.zup.client.bcb.BcbClient
import br.com.zup.model.Conta
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import sun.security.rsa.RSAUtil

@Singleton
class BcbService {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Inject
    private lateinit var bcbClient: BcbClient

    fun createRequest(
        tipoChave: TipoChave,
        conta: Conta,
        valorChave: String
    ): CreatePixKeyRequest {
        val keyType: RSAUtil.KeyType = tipoChave.converte()
        val bankAccount = BankAccountDTO(conta)
        val owner = OwnerDTO(conta.titular)
        return CreatePixKeyRequest(keyType, valorChave, bankAccount, owner)
    }

    fun registra(request: BcbCreatePixKeyRequest): HttpResponse<BcbCreatePixKeyResponse> {
        val bcbHttpResponse: HttpResponse<BcbCreatePixKeyResponse>
        try {
            bcbHttpResponse = bcbClient.registra(request)
        } catch (ex: HttpClientResponseException) {
            log.error("Erro no registro no BCB: Status ${ex.status}. Mensagem ${ex.message}")
            if (ex.status == HttpStatus.UNPROCESSABLE_ENTITY)
                throw IllegalStateException("Chave já cadastrada no BCB")
            else
                throw InternalServerError("Erro inesperado")
        } catch (ex: HttpClientException) {
            log.error("Erro na conexão com BCB: ${ex.message}")
            throw ServiceUnavailableException("Serviço indisponível")
        }
        return bcbHttpResponse
    }

    fun remove(key: String, participant: String): HttpResponse<BcbDeletePixKeyResponse> {
        val bcbRequest = BcbDeletePixKeyRequest(key, participant)
        val bcbResponse: HttpResponse<BcbDeletePixKeyResponse>

        try {
            bcbResponse = bcbClient.remove(bcbRequest.key, bcbRequest)
        } catch (e: HttpClientResponseException) {
            if (e.status == HttpStatus.FORBIDDEN)
                throw ChaveDeOutraInstituicaoException("Chave pertence a outra instituição")
            else
                throw InternalServerError("Erro inesperado")

        } catch (e: HttpClientException) {
            log.error("Erro de conexão com ERP: ${e.message}")
            throw ServiceUnavailableException("Serviço indisponível")
        }

        return bcbResponse
    }
}