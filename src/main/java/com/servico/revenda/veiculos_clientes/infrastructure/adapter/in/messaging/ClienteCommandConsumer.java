package com.servico.revenda.veiculos_clientes.infrastructure.adapter.in.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.servico.revenda.veiculos_clientes.application.mapper.ClienteMapper;
import com.servico.revenda.veiculos_clientes.domain.model.Cliente;
import com.servico.revenda.veiculos_clientes.domain.port.in.GerarDocumentacaoUseCase;
import com.servico.revenda.veiculos_clientes.domain.port.in.ValidarClienteUseCase;
import com.servico.revenda.veiculos_clientes.domain.port.out.SagaCallbackPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;
import java.util.UUID;

@Component
public class ClienteCommandConsumer {

    private static final Logger log = LoggerFactory.getLogger(ClienteCommandConsumer.class);

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final ValidarClienteUseCase validarClienteUseCase;
    private final GerarDocumentacaoUseCase gerarDocumentacaoUseCase;
    private final SagaCallbackPort sagaCallbackPort;
    private final String queueUrl;

    public ClienteCommandConsumer(SqsClient sqsClient,
                                   ObjectMapper objectMapper,
                                   ValidarClienteUseCase validarClienteUseCase,
                                   GerarDocumentacaoUseCase gerarDocumentacaoUseCase,
                                   SagaCallbackPort sagaCallbackPort,
                                   @Value("${aws.sqs.clientes-commands-url}") String queueUrl) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
        this.validarClienteUseCase = validarClienteUseCase;
        this.gerarDocumentacaoUseCase = gerarDocumentacaoUseCase;
        this.sagaCallbackPort = sagaCallbackPort;
        this.queueUrl = queueUrl;
    }

    @Scheduled(fixedDelay = 5000)
    public void consumirMensagens() {
        try {
            List<Message> messages = sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(10)
                    .waitTimeSeconds(5)
                    .build()).messages();

            for (Message message : messages) {
                processarMensagem(message);
            }
        } catch (Exception e) {
            log.warn("Erro ao consumir mensagens SQS: {}", e.getMessage());
        }
    }

    private void processarMensagem(Message message) {
        try {
            JsonNode root = objectMapper.readTree(message.body());
            String comando = root.get("nome").asText();
            String taskToken = root.get("taskToken").asText();
            JsonNode payload = root.get("payload");
            UUID clienteId = UUID.fromString(payload.get("clienteId").asText());

            log.info("Processando comando: {} para cliente: {}", comando, clienteId);

            switch (comando) {
                case "VALIDAR_CLIENTE" -> {
                    Cliente cliente = validarClienteUseCase.executar(clienteId);
                    String resultado = objectMapper.writeValueAsString(ClienteMapper.toResponse(cliente));
                    sagaCallbackPort.enviarSucesso(taskToken, resultado);
                }
                case "GERAR_DOCUMENTACAO" -> {
                    Cliente cliente = gerarDocumentacaoUseCase.executar(clienteId);
                    String resultado = objectMapper.writeValueAsString(ClienteMapper.toResponse(cliente));
                    sagaCallbackPort.enviarSucesso(taskToken, resultado);
                }
                default -> {
                    log.warn("Comando desconhecido: {}", comando);
                    sagaCallbackPort.enviarFalha(taskToken, "COMANDO_DESCONHECIDO",
                            "Comando não reconhecido: " + comando);
                }
            }

            deletarMensagem(message);

        } catch (Exception e) {
            log.error("Erro ao processar mensagem: {}", e.getMessage(), e);
            try {
                JsonNode root = objectMapper.readTree(message.body());
                String taskToken = root.get("taskToken").asText();
                sagaCallbackPort.enviarFalha(taskToken, e.getClass().getSimpleName(), e.getMessage());
            } catch (Exception inner) {
                log.error("Erro ao enviar callback de falha: {}", inner.getMessage(), inner);
            }
            deletarMensagem(message);
        }
    }

    private void deletarMensagem(Message message) {
        sqsClient.deleteMessage(DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle())
                .build());
    }
}
