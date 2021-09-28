package br.com.zup.client.bcb

import br.com.zup.TipoConta
import org.hibernate.metamodel.model.domain.BasicDomainType

enum class TipoContaBanco() {
    CACC, SVGS;

    companion object {
        fun by(domainType: TipoConta): TipoContaBanco {
            return when (domainType) {
                TipoConta.CONTA_CORRENTE -> CACC
                TipoConta.CONTA_POUPANCA -> SVGS
                TipoConta.UNKNOWN_CONTA -> TODO()
                TipoConta.UNRECOGNIZED -> TODO()
            }
        }
    }
}
