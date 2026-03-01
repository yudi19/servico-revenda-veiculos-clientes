package com.servico.revenda.veiculos_clientes.infrastructure.adapter.out.crypto;

import com.servico.revenda.veiculos_clientes.domain.port.out.CryptoPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptResponse;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class KmsCryptoAdapter implements CryptoPort {

    private final KmsClient kmsClient;
    private final String keyArn;

    public KmsCryptoAdapter(KmsClient kmsClient, @Value("${aws.kms.key-arn}") String keyArn) {
        this.kmsClient = kmsClient;
        this.keyArn = keyArn;
    }

    @Override
    public String encrypt(String plaintext) {
        if (plaintext == null) return null;

        EncryptResponse response = kmsClient.encrypt(EncryptRequest.builder()
                .keyId(keyArn)
                .plaintext(SdkBytes.fromUtf8String(plaintext))
                .build());

        return Base64.getEncoder().encodeToString(response.ciphertextBlob().asByteArray());
    }

    @Override
    public String decrypt(String ciphertext) {
        if (ciphertext == null) return null;

        byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);

        DecryptResponse response = kmsClient.decrypt(DecryptRequest.builder()
                .ciphertextBlob(SdkBytes.fromByteArray(ciphertextBytes))
                .build());

        return response.plaintext().asString(StandardCharsets.UTF_8);
    }
}
