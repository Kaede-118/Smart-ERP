package com.kaede.erp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaede.erp.common.result.Result;
import com.kaede.erp.dto.UserQueryDTO;
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

    @PostMapping("/list")
    public Result<Page<SysUserVO>> list(
            @RequestBody UserQueryDTO dto
    ){

        return Result.success(
                sysUserService.list(dto)
        );

    }
    @GetMapping("/detail/{id}")
    public Result<SysUserVO> getUser(
            @PathVariable Long id
    ){
        return Result.success(
                sysUserService.getUser(id)
        );
    }
}