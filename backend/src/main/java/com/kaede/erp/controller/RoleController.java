package com.kaede.erp.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaede.erp.common.result.Result;
import com.kaede.erp.dto.CreateRoleDTO;
import com.kaede.erp.dto.RoleQueryDTO;
import com.kaede.erp.dto.UpdateRoleDTO;
import com.kaede.erp.service.SysRoleService;
import com.kaede.erp.vo.SysRoleVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/roles")
public class RoleController {


    private final SysRoleService sysRoleService;


    public RoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }


    @PostMapping
    public Result<SysRoleVO> create(
            @Valid @RequestBody CreateRoleDTO dto
    ) {

        return Result.success(
                sysRoleService.create(dto)
        );

    }


    @GetMapping
    public Result<Page<SysRoleVO>> list(
            RoleQueryDTO dto
    ) {

        return Result.success(
                sysRoleService.list(dto)
        );

    }


    @GetMapping("/{id}")
    public Result<SysRoleVO> getRole(
            @PathVariable Long id
    ) {

        return Result.success(
                sysRoleService.getRole(id)
        );

    }


    @PutMapping("/{id}")
    public Result<SysRoleVO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleDTO dto
    ) {

        dto.setId(id);

        return Result.success(
                sysRoleService.update(dto)
        );

    }


    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @PathVariable Long id
    ) {

        sysRoleService.delete(id);

        return Result.success();

    }

}
