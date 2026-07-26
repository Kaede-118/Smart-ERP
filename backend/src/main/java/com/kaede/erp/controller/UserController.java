package com.kaede.erp.controller;

import com.kaede.erp.common.result.Result;
import com.kaede.erp.service.SysUserService;
import com.kaede.erp.vo.SysUserVO;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {


    private final SysUserService sysUserService;


    public UserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }


    @GetMapping("/{id}")
    public Result<SysUserVO> getUser(
            @PathVariable Long id
    ){

        return Result.success(
                sysUserService.getUser(id)
        );
    }
}