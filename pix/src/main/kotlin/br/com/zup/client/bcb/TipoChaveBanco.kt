package br.com.zup.client.bcb

import br.com.zup.TipoChave

enum class TipoChaveBanco(val domainType: TipoChave?) {
    CPF(TipoChave.CPF), CNPJ(null), PHONE(TipoChave.CELULAR), EMAIL(TipoChave.EMAIL), RANDOM(TipoChave.ALEATORIA);

    companion object {
        private val mapping = TipoChaveBanco.values().associateBy(TipoChaveBanco::domainType)
        fun by(domainType: TipoChave): TipoChaveBanco {
            return mapping[domainType] ?: throw IllegalArgumentException("Tipo de chave inválido")
        }
    }
}