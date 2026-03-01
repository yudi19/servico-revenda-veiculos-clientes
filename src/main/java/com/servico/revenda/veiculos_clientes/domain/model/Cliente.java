package com.servico.revenda.veiculos_clientes.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class Cliente {

    private UUID id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String endereco;
    private boolean consentimentoLgpd;
    private boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Cliente() {}

    public Cliente(UUID id, String nome, String cpf, String email, String telefone,
                   String endereco, boolean consentimentoLgpd, boolean ativo,
                   LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.consentimentoLgpd = consentimentoLgpd;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public void excluirDados() {
        this.cpf = null;
        this.email = null;
        this.telefone = null;
        this.ativo = false;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void descriptografarDados(UnaryOperator<String> decrypt) {
        if (this.cpf != null) this.cpf = decrypt.apply(this.cpf);
        if (this.email != null) this.email = decrypt.apply(this.email);
        if (this.telefone != null) this.telefone = decrypt.apply(this.telefone);
    }

    public void validarParaSaga() {
        if (!this.ativo) {
            throw new IllegalStateException("Cliente " + this.id + " está inativo");
        }
        if (!this.consentimentoLgpd) {
            throw new IllegalStateException("Cliente " + this.id + " não possui consentimento LGPD");
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public boolean isConsentimentoLgpd() { return consentimentoLgpd; }
    public void setConsentimentoLgpd(boolean consentimentoLgpd) { this.consentimentoLgpd = consentimentoLgpd; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}
