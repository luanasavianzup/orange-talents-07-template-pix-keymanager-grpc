package br.com.zup.exception

class ClienteNaoEncontradoException: Exception() {
    override val message: String?
        get() = "Nenhum cliente com este id foi encontrado"
}