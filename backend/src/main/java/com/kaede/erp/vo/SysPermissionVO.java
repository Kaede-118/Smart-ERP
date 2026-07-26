package com.kaede.erp.vo;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class SysPermissionVO {


    private Long id;


    private String name;


    private String code;


    private String type;


    private Long parentId;


    private LocalDateTime createTime;

}
