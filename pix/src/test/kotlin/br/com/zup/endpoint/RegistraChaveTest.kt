package br.com.zup.endpoint

import br.com.zup.*
import br.com.zup.client.itau.ErpItauClient
import br.com.zup.client.itau.response.ContaClienteErpResponse
import br.com.zup.client.itau.response.InstituicaoErpResponse
import br.com.zup.client.itau.response.TitularErpResponse
import br.com.zup.model.NovaChave
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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*

@MicronautTest(transactional = false)
internal class RegistraChaveTest(
    val chaveRepository: ChaveRepository,
    val grpcClient: RegistraPixServiceGrpc.RegistraPixServiceBlockingStub,
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

    @AfterEach
    internal fun tearDown() {
        chaveRepository.deleteAll()
    }

    @Test
    internal fun `deve cadastrar nova chave pix`() {
        val request = PixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoChave(TipoChave.EMAIL)
            .setChave("teste@email.com")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .build()

        Mockito.`when`(itauClient.consulta(request.clienteId, TipoConta.valueOf(request.tipoConta.name)))
            .thenReturn(HttpResponse.ok(response()))

        val response: PixResponse = grpcClient.registra(request)

        with(response) {
            assertNotNull(pixId)
            assertTrue(chaveRepository.existsByChave(pixId))
        }
    }

    @Test
    fun `nao deve cadastrar chave pix repetida`() {
        val request = PixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoChave(TipoChave.EMAIL)
            .setChave("teste@email.com")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .build()

        val chave = NovaChave(
            clienteId = clienteId,
            tipoChave = TipoChave.EMAIL,
            chave = "teste@email.com",
            tipoConta = TipoConta.CONTA_CORRENTE
        )

        Mockito.`when`(itauClient.consulta(request.clienteId, TipoConta.valueOf(request.tipoConta.name)))
            .thenReturn(HttpResponse.ok(response()))

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.registra(request)
        }

        with(error) {
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("Chave já foi cadastrada", status.description)
        }
    }

    @Test
    fun `nao deve cadastrar chave pix invalida`() {
        val request = PixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoChave(TipoChave.EMAIL)
            .setChave("sfx")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .build()

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.registra(request)
        }

        with(error) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals(
                "Chave pix enviada está em formato inválido",
                error.status.description
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
}