package com.kaede.erp.ai.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "ai")
public class AiConfig {


    private String apiKey;

    private String model = "deepseek-chat";

    private String apiUrl = "https://api.deepseek.com/chat/completions";

    private int maxTokens = 2048;

}
