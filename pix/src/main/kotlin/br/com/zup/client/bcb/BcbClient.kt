package br.com.zup.client.bcb

import br.com.zup.client.bcb.dto.*
import br.com.zup.model.NovaChave
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client

@Client("\${bcb.pix.url}")
interface BcbClient {

    @Post("/api/v1/pix/keys",
        produces = [MediaType.APPLICATION_XML],
        consumes = [MediaType.APPLICATION_XML]
    )
    fun cria(@Body request: CreatePixKeyRequest): HttpResponse<CreatePixKeyResponse>

    @Delete("/api/v1/pix/keys/{key}",
        produces = [MediaType.APPLICATION_XML],
        consumes = [MediaType.APPLICATION_XML]
    )
    fun remove(@PathVariable key: String, @Body request: DeletePixKeyRequest): HttpResponse<DeletePixKeyResponse>

    @Get("/api/v1/pix/keys/{key}",
        produces = [MediaType.APPLICATION_XML],
        consumes = [MediaType.APPLICATION_XML]
    )
    fun consulta(@PathVariable key: String): HttpResponse<DetailPixKeyResponse>
}