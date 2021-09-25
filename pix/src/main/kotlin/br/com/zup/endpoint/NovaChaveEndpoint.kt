package br.com.zup.endpoint

import br.com.zup.PixRequest
import br.com.zup.PixResponse
import br.com.zup.RegistraPixServiceGrpc
import br.com.zup.exception.ChaveExistenteException
import br.com.zup.exception.ClienteNaoEncontradoException
import br.com.zup.service.NovaChaveService
import br.com.zup.utils.toModel
import io.grpc.Status
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import javax.validation.ConstraintViolationException

@Singleton
class NovaChaveEndpoint(@Inject private val service: NovaChaveService)
    : RegistraPixServiceGrpc.RegistraPixServiceImplBase() {

    private val logger = LoggerFactory.getLogger(NovaChaveEndpoint::class.java)

    override fun registra(request: PixRequest, responseObserver: StreamObserver<PixResponse>) {
        try {
            val chaveRequest = request.toModel()
            val novaChave = service.registra(chaveRequest)

            responseObserver.onNext(PixResponse.newBuilder()
                .setClienteId(novaChave.clienteId.toString())
                .setPixId(novaChave.id.toString())
                .build())

            responseObserver.onCompleted()
            logger.info("A chave pix ${novaChave.chave} foi cadastrada com sucesso!")
        } catch (e: ConstraintViolationException) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription(e.message)
                    .asRuntimeException()
            )
        } catch (e: ChaveExistenteException) {
            responseObserver.onError(
                Status.ALREADY_EXISTS
                    .withDescription(e.message)
                    .asRuntimeException()
            )
        } catch (e: ClienteNaoEncontradoException) {
            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription(e.message)
                    .asRuntimeException()
            )
        } catch (e: Exception) {
            responseObserver.onError(
                Status.INTERNAL
                    .withDescription(e.message)
                    .asRuntimeException()
            )
        }
    }
}