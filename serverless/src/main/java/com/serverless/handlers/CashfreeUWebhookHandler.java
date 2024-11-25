package com.serverless.handlers;

import java.util.Map;
import java.util.UUID;
import com.serverless.dto.ApiGatewayResponse;
import com.serverless.dto.KafkaRecord;
import com.serverless.dto.Response;
import com.serverless.utils.KafkaRestProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CashfreeUWebhookHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	private static final Logger LOG = LogManager.getLogger(CashfreeUWebhookHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("received: {}", input);
		try {
			KafkaRecord record = new KafkaRecord(UUID.randomUUID().toString(), input);
			KafkaRestProducer.produceMessage(System.getenv("CASHFREE_WEBHOOK_KAFKA_TOPIC_NAME"), record);
		} catch (Exception e) {
			Response responseBody = new Response("Unable to process message!", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(200)
					.setObjectBody(responseBody)
					.build();
		}
		Response responseBody = new Response("Webhook Processed!", input);
		var response = ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(responseBody)
				.build();
		LOG.info("completed processing: {}", input);
		return response;
	}
}
