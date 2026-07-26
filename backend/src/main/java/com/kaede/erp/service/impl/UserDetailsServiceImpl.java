package com.kaede.erp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaede.erp.common.security.SecurityUser;
import com.kaede.erp.entity.SysUser;
import com.kaede.erp.mapper.RBACMapper;
import com.kaede.erp.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl
        implements UserDetailsService {


    private final SysUserMapper mapper;

    private final RBACMapper rbacMapper;


    @Override
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {


        SysUser user =
                mapper.selectOne(
                        new LambdaQueryWrapper<SysUser>()
                                .eq(
                                        SysUser::getUsername,
                                        username
                                )
                );


        if (user == null) {

            throw new UsernameNotFoundException(
                    "用户不存在"
            );

        }


        List<String> roleCodes =
                rbacMapper.selectRoleCodesByUserId(user.getId());

        List<String> permissionCodes =
                rbacMapper.selectPermissionCodesByUserId(user.getId());


        return new SecurityUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getNickname(),
                user.getStatus(),
                roleCodes,
                permissionCodes
        );
    }

}
