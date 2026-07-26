package com.kaede.erp.ai.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaede.erp.ai.client.AiClient;
import com.kaede.erp.ai.config.AiConfig;
import com.kaede.erp.ai.dto.AiReportRequest;
import com.kaede.erp.ai.dto.AiReportResponse;
import com.kaede.erp.common.context.UserContext;
import com.kaede.erp.entity.AiReport;
import com.kaede.erp.mapper.AiReportMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class AiService {


    private final ContextService contextService;

    private final PromptService promptService;

    private final AiClient aiClient;

    private final AiConfig aiConfig;

    private final AiReportMapper reportMapper;

    private final ObjectMapper objectMapper;


    public AiService(
            ContextService contextService,
            PromptService promptService,
            AiClient aiClient,
            AiConfig aiConfig,
            AiReportMapper reportMapper
    ) {
        this.contextService = contextService;
        this.promptService = promptService;
        this.aiClient = aiClient;
        this.aiConfig = aiConfig;
        this.reportMapper = reportMapper;
        this.objectMapper = new ObjectMapper();
    }


    public AiReportResponse generate(AiReportRequest request) {

        String typeName = request.getType().name(); // SALES, INVENTORY, OVERVIEW
        String range = request.getRange();


        Map<String, Object> context = switch (request.getType()) {
            case SALES -> contextService.buildSalesContext(range);
            case INVENTORY -> contextService.buildInventoryContext();
            case OVERVIEW -> contextService.buildOverviewContext();
        };


        context.putIfAbsent("range", range);


        String prompt = promptService.build(typeName, context);


        long start = System.currentTimeMillis();
        String llmResponse = aiClient.chat(prompt);
        long elapsed = System.currentTimeMillis() - start;


        AiReport entity = new AiReport();
        entity.setType(typeName);
        entity.setPrompt(prompt);
        entity.setModel(aiConfig.getModel());
        entity.setElapsedMs(elapsed);
        entity.setCreatedBy(UserContext.getUserId());


        try {

            @SuppressWarnings("unchecked")
            Map<String, Object> parsed =
                    objectMapper.readValue(llmResponse, Map.class);

            entity.setTitle((String) parsed.getOrDefault("title", typeName + "报告"));
            entity.setSummary((String) parsed.getOrDefault("summary", ""));
            entity.setContent((String) parsed.getOrDefault("content", llmResponse));

        } catch (Exception e) {

            entity.setTitle(typeName + "分析报告");
            entity.setContent(llmResponse);
        }


        reportMapper.insert(entity);


        return toResponse(entity);
    }


    public List<AiReportResponse> listReports() {

        return reportMapper.selectList(null)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public AiReportResponse getReport(Long id) {

        AiReport report = reportMapper.selectById(id);

        if (report == null) {
            return null;
        }

        return toResponse(report);
    }


    private AiReportResponse toResponse(AiReport entity) {

        AiReportResponse resp = new AiReportResponse();

        resp.setId(entity.getId());
        resp.setType(entity.getType());
        resp.setTitle(entity.getTitle());
        resp.setSummary(entity.getSummary());
        resp.setContent(entity.getContent());
        resp.setModel(entity.getModel());
        resp.setInputTokens(entity.getInputTokens());
        resp.setOutputTokens(entity.getOutputTokens());
        resp.setElapsedMs(entity.getElapsedMs());
        resp.setCreatedTime(entity.getCreatedTime());

        return resp;
    }

}
