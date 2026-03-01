package com.servico.revenda.veiculos_clientes.domain.exception;

import java.util.UUID;

public class ClienteNaoEncontradoException extends RuntimeException {
    public ClienteNaoEncontradoException(UUID id) {
        super("Cliente não encontrado com id: " + id);
    }
}
