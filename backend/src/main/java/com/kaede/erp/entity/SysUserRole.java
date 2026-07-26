package com.kaede.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("sys_user_role")
public class SysUserRole {


    private Long id;


    private Long userId;


    private Long roleId;

}
