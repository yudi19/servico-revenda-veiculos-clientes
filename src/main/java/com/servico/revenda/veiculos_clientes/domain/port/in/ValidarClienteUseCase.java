package com.servico.revenda.veiculos_clientes.domain.port.in;

import com.servico.revenda.veiculos_clientes.domain.model.Cliente;

import java.util.UUID;

public interface ValidarClienteUseCase {
    Cliente executar(UUID clienteId);
}
