package com.servico.revenda.veiculos_clientes.infrastructure.adapter.out.messaging;

import com.servico.revenda.veiculos_clientes.domain.port.out.SagaCallbackPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sfn.SfnClient;
import software.amazon.awssdk.services.sfn.model.SendTaskFailureRequest;
import software.amazon.awssdk.services.sfn.model.SendTaskSuccessRequest;

@Component
public class StepFunctionsCallbackAdapter implements SagaCallbackPort {

    private static final Logger log = LoggerFactory.getLogger(StepFunctionsCallbackAdapter.class);

    private final SfnClient sfnClient;

    public StepFunctionsCallbackAdapter(SfnClient sfnClient) {
        this.sfnClient = sfnClient;
    }

    @Override
    public void enviarSucesso(String taskToken, String resultado) {
        log.info("Enviando callback de sucesso para Step Functions. TaskToken: {}", taskToken);
        sfnClient.sendTaskSuccess(SendTaskSuccessRequest.builder()
                .taskToken(taskToken)
                .output(resultado)
                .build());
    }

    @Override
    public void enviarFalha(String taskToken, String erro, String causa) {
        log.error("Enviando callback de falha para Step Functions. Erro: {}, Causa: {}", erro, causa);
        sfnClient.sendTaskFailure(SendTaskFailureRequest.builder()
                .taskToken(taskToken)
                .error(erro)
                .cause(causa)
                .build());
    }
}
