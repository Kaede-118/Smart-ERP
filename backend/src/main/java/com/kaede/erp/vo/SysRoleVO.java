package com.kaede.erp.vo;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class SysRoleVO {


    private Long id;


    private String roleName;


    private String roleCode;


    private String description;


    private LocalDateTime createTime;

}
