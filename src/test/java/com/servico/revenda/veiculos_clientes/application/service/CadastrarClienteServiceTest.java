package com.servico.revenda.veiculos_clientes.application.service;

import com.servico.revenda.veiculos_clientes.domain.exception.ConsentimentoLgpdObrigatorioException;
import com.servico.revenda.veiculos_clientes.domain.model.Cliente;
import com.servico.revenda.veiculos_clientes.domain.port.out.ClienteRepositoryPort;
import com.servico.revenda.veiculos_clientes.domain.port.out.CryptoPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarClienteServiceTest {

    @Mock
    private ClienteRepositoryPort repositoryPort;

    @Mock
    private CryptoPort cryptoPort;

    private CadastrarClienteService service;

    @BeforeEach
    void setUp() {
        service = new CadastrarClienteService(repositoryPort, cryptoPort);
    }

    @Test
    void deveCadastrarClienteComConsentimentoLgpd() {
        when(cryptoPort.encrypt(any(String.class))).thenAnswer(inv -> "encrypted-" + inv.getArgument(0));
        when(repositoryPort.salvar(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente resultado = service.executar("João", "12345678900", "joao@email.com",
                "(11) 99999-9999", "Rua Teste", true);

        assertNotNull(resultado.getId());
        assertEquals("João", resultado.getNome());
        assertEquals("encrypted-12345678900", resultado.getCpf());
        assertEquals("encrypted-joao@email.com", resultado.getEmail());
        assertEquals("encrypted-(11) 99999-9999", resultado.getTelefone());
        assertEquals("Rua Teste", resultado.getEndereco());
        assertTrue(resultado.isConsentimentoLgpd());
        assertTrue(resultado.isAtivo());
        assertNotNull(resultado.getDataCriacao());
        verify(repositoryPort).salvar(any(Cliente.class));
        verify(cryptoPort, times(3)).encrypt(any(String.class));
    }

    @Test
    void deveLancarExcecaoSemConsentimentoLgpd() {
        assertThrows(ConsentimentoLgpdObrigatorioException.class, () ->
                service.executar("João", "12345678900", "joao@email.com",
                        "(11) 99999-9999", "Rua Teste", false));

        verify(repositoryPort, never()).salvar(any());
        verify(cryptoPort, never()).encrypt(any());
    }
}
