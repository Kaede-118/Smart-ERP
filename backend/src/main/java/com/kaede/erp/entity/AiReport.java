package com.kaede.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("ai_report")
public class AiReport {


    private Long id;


    private String type;


    private String title;


    private String summary;


    private String content;


    private String prompt;


    private String model;


    private Integer inputTokens;


    private Integer outputTokens;


    private Long elapsedMs;


    private Long createdBy;


    private LocalDateTime createdTime;

}
