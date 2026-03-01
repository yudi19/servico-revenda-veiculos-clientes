package com.servico.revenda.veiculos_clientes.application.service;

import com.servico.revenda.veiculos_clientes.domain.exception.ClienteNaoEncontradoException;
import com.servico.revenda.veiculos_clientes.domain.model.Cliente;
import com.servico.revenda.veiculos_clientes.domain.port.out.AuditPort;
import com.servico.revenda.veiculos_clientes.domain.port.out.ClienteRepositoryPort;
import com.servico.revenda.veiculos_clientes.domain.port.out.CryptoPort;
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
class GerarDocumentacaoServiceTest {

    @Mock
    private ClienteRepositoryPort repositoryPort;

    @Mock
    private CryptoPort cryptoPort;

    @Mock
    private AuditPort auditPort;

    private GerarDocumentacaoService service;

    @BeforeEach
    void setUp() {
        service = new GerarDocumentacaoService(repositoryPort, cryptoPort, auditPort);
    }

    @Test
    void deveGerarDocumentacaoDescriptografandoDados() {
        UUID id = UUID.randomUUID();
        Cliente existente = criarCliente(id);
        when(repositoryPort.buscarPorId(id)).thenReturn(Optional.of(existente));
        when(cryptoPort.decrypt("encrypted-cpf")).thenReturn("123.456.789-00");
        when(cryptoPort.decrypt("encrypted-email")).thenReturn("joao@email.com");
        when(cryptoPort.decrypt("encrypted-telefone")).thenReturn("(11) 99999-9999");

        Cliente resultado = service.executar(id);

        assertEquals("123.456.789-00", resultado.getCpf());
        assertEquals("joao@email.com", resultado.getEmail());
        verify(auditPort).registrar(eq(id), eq("GERACAO_DOCUMENTACAO"), anyString());
        verify(cryptoPort, times(3)).decrypt(anyString());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(repositoryPort.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class, () -> service.executar(id));
        verify(auditPort, never()).registrar(any(), any(), any());
    }

    private Cliente criarCliente(UUID id) {
        LocalDateTime agora = LocalDateTime.now();
        return new Cliente(id, "João Silva", "encrypted-cpf", "encrypted-email",
                "encrypted-telefone", "Rua Teste, 123", true, true, agora, agora);
    }
}
