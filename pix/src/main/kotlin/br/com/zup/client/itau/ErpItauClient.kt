package br.com.zup.client.itau

import br.com.zup.TipoConta
import br.com.zup.client.itau.response.ContaClienteErpResponse
import br.com.zup.client.itau.response.TitularErpResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("\${itau.contas.url}")
interface ErpItauClient {

    @Get("/api/v1/clientes/{clientId}")
    fun consulta(@PathVariable clientId: String) : HttpResponse<TitularErpResponse>

    @Get("/api/v1/clientes/{clienteId}/contas{?tipo}")
    fun consulta(@PathVariable clienteId: String, @QueryValue tipo: TipoConta) : HttpResponse<ContaClienteErpResponse>
}