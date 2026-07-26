package com.kaede.erp.common.converter;


import com.kaede.erp.entity.SysRole;
import com.kaede.erp.vo.SysRoleVO;


public final class RoleConverter {


    private RoleConverter() {
    }


    public static SysRoleVO toVO(SysRole role) {

        if (role == null) {
            return null;
        }

        SysRoleVO vo = new SysRoleVO();

        vo.setId(role.getId());
        vo.setRoleName(role.getRoleName());
        vo.setRoleCode(role.getRoleCode());
        vo.setDescription(role.getDescription());
        vo.setCreateTime(role.getCreateTime());

        return vo;
    }

}
