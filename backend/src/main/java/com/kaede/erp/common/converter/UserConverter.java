package com.kaede.erp.common.converter;

import com.kaede.erp.entity.SysUser;
import com.kaede.erp.vo.SysUserVO;

public final class UserConverter {

    private UserConverter() {
    }

    public static SysUserVO toVO(SysUser user) {

        if (user == null) {
            return null;
        }

        SysUserVO vo = new SysUserVO();

        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setStatus(user.getStatus());

        return vo;
    }
}