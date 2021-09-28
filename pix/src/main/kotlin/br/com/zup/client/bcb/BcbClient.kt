package br.com.zup.client.bcb

import br.com.zup.client.bcb.dto.CreatePixKeyRequest
import br.com.zup.client.bcb.dto.CreatePixKeyResponse
import br.com.zup.client.bcb.dto.DeletePixKeyRequest
import br.com.zup.client.bcb.dto.DeletePixKeyResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
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
    fun remove(@PathVariable key: String, @Body request: String): HttpResponse<DeletePixKeyResponse>

}