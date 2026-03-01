package com.servico.revenda.veiculos_clientes.application.service;

import com.servico.revenda.veiculos_clientes.domain.exception.ClienteNaoEncontradoException;
import com.servico.revenda.veiculos_clientes.domain.model.Cliente;
import com.servico.revenda.veiculos_clientes.domain.port.out.ClienteRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidarClienteServiceTest {

    @Mock
    private ClienteRepositoryPort repositoryPort;

    private ValidarClienteService service;

    @BeforeEach
    void setUp() {
        service = new ValidarClienteService(repositoryPort);
    }

    @Test
    void deveValidarClienteAtivoComConsentimento() {
        UUID id = UUID.randomUUID();
        Cliente existente = criarCliente(id, true, true);
        when(repositoryPort.buscarPorId(id)).thenReturn(Optional.of(existente));

        Cliente resultado = service.executar(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
    }

    @Test
    void deveLancarExcecaoParaClienteInativo() {
        UUID id = UUID.randomUUID();
        Cliente existente = criarCliente(id, true, false);
        when(repositoryPort.buscarPorId(id)).thenReturn(Optional.of(existente));

        assertThrows(IllegalStateException.class, () -> service.executar(id));
    }

    @Test
    void deveLancarExcecaoParaClienteSemConsentimento() {
        UUID id = UUID.randomUUID();
        Cliente existente = criarCliente(id, false, true);
        when(repositoryPort.buscarPorId(id)).thenReturn(Optional.of(existente));

        assertThrows(IllegalStateException.class, () -> service.executar(id));
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(repositoryPort.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class, () -> service.executar(id));
    }

    private Cliente criarCliente(UUID id, boolean consentimentoLgpd, boolean ativo) {
        LocalDateTime agora = LocalDateTime.now();
        return new Cliente(id, "João Silva", "encrypted-cpf", "encrypted-email",
                "encrypted-telefone", "Rua Teste, 123", consentimentoLgpd, ativo, agora, agora);
    }
}
