package br.com.zup.exception

class ChaveExistenteException : Exception() {
    override val message: String?
        get() = "Esta chave já foi cadastrada!"
}