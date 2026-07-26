package com.kaede.erp.ai.client;


import com.kaede.erp.ai.config.AiConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Component
public class DeepSeekClient implements AiClient {


    private final RestTemplate restTemplate;

    private final AiConfig aiConfig;


    public DeepSeekClient(RestTemplateBuilder builder, AiConfig aiConfig) {
        this.restTemplate = builder.build();
        this.aiConfig = aiConfig;
    }


    @Override
    public String chat(String prompt) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(aiConfig.getApiKey());


        Map<String, Object> requestBody = Map.of(
                "model", aiConfig.getModel(),
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                ),
                "max_tokens", aiConfig.getMaxTokens()
        );


        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                aiConfig.getApiUrl(),
                request,
                Map.class
        );


        List<Map<String, Object>> choices =
                (List<Map<String, Object>>) response.getBody().get("choices");

        if (choices != null && !choices.isEmpty()) {

            Map<String, Object> message =
                    (Map<String, Object>) choices.get(0).get("message");

            return (String) message.get("content");
        }

        return "";
    }

}
