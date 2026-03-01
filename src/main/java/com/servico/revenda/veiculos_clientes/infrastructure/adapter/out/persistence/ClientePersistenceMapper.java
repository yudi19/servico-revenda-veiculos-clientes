package com.servico.revenda.veiculos_clientes.infrastructure.adapter.out.persistence;

import com.servico.revenda.veiculos_clientes.domain.model.Cliente;

public class ClientePersistenceMapper {

    private ClientePersistenceMapper() {}

    public static ClienteJpaEntity toEntity(Cliente cliente) {
        return new ClienteJpaEntity(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getEndereco(),
                cliente.isConsentimentoLgpd(),
                cliente.isAtivo(),
                cliente.getDataCriacao(),
                cliente.getDataAtualizacao()
        );
    }

    public static Cliente toDomain(ClienteJpaEntity entity) {
        return new Cliente(
                entity.getId(),
                entity.getNome(),
                entity.getCpf(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.getEndereco(),
                entity.isConsentimentoLgpd(),
                entity.isAtivo(),
                entity.getDataCriacao(),
                entity.getDataAtualizacao()
        );
    }
}
