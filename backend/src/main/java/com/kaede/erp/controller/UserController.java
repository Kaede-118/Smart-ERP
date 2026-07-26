package com.kaede.erp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaede.erp.common.result.Result;
import com.kaede.erp.dto.CreateUserDTO;
import com.kaede.erp.dto.ResetPasswordDTO;
import com.kaede.erp.dto.UpdateUserDTO;
import com.kaede.erp.dto.UserQueryDTO;
import com.kaede.erp.service.SysUserService;
import com.kaede.erp.vo.SysUserVO;
import jakarta.validation.Valid;
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
    ) {

        return Result.success(
                sysUserService.list(dto)
        );

    }

    @GetMapping("/detail/{id}")
    public Result<SysUserVO> getUser(
            @PathVariable Long id
    ) {
        return Result.success(
                sysUserService.getUser(id)
        );
    }

    @PostMapping
    public Result<SysUserVO> create(
            @Valid @RequestBody CreateUserDTO dto
    ) {

        System.out.println("进入 UserController.create");

        return Result.success(
                sysUserService.create(dto)
        );

    }

    @PutMapping("/{id}")
    public Result<SysUserVO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserDTO dto
    ) {

        System.out.println("进入 UserController.update");

        dto.setId(id);

        return Result.success(
                sysUserService.update(dto)
        );

    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @PathVariable Long id
    ) {

        System.out.println("进入 UserController.delete");

        sysUserService.delete(id);

        return Result.success();

    }

    @PutMapping("/{id}/reset-password")
    public Result<Void> resetPassword(
            @PathVariable Long id,
            @RequestBody ResetPasswordDTO dto
    ) {

        System.out.println("进入 UserController.resetPassword");

        sysUserService.resetPassword(id, dto);

        return Result.success();

    }

}