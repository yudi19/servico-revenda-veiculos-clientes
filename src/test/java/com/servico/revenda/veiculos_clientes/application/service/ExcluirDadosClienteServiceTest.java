package com.servico.revenda.veiculos_clientes.application.service;

import com.servico.revenda.veiculos_clientes.domain.exception.ClienteNaoEncontradoException;
import com.servico.revenda.veiculos_clientes.domain.model.Cliente;
import com.servico.revenda.veiculos_clientes.domain.port.out.AuditPort;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExcluirDadosClienteServiceTest {

    @Mock
    private ClienteRepositoryPort repositoryPort;

    @Mock
    private AuditPort auditPort;

    private ExcluirDadosClienteService service;

    @BeforeEach
    void setUp() {
        service = new ExcluirDadosClienteService(repositoryPort, auditPort);
    }

    @Test
    void deveExcluirDadosSensiveisDoCliente() {
        UUID id = UUID.randomUUID();
        Cliente existente = criarCliente(id);
        when(repositoryPort.buscarPorId(id)).thenReturn(Optional.of(existente));
        when(repositoryPort.salvar(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        service.executar(id);

        assertNull(existente.getCpf());
        assertNull(existente.getEmail());
        assertNull(existente.getTelefone());
        assertFalse(existente.isAtivo());
        verify(repositoryPort).salvar(existente);
        verify(auditPort).registrar(eq(id), eq("EXCLUSAO_LGPD"), anyString());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(repositoryPort.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class, () -> service.executar(id));
        verify(repositoryPort, never()).salvar(any());
        verify(auditPort, never()).registrar(any(), any(), any());
    }

    private Cliente criarCliente(UUID id) {
        LocalDateTime agora = LocalDateTime.now();
        return new Cliente(id, "João Silva", "encrypted-cpf", "encrypted-email",
                "encrypted-telefone", "Rua Teste, 123", true, true, agora, agora);
    }
}
