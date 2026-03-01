package com.servico.revenda.veiculos_clientes.application.service;

import com.servico.revenda.veiculos_clientes.domain.exception.ClienteNaoEncontradoException;
import com.servico.revenda.veiculos_clientes.domain.model.Cliente;
import com.servico.revenda.veiculos_clientes.domain.port.in.GerarDocumentacaoUseCase;
import com.servico.revenda.veiculos_clientes.domain.port.out.AuditPort;
import com.servico.revenda.veiculos_clientes.domain.port.out.ClienteRepositoryPort;
import com.servico.revenda.veiculos_clientes.domain.port.out.CryptoPort;

import java.util.UUID;

public class GerarDocumentacaoService implements GerarDocumentacaoUseCase {

    private final ClienteRepositoryPort repositoryPort;
    private final CryptoPort cryptoPort;
    private final AuditPort auditPort;

    public GerarDocumentacaoService(ClienteRepositoryPort repositoryPort, CryptoPort cryptoPort, AuditPort auditPort) {
        this.repositoryPort = repositoryPort;
        this.cryptoPort = cryptoPort;
        this.auditPort = auditPort;
    }

    @Override
    public Cliente executar(UUID clienteId) {
        Cliente cliente = repositoryPort.buscarPorId(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));

        auditPort.registrar(clienteId, "GERACAO_DOCUMENTACAO", "Geração de documentação do cliente");

        cliente.descriptografarDados(cryptoPort::decrypt);

        return cliente;
    }
}
