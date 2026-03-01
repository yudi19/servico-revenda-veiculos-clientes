package com.servico.revenda.veiculos_clientes.infrastructure.adapter.out.persistence;

import com.servico.revenda.veiculos_clientes.domain.model.Cliente;
import com.servico.revenda.veiculos_clientes.domain.port.out.ClienteRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final ClienteJpaRepository jpaRepository;

    public ClienteRepositoryAdapter(ClienteJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        ClienteJpaEntity entity = ClientePersistenceMapper.toEntity(cliente);
        ClienteJpaEntity saved = jpaRepository.save(entity);
        return ClientePersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(ClientePersistenceMapper::toDomain);
    }
}
