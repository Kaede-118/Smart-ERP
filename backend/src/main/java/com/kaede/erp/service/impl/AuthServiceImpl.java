package com.kaede.erp.service.impl;
import lombok.RequiredArgsConstructor;

import com.kaede.erp.common.constant.ResultCode;
import com.kaede.erp.common.context.UserContext;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.common.security.JwtTokenProvider;
import com.kaede.erp.common.security.SecurityUser;
import com.kaede.erp.dto.LoginDTO;
import com.kaede.erp.entity.SysUser;
import com.kaede.erp.mapper.RBACMapper;
import com.kaede.erp.mapper.SysUserMapper;
import com.kaede.erp.service.AuthService;
import com.kaede.erp.vo.LoginVO;
import com.kaede.erp.vo.SysUserVO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final SysUserMapper sysUserMapper;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final RBACMapper rbacMapper;

    @Override
    public SysUserVO getCurrentUser() {
        System.out.println("进入 getCurrentUser");
        Long userId = UserContext.getUserId();
        System.out.println("userId = " + userId);
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }

        SysUser user = sysUserMapper.selectById(userId);
        System.out.println("user = " + user);

        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        SysUserVO vo = new SysUserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setStatus(user.getStatus());

        return vo;
    }





    @Override
    public LoginVO login(LoginDTO dto){


        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                dto.getUsername(),
                                dto.getPassword()
                        )
                );



        SecurityUser user =
                (SecurityUser) authentication.getPrincipal();



        String token =
                jwtTokenProvider.createToken(
                        user.getId(),
                        user.getUsername(),
                        user.getPermissionCodes()
                );



        LoginVO vo = new LoginVO();


        vo.setToken(token);

        vo.setRoles(user.getRoleCodes());

        vo.setPermissions(user.getPermissionCodes());



        SysUserVO userVO =
                new SysUserVO();

        userVO.setId(user.getId());

        userVO.setUsername(
                user.getUsername()
        );

        userVO.setNickname(
                user.getNickname()
        );

        userVO.setStatus(
                user.getStatus()
        );

        vo.setUser(userVO);



        return vo;

    }

}
