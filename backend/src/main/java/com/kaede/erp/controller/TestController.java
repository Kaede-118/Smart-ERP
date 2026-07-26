package com.kaede.erp.controller;


import com.kaede.erp.common.result.Result;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping
    public Result<String> test(){

        return Result.success("ERP System Running");

    }
}