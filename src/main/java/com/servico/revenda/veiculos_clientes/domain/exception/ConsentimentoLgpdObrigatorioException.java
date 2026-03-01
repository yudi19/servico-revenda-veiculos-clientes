package com.servico.revenda.veiculos_clientes.domain.exception;

public class ConsentimentoLgpdObrigatorioException extends RuntimeException {
    public ConsentimentoLgpdObrigatorioException() {
        super("Consentimento LGPD é obrigatório para cadastro de cliente");
    }
}
