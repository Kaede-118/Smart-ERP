package com.kaede.erp.ai.dto;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class AiReportResponse {


    private Long id;

    private String type;

    private String title;

    private String summary;

    private String content;

    private String model;

    private Integer inputTokens;

    private Integer outputTokens;

    private Long elapsedMs;

    private LocalDateTime createdTime;

}
