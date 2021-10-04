package br.com.zup.endpoint

import br.com.zup.*
import br.com.zup.exception.ChaveNaoEncontradaException
import br.com.zup.model.NovaChave
import br.com.zup.service.ConsultaChaveService
import br.com.zup.utils.toModel
import com.google.protobuf.Timestamp
import io.grpc.Status
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.time.ZoneId

@Singleton
class ConsultaChaveEndpoint(@Inject private val service: ConsultaChaveService)
    : ConsultaPixServiceGrpc.ConsultaPixServiceImplBase() {

    override fun consulta(request: ConsultaChaveRequest, responseObserver: StreamObserver<ConsultaChaveResponse>) {
        var chaveInfo: NovaChave? = null
        val detalhaChave = request.toModel()
        try{
            chaveInfo = service.consulta(detalhaChave)
        }catch (e: ChaveNaoEncontradaException){
            responseObserver?.onError(
                Status.NOT_FOUND
                    .withDescription(e.message)
                    .asRuntimeException()
            )
        }catch (e: IllegalStateException){
            responseObserver?.onError(Status.INTERNAL
                .withDescription(e.message)
                .asRuntimeException())
        } catch (e: Exception){
            responseObserver?.onError(Status.INTERNAL
                .withDescription(e.message)
                .asRuntimeException())
        }

        responseObserver?.onNext(chaveInfo?.id?.let {
            ConsultaChaveResponse.newBuilder()
                .setClienteId(chaveInfo?.clienteId.toString() ?: "")
                .setPixId(it.toLong())
                .setChave(ConsultaChaveResponse.Chave
                    .newBuilder()
                    .setTipo(chaveInfo?.tipoConta?.name?.let { TipoConta.valueOf(it) })
                    .setChave(chaveInfo?.chave)
                    .setConta(ConsultaChaveResponse.Chave.ContaInfo.newBuilder()
                        .setTipo(chaveInfo?.tipoConta?.name?.let { TipoConta.valueOf(it) })
                        .setInstituicao(chaveInfo?.conta?.instituicao!!.nomeInstituicao)
                        .setNomeTitular(chaveInfo?.conta?.titular!!.nomeCliente)
                        .setCpfTitular(chaveInfo?.conta?.titular.cpfCliente)
                        .setAgencia(chaveInfo?.conta?.agencia)
                        .setNumeroConta(chaveInfo?.conta?.numero)
                        .build()
                    )
                    .setCriadaEm(chaveInfo?.criadaEm?.let {
                        val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                        Timestamp.newBuilder()
                            .setSeconds(createdAt.epochSecond)
                            .setNanos(createdAt.nano)
                            .build()
                    })
                )
                .build()
        }
        )
        responseObserver?.onCompleted()
    }
}