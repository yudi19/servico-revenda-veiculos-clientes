package com.servico.revenda.veiculos_clientes.domain.port.out;

public interface SagaCallbackPort {
    void enviarSucesso(String taskToken, String resultado);
    void enviarFalha(String taskToken, String erro, String causa);
}
