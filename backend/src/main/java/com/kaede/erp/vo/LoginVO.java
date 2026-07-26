package com.kaede.erp.vo;
import com.kaede.erp.vo.SysUserVO;

import lombok.Data;


@Data
public class LoginVO {


    /**
     * JWT Token
     */
    private String token;


    /**
     * 用户信息
     */
    private SysUserVO user;

}