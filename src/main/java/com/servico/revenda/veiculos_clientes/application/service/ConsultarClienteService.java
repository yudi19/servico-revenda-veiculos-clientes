package com.servico.revenda.veiculos_clientes.application.service;

import com.servico.revenda.veiculos_clientes.domain.exception.ClienteNaoEncontradoException;
import com.servico.revenda.veiculos_clientes.domain.model.Cliente;
import com.servico.revenda.veiculos_clientes.domain.port.in.ConsultarClienteUseCase;
import com.servico.revenda.veiculos_clientes.domain.port.out.AuditPort;
import com.servico.revenda.veiculos_clientes.domain.port.out.ClienteRepositoryPort;
import com.servico.revenda.veiculos_clientes.domain.port.out.CryptoPort;

import java.util.UUID;

public class ConsultarClienteService implements ConsultarClienteUseCase {

    private final ClienteRepositoryPort repositoryPort;
    private final CryptoPort cryptoPort;
    private final AuditPort auditPort;

    public ConsultarClienteService(ClienteRepositoryPort repositoryPort, CryptoPort cryptoPort, AuditPort auditPort) {
        this.repositoryPort = repositoryPort;
        this.cryptoPort = cryptoPort;
        this.auditPort = auditPort;
    }

    @Override
    public Cliente executar(UUID id) {
        Cliente cliente = repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));

        auditPort.registrar(id, "CONSULTA", "Consulta de dados do cliente");

        cliente.descriptografarDados(cryptoPort::decrypt);

        return cliente;
    }
}
