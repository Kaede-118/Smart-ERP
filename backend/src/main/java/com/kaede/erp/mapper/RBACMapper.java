package com.kaede.erp.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface RBACMapper {


    @Select("SELECT r.role_code FROM sys_user u " +
            "JOIN sys_user_role ur ON u.id = ur.user_id " +
            "JOIN sys_role r ON ur.role_id = r.id " +
            "WHERE u.id = #{userId} AND u.deleted = 0 AND r.deleted = 0")
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);


    @Select("SELECT DISTINCT p.code FROM sys_user u " +
            "JOIN sys_user_role ur ON u.id = ur.user_id " +
            "JOIN sys_role r ON ur.role_id = r.id " +
            "JOIN sys_role_permission rp ON r.id = rp.role_id " +
            "JOIN sys_permission p ON rp.permission_id = p.id " +
            "WHERE u.id = #{userId} AND u.deleted = 0 AND r.deleted = 0 AND p.deleted = 0")
    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);

}
