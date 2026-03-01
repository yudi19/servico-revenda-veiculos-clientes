package com.servico.revenda.veiculos_clientes.domain.port.in;

import com.servico.revenda.veiculos_clientes.domain.model.Cliente;

public interface CadastrarClienteUseCase {
    Cliente executar(String nome, String cpf, String email, String telefone,
                     String endereco, boolean consentimentoLgpd);
}
