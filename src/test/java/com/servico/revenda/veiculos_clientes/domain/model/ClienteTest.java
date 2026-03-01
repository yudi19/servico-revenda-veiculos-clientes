package com.servico.revenda.veiculos_clientes.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void deveExcluirDadosSensiveis() {
        Cliente cliente = criarCliente(true, true);

        cliente.excluirDados();

        assertNull(cliente.getCpf());
        assertNull(cliente.getEmail());
        assertNull(cliente.getTelefone());
        assertFalse(cliente.isAtivo());
        assertNotNull(cliente.getDataAtualizacao());
    }

    @Test
    void deveManterNomeEEnderecoAposExcluirDados() {
        Cliente cliente = criarCliente(true, true);

        cliente.excluirDados();

        assertEquals("João Silva", cliente.getNome());
        assertEquals("Rua Teste, 123", cliente.getEndereco());
    }

    @Test
    void deveValidarParaSagaComSucesso() {
        Cliente cliente = criarCliente(true, true);

        assertDoesNotThrow(cliente::validarParaSaga);
    }

    @Test
    void deveLancarExcecaoQuandoClienteInativo() {
        Cliente cliente = criarCliente(true, false);

        IllegalStateException ex = assertThrows(IllegalStateException.class, cliente::validarParaSaga);
        assertTrue(ex.getMessage().contains("inativo"));
    }

    @Test
    void deveLancarExcecaoQuandoSemConsentimentoLgpd() {
        Cliente cliente = criarCliente(false, true);

        IllegalStateException ex = assertThrows(IllegalStateException.class, cliente::validarParaSaga);
        assertTrue(ex.getMessage().contains("consentimento LGPD"));
    }

    private Cliente criarCliente(boolean consentimentoLgpd, boolean ativo) {
        LocalDateTime agora = LocalDateTime.now();
        return new Cliente(
                UUID.randomUUID(),
                "João Silva",
                "encrypted-cpf",
                "encrypted-email",
                "encrypted-telefone",
                "Rua Teste, 123",
                consentimentoLgpd,
                ativo,
                agora,
                agora
        );
    }
}
