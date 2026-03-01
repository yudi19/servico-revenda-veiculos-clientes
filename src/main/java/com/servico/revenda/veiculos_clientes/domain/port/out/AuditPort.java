package com.servico.revenda.veiculos_clientes.domain.port.out;

import java.util.UUID;

public interface AuditPort {
    void registrar(UUID clienteId, String tipoAcesso, String detalhes);
}
