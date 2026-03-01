package com.servico.revenda.veiculos_clientes.infrastructure.adapter.in.web;

import com.servico.revenda.veiculos_clientes.application.dto.CadastrarClienteRequest;
import com.servico.revenda.veiculos_clientes.application.dto.ClienteResponse;
import com.servico.revenda.veiculos_clientes.application.dto.EditarClienteRequest;
import com.servico.revenda.veiculos_clientes.application.mapper.ClienteMapper;
import com.servico.revenda.veiculos_clientes.domain.model.Cliente;
import com.servico.revenda.veiculos_clientes.domain.port.in.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final CadastrarClienteUseCase cadastrarClienteUseCase;
    private final EditarClienteUseCase editarClienteUseCase;
    private final ConsultarClienteUseCase consultarClienteUseCase;
    private final ExcluirDadosClienteUseCase excluirDadosClienteUseCase;

    public ClienteController(CadastrarClienteUseCase cadastrarClienteUseCase,
                              EditarClienteUseCase editarClienteUseCase,
                              ConsultarClienteUseCase consultarClienteUseCase,
                              ExcluirDadosClienteUseCase excluirDadosClienteUseCase) {
        this.cadastrarClienteUseCase = cadastrarClienteUseCase;
        this.editarClienteUseCase = editarClienteUseCase;
        this.consultarClienteUseCase = consultarClienteUseCase;
        this.excluirDadosClienteUseCase = excluirDadosClienteUseCase;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrar(@Valid @RequestBody CadastrarClienteRequest request) {
        Cliente cliente = cadastrarClienteUseCase.executar(
                request.nome(), request.cpf(), request.email(), request.telefone(),
                request.endereco(), request.consentimentoLgpd()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toResponse(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> editar(@PathVariable UUID id,
                                                   @Valid @RequestBody EditarClienteRequest request) {
        Cliente cliente = editarClienteUseCase.executar(
                id, request.nome(), request.email(), request.telefone(), request.endereco()
        );
        return ResponseEntity.ok(ClienteMapper.toResponse(cliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> consultar(@PathVariable UUID id) {
        Cliente cliente = consultarClienteUseCase.executar(id);
        return ResponseEntity.ok(ClienteMapper.toResponse(cliente));
    }

    @DeleteMapping("/{id}/dados")
    public ResponseEntity<Void> excluirDados(@PathVariable UUID id) {
        excluirDadosClienteUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }
}
