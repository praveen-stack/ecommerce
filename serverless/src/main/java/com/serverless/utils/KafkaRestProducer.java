package com.serverless.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.dto.KafkaProducerInput;
import com.serverless.dto.KafkaRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Arrays;
import java.util.Base64;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class KafkaRestProducer {

    private static final Logger LOG = LogManager.getLogger(KafkaRestProducer.class);
    private static String getBasicAuth() {
        String username = System.getenv("KAFKA_USERNAME");
        String password = System.getenv("KAFKA_PASSWORD");
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + encodedAuth;
        return authHeader;
    }
    public static void produceMessage(String topic, KafkaRecord record) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        KafkaProducerInput input = new KafkaProducerInput();
        input.setRecords(Arrays.asList(record));
        String payload = objectMapper.writeValueAsString(input);
        String kafkaRestUrl = System.getenv("KAFKA_REST_URL") + "/topics/" + topic;
        HttpClient httpClient = HttpClient.newHttpClient();
        String authHeader = getBasicAuth();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(kafkaRestUrl))
                .header("Content-Type", "application/vnd.kafka.json.v2+json")
                .header("Accept", "application/vnd.kafka.v2+json")
                .header("Authorization", authHeader)
                .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        LOG.info("Kafka Response Status: {}", response.statusCode());
        LOG.info("Kafka Response Body: {}", response.body());
    }
}