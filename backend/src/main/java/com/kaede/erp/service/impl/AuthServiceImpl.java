package com.kaede.erp.service.impl;


import com.kaede.erp.common.security.JwtTokenProvider;
import com.kaede.erp.common.security.SecurityUser;
import com.kaede.erp.dto.LoginDTO;
import com.kaede.erp.service.AuthService;
import com.kaede.erp.vo.LoginVO;
import com.kaede.erp.vo.SysUserVO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {


    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;



    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider
    ){

        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;

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
                        user.getUsername()
                );



        LoginVO vo = new LoginVO();


        vo.setToken(token);



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