package com.kaede.erp.controller;


import com.kaede.erp.common.result.Result;
import com.kaede.erp.dto.CreatePermissionDTO;
import com.kaede.erp.dto.UpdatePermissionDTO;
import com.kaede.erp.service.SysPermissionService;
import com.kaede.erp.vo.SysPermissionVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/permissions")
public class PermissionController {


    private final SysPermissionService sysPermissionService;


    public PermissionController(SysPermissionService sysPermissionService) {
        this.sysPermissionService = sysPermissionService;
    }


    @PostMapping
    public Result<SysPermissionVO> create(
            @Valid @RequestBody CreatePermissionDTO dto
    ) {

        return Result.success(
                sysPermissionService.create(dto)
        );

    }


    @GetMapping
    public Result<List<SysPermissionVO>> list() {

        return Result.success(
                sysPermissionService.list()
        );

    }


    @PutMapping("/{id}")
    public Result<SysPermissionVO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePermissionDTO dto
    ) {

        dto.setId(id);

        return Result.success(
                sysPermissionService.update(dto)
        );

    }


    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @PathVariable Long id
    ) {

        sysPermissionService.delete(id);

        return Result.success();

    }

}
