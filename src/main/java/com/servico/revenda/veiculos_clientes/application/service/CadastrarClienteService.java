package com.servico.revenda.veiculos_clientes.application.service;

import com.servico.revenda.veiculos_clientes.domain.exception.ConsentimentoLgpdObrigatorioException;
import com.servico.revenda.veiculos_clientes.domain.model.Cliente;
import com.servico.revenda.veiculos_clientes.domain.port.in.CadastrarClienteUseCase;
import com.servico.revenda.veiculos_clientes.domain.port.out.ClienteRepositoryPort;
import com.servico.revenda.veiculos_clientes.domain.port.out.CryptoPort;

import java.time.LocalDateTime;
import java.util.UUID;

public class CadastrarClienteService implements CadastrarClienteUseCase {

    private final ClienteRepositoryPort repositoryPort;
    private final CryptoPort cryptoPort;

    public CadastrarClienteService(ClienteRepositoryPort repositoryPort, CryptoPort cryptoPort) {
        this.repositoryPort = repositoryPort;
        this.cryptoPort = cryptoPort;
    }

    @Override
    public Cliente executar(String nome, String cpf, String email, String telefone,
                            String endereco, boolean consentimentoLgpd) {
        if (!consentimentoLgpd) {
            throw new ConsentimentoLgpdObrigatorioException();
        }

        LocalDateTime agora = LocalDateTime.now();
        Cliente cliente = new Cliente(
                UUID.randomUUID(),
                nome,
                cryptoPort.encrypt(cpf),
                cryptoPort.encrypt(email),
                cryptoPort.encrypt(telefone),
                endereco,
                true,
                true,
                agora,
                agora
        );

        return repositoryPort.salvar(cliente);
    }
}
