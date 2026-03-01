package com.servico.revenda.veiculos_clientes.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_acesso_dados")
public class AuditAcessoDadosJpaEntity {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;

    @Column(name = "tipo_acesso", nullable = false)
    private String tipoAcesso;

    private String detalhes;

    @Column(name = "data_acesso", nullable = false)
    private LocalDateTime dataAcesso;

    public AuditAcessoDadosJpaEntity() {}

    public AuditAcessoDadosJpaEntity(UUID id, UUID clienteId, String tipoAcesso,
                                      String detalhes, LocalDateTime dataAcesso) {
        this.id = id;
        this.clienteId = clienteId;
        this.tipoAcesso = tipoAcesso;
        this.detalhes = detalhes;
        this.dataAcesso = dataAcesso;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getClienteId() { return clienteId; }
    public void setClienteId(UUID clienteId) { this.clienteId = clienteId; }

    public String getTipoAcesso() { return tipoAcesso; }
    public void setTipoAcesso(String tipoAcesso) { this.tipoAcesso = tipoAcesso; }

    public String getDetalhes() { return detalhes; }
    public void setDetalhes(String detalhes) { this.detalhes = detalhes; }

    public LocalDateTime getDataAcesso() { return dataAcesso; }
    public void setDataAcesso(LocalDateTime dataAcesso) { this.dataAcesso = dataAcesso; }
}
