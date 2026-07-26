package com.kaede.erp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaede.erp.common.constant.ResultCode;
import com.kaede.erp.common.converter.PermissionConverter;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.dto.CreatePermissionDTO;
import com.kaede.erp.dto.UpdatePermissionDTO;
import com.kaede.erp.entity.SysPermission;
import com.kaede.erp.mapper.SysPermissionMapper;
import com.kaede.erp.service.SysPermissionService;
import com.kaede.erp.vo.SysPermissionVO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SysPermissionServiceImpl implements SysPermissionService {


    private final SysPermissionMapper mapper;


    public SysPermissionServiceImpl(SysPermissionMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public List<SysPermissionVO> list() {

        List<SysPermission> list = mapper.selectList(null);

        return list.stream()
                .map(PermissionConverter::toVO)
                .toList();
    }


    @Override
    public SysPermissionVO create(CreatePermissionDTO dto) {

        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysPermission::getCode, dto.getCode());

        if (mapper.selectOne(wrapper) != null) {
            throw new BusinessException(40000, "权限编码已存在");
        }

        SysPermission permission = new SysPermission();
        permission.setName(dto.getName());
        permission.setCode(dto.getCode());
        permission.setType(dto.getType());
        permission.setParentId(dto.getParentId());

        mapper.insert(permission);

        return PermissionConverter.toVO(permission);
    }


    @Override
    public SysPermissionVO update(UpdatePermissionDTO dto) {

        SysPermission permission = mapper.selectById(dto.getId());

        if (permission == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        permission.setName(dto.getName());
        permission.setType(dto.getType());
        permission.setParentId(dto.getParentId());

        mapper.updateById(permission);

        return PermissionConverter.toVO(permission);
    }


    @Override
    public void delete(Long id) {

        SysPermission permission = mapper.selectById(id);

        if (permission == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        mapper.deleteById(id);

    }

}
