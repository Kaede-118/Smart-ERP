package com.kaede.erp.ai.controller;


import com.kaede.erp.ai.dto.AiReportRequest;
import com.kaede.erp.ai.dto.AiReportResponse;
import com.kaede.erp.ai.service.AiService;
import com.kaede.erp.common.result.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/ai")
public class AiController {


    private final AiService aiService;


    public AiController(AiService aiService) {
        this.aiService = aiService;
    }


    @PostMapping("/reports")
    public Result<AiReportResponse> generate(
            @Valid @RequestBody AiReportRequest request
    ) {

        return Result.success(
                aiService.generate(request)
        );

    }


    @GetMapping("/reports")
    public Result<List<AiReportResponse>> list() {

        return Result.success(
                aiService.listReports()
        );

    }


    @GetMapping("/reports/{id}")
    public Result<AiReportResponse> getReport(
            @PathVariable Long id
    ) {

        AiReportResponse report = aiService.getReport(id);

        if (report == null) {
            return Result.error(40400, "报告不存在");
        }

        return Result.success(report);

    }

}
