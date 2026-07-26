package com.kaede.erp.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaede.erp.dto.CreateRoleDTO;
import com.kaede.erp.dto.RoleQueryDTO;
import com.kaede.erp.dto.UpdateRoleDTO;
import com.kaede.erp.vo.SysRoleVO;


public interface SysRoleService {


    SysRoleVO getRole(Long id);


    Page<SysRoleVO> list(RoleQueryDTO dto);


    SysRoleVO create(CreateRoleDTO dto);


    SysRoleVO update(UpdateRoleDTO dto);


    void delete(Long id);

}
