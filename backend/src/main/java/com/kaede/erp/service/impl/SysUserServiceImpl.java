package com.kaede.erp.service.impl;


import com.kaede.erp.entity.SysUser;
import com.kaede.erp.mapper.SysUserMapper;
import com.kaede.erp.service.SysUserService;
import org.springframework.stereotype.Service;


@Service
public class SysUserServiceImpl implements SysUserService {


    private final SysUserMapper mapper;


    public SysUserServiceImpl(SysUserMapper mapper){
        this.mapper = mapper;
    }


    @Override
    public SysUser getUser(Long id){

        return mapper.selectById(id);

    }
}