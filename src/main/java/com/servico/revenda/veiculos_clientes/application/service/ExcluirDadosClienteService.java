package com.servico.revenda.veiculos_clientes.application.service;

import com.servico.revenda.veiculos_clientes.domain.exception.ClienteNaoEncontradoException;
import com.servico.revenda.veiculos_clientes.domain.model.Cliente;
import com.servico.revenda.veiculos_clientes.domain.port.in.ExcluirDadosClienteUseCase;
import com.servico.revenda.veiculos_clientes.domain.port.out.AuditPort;
import com.servico.revenda.veiculos_clientes.domain.port.out.ClienteRepositoryPort;

import java.util.UUID;

public class ExcluirDadosClienteService implements ExcluirDadosClienteUseCase {

    private final ClienteRepositoryPort repositoryPort;
    private final AuditPort auditPort;

    public ExcluirDadosClienteService(ClienteRepositoryPort repositoryPort, AuditPort auditPort) {
        this.repositoryPort = repositoryPort;
        this.auditPort = auditPort;
    }

    @Override
    public void executar(UUID id) {
        Cliente cliente = repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));

        cliente.excluirDados();
        repositoryPort.salvar(cliente);

        auditPort.registrar(id, "EXCLUSAO_LGPD", "Exclusão de dados sensíveis do cliente por solicitação LGPD");
    }
}
