package br.com.zup.endpoint

import br.com.zup.*
import br.com.zup.client.itau.ErpItauClient
import br.com.zup.client.itau.response.ContaClienteErpResponse
import br.com.zup.client.itau.response.InstituicaoErpResponse
import br.com.zup.client.itau.response.TitularErpResponse
import br.com.zup.model.Conta
import br.com.zup.model.Instituicao
import br.com.zup.model.NovaChave
import br.com.zup.model.Titular
import br.com.zup.repository.ChaveRepository
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*

@MicronautTest(transactional = false)
internal class RegistraChaveTest2(
    val chaveRepository: ChaveRepository,
    val grpcClient: RegistraPixServiceGrpc.RegistraPixServiceBlockingStub
) {
    @Inject
    lateinit var itauClient: ErpItauClient

    companion object {
        val clienteId = UUID.randomUUID()
    }

    @BeforeEach
    internal fun setUp() {
        chaveRepository.deleteAll()
    }

    @Test
    internal fun `deve cadastrar nova chave pix`() {
        val response = grpcClient.registra(PixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoChave(TipoChave.EMAIL)
            .setChave("teste@email.com")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .build())

        Mockito.`when`(itauClient.consulta(clienteId = clienteId.toString(), tipo = TipoConta.CONTA_CORRENTE))
            .thenReturn(HttpResponse.ok(response()))

        with(response) {
            assertNotNull(pixId)
            assertEquals(clienteId.toString(), pixId)
        }
    }

    @Test
    fun `nao deve cadastrar chave pix repetida`() {
        chaveRepository.save(NovaChave(
            clienteId = clienteId.toString(),
            tipoChave = TipoChave.CPF,
            chave = "63657520325",
            tipoConta = TipoConta.CONTA_CORRENTE,
            conta = Conta(Instituicao("", ""),"","", Titular("",""))
        ))

        val thrown = assertThrows<StatusRuntimeException> {
            grpcClient.registra(PixRequest.newBuilder()
                .setClienteId(clienteId.toString())
                .setTipoChave(TipoChave.EMAIL)
                .setChave("teste@email.com")
                .setTipoConta(TipoConta.CONTA_CORRENTE)
                .build())
        }

        with(thrown) {
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("Chave já foi cadastrada", status.description)
        }
    }

    @Test
    fun `nao deve cadastrar chave pix invalida`() {
        val thrown = assertThrows<StatusRuntimeException> {
            grpcClient.registra(PixRequest.newBuilder().build())
        }

        with(thrown) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals(
                "Chave pix inválida",
                status.description
            )
        }
    }

    @Test
    fun `nao deve cadastrar chave pix sem cliente ja cadastrado`() {
        val request = PixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoChave(TipoChave.EMAIL)
            .setChave("teste@email.com")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .build()

        Mockito.`when`(
            itauClient.consulta(
                clienteId = "c56dfef4-7901-44fb-84e2-123456789012",
                tipo = TipoConta.CONTA_CORRENTE
            )
        ).thenReturn(null)

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.registra(request)
        }

        with(error) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Cliente com esse id não foi encontrado", status.description)
        }
    }

    @Factory
    class Clients {
        @Bean
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): RegistraPixServiceGrpc.RegistraPixServiceBlockingStub? {
            return RegistraPixServiceGrpc.newBlockingStub(channel)
        }
    }

    @MockBean(ErpItauClient::class)
    fun erpItauClient(): ErpItauClient? {
        return Mockito.mock(ErpItauClient::class.java)
    }

    private fun response(): ContaClienteErpResponse {
        return ContaClienteErpResponse(
            tipo = "CONTA_CORRENTE",
            instituicao = InstituicaoErpResponse("ITAÚ UNIBANCO S.A.", "98756537"),
            agencia = "1218",
            numero = "291900",
            titular = TitularErpResponse(
                "Rafael M C Ponte",
                "63657520325"
            )
        )
    }
}