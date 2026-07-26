package com.kaede.erp.ai.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class AiReportRequest {


    @NotNull(message = "报告类型不能为空")
    private ReportType type;


    @NotBlank(message = "分析范围不能为空")
    private String range = "MONTH";


    public enum ReportType {
        SALES,
        INVENTORY,
        OVERVIEW
    }

}
