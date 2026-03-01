package com.servico.revenda.veiculos_clientes.infrastructure.config;

import com.servico.revenda.veiculos_clientes.application.service.*;
import com.servico.revenda.veiculos_clientes.domain.port.in.*;
import com.servico.revenda.veiculos_clientes.domain.port.out.AuditPort;
import com.servico.revenda.veiculos_clientes.domain.port.out.ClienteRepositoryPort;
import com.servico.revenda.veiculos_clientes.domain.port.out.CryptoPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class BeanConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public CadastrarClienteUseCase cadastrarClienteUseCase(ClienteRepositoryPort repositoryPort, CryptoPort cryptoPort) {
        return new CadastrarClienteService(repositoryPort, cryptoPort);
    }

    @Bean
    public EditarClienteUseCase editarClienteUseCase(ClienteRepositoryPort repositoryPort, CryptoPort cryptoPort) {
        return new EditarClienteService(repositoryPort, cryptoPort);
    }

    @Bean
    public ConsultarClienteUseCase consultarClienteUseCase(ClienteRepositoryPort repositoryPort, CryptoPort cryptoPort, AuditPort auditPort) {
        return new ConsultarClienteService(repositoryPort, cryptoPort, auditPort);
    }

    @Bean
    public ExcluirDadosClienteUseCase excluirDadosClienteUseCase(ClienteRepositoryPort repositoryPort, AuditPort auditPort) {
        return new ExcluirDadosClienteService(repositoryPort, auditPort);
    }

    @Bean
    public ValidarClienteUseCase validarClienteUseCase(ClienteRepositoryPort repositoryPort) {
        return new ValidarClienteService(repositoryPort);
    }

    @Bean
    public GerarDocumentacaoUseCase gerarDocumentacaoUseCase(ClienteRepositoryPort repositoryPort, CryptoPort cryptoPort, AuditPort auditPort) {
        return new GerarDocumentacaoService(repositoryPort, cryptoPort, auditPort);
    }
}
