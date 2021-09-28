package br.com.zup.exception

class ChaveNaoEncontradaException: Exception() {
    override val message: String?
        get() = "Chave pix não encontrada"
}