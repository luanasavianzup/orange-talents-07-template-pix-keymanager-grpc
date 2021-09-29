package br.com.zup.endpoint

import br.com.zup.RemoveChaveRequest
import br.com.zup.RemoveChaveResponse
import br.com.zup.RemovePixServiceGrpc
import br.com.zup.exception.ChaveNaoEncontradaException
import br.com.zup.exception.ClienteNaoEncontradoException
import br.com.zup.service.RemoveChaveService
import br.com.zup.utils.toModel
import io.grpc.Status
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import javax.validation.ConstraintViolationException

@Singleton
class RemoveChaveEndpoint(@Inject private val removeChaveService: RemoveChaveService) : RemovePixServiceGrpc.RemovePixServiceImplBase() {

    private val logger = LoggerFactory.getLogger(RemoveChaveEndpoint::class.java)

    override fun remove(request: RemoveChaveRequest, responseObserver: StreamObserver<RemoveChaveResponse>?) {
        val removeChaveDto = request.toModel()

        try{
            removeChaveService.remove(removeChaveDto)
        }catch (e: IllegalStateException){
            responseObserver?.onError(
                Status.INTERNAL
                .withDescription(e.message)
                .asRuntimeException())
            return
        }catch (e: ConstraintViolationException){
            responseObserver?.onError(
                Status.INVALID_ARGUMENT
                    .withDescription(e.message)
                    .asRuntimeException()
            )
            return
        }catch (e: ClienteNaoEncontradoException){
            responseObserver?.onError(
                Status.NOT_FOUND
                    .withDescription(e.message)
                    .asRuntimeException()
            )
            return
        }catch (e: ChaveNaoEncontradaException){
            responseObserver?.onError(
                Status.NOT_FOUND
                    .withDescription(e.message)
                    .asRuntimeException()
            )
            return
        }

        val response = RemoveChaveResponse.newBuilder()
            .setClienteId(request.clienteId)
            .setPixId(request.pixId)
            .build()

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()

        logger.info("A chave pix foi removida!")
    }
}
