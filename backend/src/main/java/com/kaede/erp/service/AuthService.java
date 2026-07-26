package com.kaede.erp.service;


import com.kaede.erp.dto.LoginDTO;
import com.kaede.erp.vo.LoginVO;
import com.kaede.erp.vo.SysUserVO;


public interface AuthService {


    LoginVO login(LoginDTO dto);
    SysUserVO getCurrentUser();
}