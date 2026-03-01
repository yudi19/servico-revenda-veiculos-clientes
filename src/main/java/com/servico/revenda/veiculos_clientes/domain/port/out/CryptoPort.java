package com.servico.revenda.veiculos_clientes.domain.port.out;

public interface CryptoPort {
    String encrypt(String plaintext);
    String decrypt(String ciphertext);
}
