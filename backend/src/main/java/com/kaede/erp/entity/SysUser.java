package com.kaede.erp.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("sys_user")
public class SysUser {


    private Long id;


    private String username;


    private String password;


    private String nickname;


    private Integer status;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    /**
     * 逻辑删除
     * 0 正常
     * 1 删除
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

}