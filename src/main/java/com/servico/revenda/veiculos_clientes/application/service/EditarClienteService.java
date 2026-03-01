package com.servico.revenda.veiculos_clientes.application.service;

import com.servico.revenda.veiculos_clientes.domain.exception.ClienteNaoEncontradoException;
import com.servico.revenda.veiculos_clientes.domain.model.Cliente;
import com.servico.revenda.veiculos_clientes.domain.port.in.EditarClienteUseCase;
import com.servico.revenda.veiculos_clientes.domain.port.out.ClienteRepositoryPort;
import com.servico.revenda.veiculos_clientes.domain.port.out.CryptoPort;

import java.time.LocalDateTime;
import java.util.UUID;

public class EditarClienteService implements EditarClienteUseCase {

    private final ClienteRepositoryPort repositoryPort;
    private final CryptoPort cryptoPort;

    public EditarClienteService(ClienteRepositoryPort repositoryPort, CryptoPort cryptoPort) {
        this.repositoryPort = repositoryPort;
        this.cryptoPort = cryptoPort;
    }

    @Override
    public Cliente executar(UUID id, String nome, String email, String telefone, String endereco) {
        Cliente cliente = repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));

        if (nome != null) {
            cliente.setNome(nome);
        }
        if (email != null) {
            cliente.setEmail(cryptoPort.encrypt(email));
        }
        if (telefone != null) {
            cliente.setTelefone(cryptoPort.encrypt(telefone));
        }
        if (endereco != null) {
            cliente.setEndereco(endereco);
        }

        cliente.setDataAtualizacao(LocalDateTime.now());
        return repositoryPort.salvar(cliente);
    }
}
