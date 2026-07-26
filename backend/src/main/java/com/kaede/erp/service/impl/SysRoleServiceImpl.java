package com.kaede.erp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaede.erp.common.constant.ResultCode;
import com.kaede.erp.common.converter.PermissionConverter;
import com.kaede.erp.common.converter.RoleConverter;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.dto.CreateRoleDTO;
import com.kaede.erp.dto.RoleQueryDTO;
import com.kaede.erp.dto.UpdateRoleDTO;
import com.kaede.erp.entity.SysPermission;
import com.kaede.erp.entity.SysRole;
import com.kaede.erp.mapper.RBACMapper;
import com.kaede.erp.mapper.SysPermissionMapper;
import com.kaede.erp.mapper.SysRoleMapper;
import com.kaede.erp.mapper.SysRolePermissionMapper;
import com.kaede.erp.service.SysRoleService;
import com.kaede.erp.vo.SysPermissionVO;
import com.kaede.erp.vo.SysRoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class SysRoleServiceImpl implements SysRoleService {


    private final SysRoleMapper mapper;

    private final SysRolePermissionMapper rolePermMapper;

    private final SysPermissionMapper permMapper;

    private final RBACMapper rbacMapper;


    public SysRoleServiceImpl(
            SysRoleMapper mapper,
            SysRolePermissionMapper rolePermMapper,
            SysPermissionMapper permMapper,
            RBACMapper rbacMapper
    ) {
        this.mapper = mapper;
        this.rolePermMapper = rolePermMapper;
        this.permMapper = permMapper;
        this.rbacMapper = rbacMapper;
    }


    @Override
    public SysRoleVO getRole(Long id) {

        SysRole role = mapper.selectById(id);

        if (role == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        return RoleConverter.toVO(role);
    }


    @Override
    public Page<SysRoleVO> list(RoleQueryDTO dto) {

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();

        if (dto.getKeyword() != null && !dto.getKeyword().isBlank()) {
            wrapper.like(SysRole::getRoleName, dto.getKeyword())
                    .or()
                    .like(SysRole::getRoleCode, dto.getKeyword());
        }

        Page<SysRole> page = new Page<>(dto.getPage(), dto.getSize());

        Page<SysRole> result = mapper.selectPage(page, wrapper);

        Page<SysRoleVO> voPage = new Page<>();
        voPage.setCurrent(result.getCurrent());
        voPage.setSize(result.getSize());
        voPage.setTotal(result.getTotal());
        voPage.setRecords(
                result.getRecords().stream()
                        .map(RoleConverter::toVO)
                        .toList()
        );

        return voPage;
    }


    @Override
    public SysRoleVO create(CreateRoleDTO dto) {

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleCode, dto.getRoleCode());

        if (mapper.selectOne(wrapper) != null) {
            throw new BusinessException(40000, "角色编码已存在");
        }

        SysRole role = new SysRole();
        role.setRoleName(dto.getRoleName());
        role.setRoleCode(dto.getRoleCode());
        role.setDescription(dto.getDescription());

        mapper.insert(role);

        return RoleConverter.toVO(role);
    }


    @Override
    public SysRoleVO update(UpdateRoleDTO dto) {

        SysRole role = mapper.selectById(dto.getId());

        if (role == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());

        mapper.updateById(role);

        return RoleConverter.toVO(role);
    }


    @Override
    public void delete(Long id) {

        SysRole role = mapper.selectById(id);

        if (role == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        mapper.deleteById(id);

    }


    @Override
    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {


        SysRole role = mapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }


        List<Long> distinctIds = permissionIds.stream()
                .distinct()
                .toList();


        if (!distinctIds.isEmpty()) {

            List<SysPermission> existingPerms =
                    permMapper.selectBatchIds(distinctIds);

            if (existingPerms.size() != distinctIds.size()) {

                List<Long> validIds = existingPerms
                        .stream()
                        .map(SysPermission::getId)
                        .toList();

                List<Long> invalidIds = distinctIds
                        .stream()
                        .filter(id -> !validIds.contains(id))
                        .toList();

                throw new BusinessException(
                        40000,
                        "权限ID不存在: " + invalidIds
                );
            }
        }


        rolePermMapper.deleteByRoleId(roleId);


        if (!distinctIds.isEmpty()) {
            rolePermMapper.batchInsert(roleId, distinctIds);
        }

    }


    @Override
    public List<SysPermissionVO> getRolePermissions(Long roleId) {

        SysRole role = mapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        List<SysPermission> perms =
                rbacMapper.selectPermissionsByRoleId(roleId);

        return perms.stream()
                .map(PermissionConverter::toVO)
                .toList();
    }

}
