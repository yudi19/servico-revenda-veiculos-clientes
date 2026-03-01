package com.servico.revenda.veiculos_clientes.infrastructure.adapter.out.persistence;

import com.servico.revenda.veiculos_clientes.domain.port.out.AuditPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AuditAdapter implements AuditPort {

    private final AuditAcessoDadosJpaRepository auditRepository;

    public AuditAdapter(AuditAcessoDadosJpaRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public void registrar(UUID clienteId, String tipoAcesso, String detalhes) {
        AuditAcessoDadosJpaEntity audit = new AuditAcessoDadosJpaEntity(
                UUID.randomUUID(),
                clienteId,
                tipoAcesso,
                detalhes,
                LocalDateTime.now()
        );
        auditRepository.save(audit);
    }
}
