package com.servico.revenda.veiculos_clientes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.sfn.SfnClient;
import software.amazon.awssdk.services.sqs.SqsClient;

@SpringBootTest
class VeiculosClientesApplicationTests {

	@MockitoBean
	private SqsClient sqsClient;

	@MockitoBean
	private SfnClient sfnClient;

	@MockitoBean
	private KmsClient kmsClient;

	@Test
	void contextLoads() {
	}

}
