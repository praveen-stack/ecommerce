package com.ecommerce.prodcatalogservice.configuration;

import org.springframework.data.elasticsearch.support.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

import java.util.Collections;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Autowired
    private Environment environment;

    @Override
    public ClientConfiguration clientConfiguration() {

        String hostAndPort = environment.getProperty("elasticsearch.host.and.port");
        String apiKey = environment.getProperty("elasticsearch.apiKey");
        String apiKeyAuthHeader = "ApiKey " + apiKey;
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, apiKeyAuthHeader);

        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(hostAndPort)
                .usingSsl()
                .withDefaultHeaders(headers)
                .build();
        return clientConfiguration;
    }
}
