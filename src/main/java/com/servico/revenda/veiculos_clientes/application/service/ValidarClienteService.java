package com.servico.revenda.veiculos_clientes.application.service;

import com.servico.revenda.veiculos_clientes.domain.exception.ClienteNaoEncontradoException;
import com.servico.revenda.veiculos_clientes.domain.model.Cliente;
import com.servico.revenda.veiculos_clientes.domain.port.in.ValidarClienteUseCase;
import com.servico.revenda.veiculos_clientes.domain.port.out.ClienteRepositoryPort;

import java.util.UUID;

public class ValidarClienteService implements ValidarClienteUseCase {

    private final ClienteRepositoryPort repositoryPort;

    public ValidarClienteService(ClienteRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Cliente executar(UUID clienteId) {
        Cliente cliente = repositoryPort.buscarPorId(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));

        cliente.validarParaSaga();
        return cliente;
    }
}
