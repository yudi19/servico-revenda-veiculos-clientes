package com.servico.revenda.veiculos_clientes.application.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastrarClienteRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "CPF é obrigatório")
        @Size(min = 11, max = 11, message = "CPF deve conter exatamente 11 dígitos")
        String cpf,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        String email,

        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @NotBlank(message = "Endereço é obrigatório")
        String endereco,

        @NotNull(message = "Consentimento LGPD é obrigatório")
        @AssertTrue(message = "Consentimento LGPD deve ser verdadeiro para cadastro")
        Boolean consentimentoLgpd
) {}
