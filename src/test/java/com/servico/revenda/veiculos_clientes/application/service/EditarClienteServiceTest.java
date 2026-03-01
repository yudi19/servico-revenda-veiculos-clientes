package com.servico.revenda.veiculos_clientes.application.service;

import com.servico.revenda.veiculos_clientes.domain.exception.ClienteNaoEncontradoException;
import com.servico.revenda.veiculos_clientes.domain.model.Cliente;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditarClienteServiceTest {

    @Mock
    private ClienteRepositoryPort repositoryPort;

    @Mock
    private CryptoPort cryptoPort;

    private EditarClienteService service;

    @BeforeEach
    void setUp() {
        service = new EditarClienteService(repositoryPort, cryptoPort);
    }

    @Test
    void deveEditarTodosOsCampos() {
        UUID id = UUID.randomUUID();
        Cliente existente = criarCliente(id);
        when(repositoryPort.buscarPorId(id)).thenReturn(Optional.of(existente));
        when(cryptoPort.encrypt("novo@email.com")).thenReturn("encrypted-novo@email.com");
        when(cryptoPort.encrypt("(11) 88888-8888")).thenReturn("encrypted-(11) 88888-8888");
        when(repositoryPort.salvar(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente resultado = service.executar(id, "Maria", "novo@email.com", "(11) 88888-8888", "Rua Nova, 456");

        assertEquals("Maria", resultado.getNome());
        assertEquals("encrypted-novo@email.com", resultado.getEmail());
        assertEquals("encrypted-(11) 88888-8888", resultado.getTelefone());
        assertEquals("Rua Nova, 456", resultado.getEndereco());
        verify(repositoryPort).salvar(any(Cliente.class));
    }

    @Test
    void deveEditarApenasOsCamposInformados() {
        UUID id = UUID.randomUUID();
        Cliente existente = criarCliente(id);
        when(repositoryPort.buscarPorId(id)).thenReturn(Optional.of(existente));
        when(repositoryPort.salvar(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente resultado = service.executar(id, "Maria", null, null, null);

        assertEquals("Maria", resultado.getNome());
        assertEquals("encrypted-email", resultado.getEmail());
        assertEquals("encrypted-telefone", resultado.getTelefone());
        assertEquals("Rua Teste, 123", resultado.getEndereco());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(repositoryPort.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class, () ->
                service.executar(id, "Maria", null, null, null));
        verify(repositoryPort, never()).salvar(any());
    }

    private Cliente criarCliente(UUID id) {
        LocalDateTime agora = LocalDateTime.now();
        return new Cliente(id, "João Silva", "encrypted-cpf", "encrypted-email",
                "encrypted-telefone", "Rua Teste, 123", true, true, agora, agora);
    }
}
