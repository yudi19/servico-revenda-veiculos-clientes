package com.servico.revenda.veiculos_clientes.domain.port.out;

import com.servico.revenda.veiculos_clientes.domain.model.Cliente;

import java.util.Optional;
import java.util.UUID;

public interface ClienteRepositoryPort {
    Cliente salvar(Cliente cliente);
    Optional<Cliente> buscarPorId(UUID id);
}
