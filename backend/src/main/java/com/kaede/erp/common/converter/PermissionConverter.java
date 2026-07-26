package com.kaede.erp.common.converter;


import com.kaede.erp.entity.SysPermission;
import com.kaede.erp.vo.SysPermissionVO;


public final class PermissionConverter {


    private PermissionConverter() {
    }


    public static SysPermissionVO toVO(SysPermission permission) {

        if (permission == null) {
            return null;
        }

        SysPermissionVO vo = new SysPermissionVO();

        vo.setId(permission.getId());
        vo.setName(permission.getName());
        vo.setCode(permission.getCode());
        vo.setType(permission.getType());
        vo.setParentId(permission.getParentId());
        vo.setCreateTime(permission.getCreateTime());

        return vo;
    }

}
