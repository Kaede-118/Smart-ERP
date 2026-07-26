package com.kaede.erp.controller;


import com.kaede.erp.common.result.Result;
import com.kaede.erp.entity.SysUser;
import com.kaede.erp.service.SysUserService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test")
public class TestController {


    private final SysUserService sysUserService;


    public TestController(SysUserService sysUserService){
        this.sysUserService = sysUserService;
    }


    @GetMapping
    public Result<String> test(){

        return Result.success("ERP System Running");

    }


    @GetMapping("/user/{id}")
    public Result<SysUser> user(
            @PathVariable Long id
    ){

        return Result.success(
                sysUserService.getUser(id)
        );

    }

}