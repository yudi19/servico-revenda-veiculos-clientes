package com.servico.revenda.veiculos_clientes.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditAcessoDadosJpaRepository extends JpaRepository<AuditAcessoDadosJpaEntity, UUID> {
}
