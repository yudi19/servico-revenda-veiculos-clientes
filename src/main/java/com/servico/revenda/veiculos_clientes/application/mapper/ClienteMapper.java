package com.servico.revenda.veiculos_clientes.application.mapper;

import com.servico.revenda.veiculos_clientes.application.dto.ClienteResponse;
import com.servico.revenda.veiculos_clientes.domain.model.Cliente;

public class ClienteMapper {

    private ClienteMapper() {}

    public static ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                mascararCpf(cliente.getCpf()),
                mascararEmail(cliente.getEmail()),
                mascararTelefone(cliente.getTelefone()),
                cliente.getEndereco(),
                cliente.isConsentimentoLgpd(),
                cliente.isAtivo(),
                cliente.getDataCriacao(),
                cliente.getDataAtualizacao()
        );
    }

    private static String mascararCpf(String cpf) {
        if (cpf == null || cpf.length() < 6) return "***.***.***-**";
        return "***.***." + cpf.substring(cpf.length() - 6);
    }

    private static String mascararEmail(String email) {
        if (email == null || !email.contains("@")) return "***@***";
        int atIndex = email.indexOf("@");
        return email.charAt(0) + "***" + email.substring(atIndex);
    }

    private static String mascararTelefone(String telefone) {
        if (telefone == null || telefone.length() < 4) return "(**) *****-****";
        return "(**) *****-" + telefone.substring(telefone.length() - 4);
    }
}
