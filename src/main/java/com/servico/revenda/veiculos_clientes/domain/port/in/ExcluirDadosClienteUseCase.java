package com.servico.revenda.veiculos_clientes.domain.port.in;

import java.util.UUID;

public interface ExcluirDadosClienteUseCase {
    void executar(UUID id);
}
