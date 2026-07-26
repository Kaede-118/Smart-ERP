package com.kaede.erp.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("sys_user")
public class SysUser {

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private Integer status;

}