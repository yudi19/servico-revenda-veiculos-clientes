package com.servico.revenda.veiculos_clientes.application.dto;

import jakarta.validation.constraints.Email;

public record EditarClienteRequest(
        String nome,

        @Email(message = "Email deve ser válido")
        String email,

        String telefone,

        String endereco
) {}
