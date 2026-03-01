package com.servico.revenda.veiculos_clientes.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClienteResponse(
        UUID id,
        String nome,
        String cpfMascarado,
        String emailMascarado,
        String telefoneMascarado,
        String endereco,
        boolean consentimentoLgpd,
        boolean ativo,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {}
