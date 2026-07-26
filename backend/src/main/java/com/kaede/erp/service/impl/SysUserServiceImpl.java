package com.kaede.erp.service.impl;

import com.kaede.erp.common.constant.ResultCode;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.entity.SysUser;
import com.kaede.erp.mapper.SysUserMapper;
import com.kaede.erp.service.SysUserService;
import com.kaede.erp.vo.SysUserVO;
import org.springframework.stereotype.Service;


@Service
public class SysUserServiceImpl implements SysUserService {


    private final SysUserMapper mapper;


    public SysUserServiceImpl(SysUserMapper mapper){
        this.mapper = mapper;
    }


    @Override
    public SysUserVO getUser(Long id) {

        SysUser user = mapper.selectById(id);

        if (user == null) {

            throw new BusinessException(
                    ResultCode.USER_NOT_FOUND
            );

        }

        SysUserVO vo = new SysUserVO();

        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setStatus(user.getStatus());

        return vo;
    }
}