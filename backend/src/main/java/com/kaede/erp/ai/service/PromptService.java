package com.kaede.erp.ai.service;


import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;


@Service
public class PromptService {


    public String build(String type, Map<String, Object> context) {

        String template = loadTemplate(type);


        String result = template;

        for (Map.Entry<String, Object> entry : context.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            result = result.replace(placeholder, value);
        }

        return result;
    }


    private String loadTemplate(String type) {

        String path = "prompt/" + type.toLowerCase() + ".md";

        try {
            return Files.readString(
                    new ClassPathResource(path).getFile().toPath(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new RuntimeException("Prompt template not found: " + path, e);
        }
    }

}
