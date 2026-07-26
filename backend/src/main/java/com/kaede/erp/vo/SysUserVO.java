package com.kaede.erp.vo;

import lombok.Data;

@Data
public class SysUserVO {

    private Long id;

    private String username;

    private String nickname;

    private Integer status;
}