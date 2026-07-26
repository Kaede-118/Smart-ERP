package com.kaede.erp.service;


import com.kaede.erp.dto.CreatePermissionDTO;
import com.kaede.erp.dto.UpdatePermissionDTO;
import com.kaede.erp.vo.SysPermissionVO;

import java.util.List;


public interface SysPermissionService {


    List<SysPermissionVO> list();


    SysPermissionVO create(CreatePermissionDTO dto);


    SysPermissionVO update(UpdatePermissionDTO dto);


    void delete(Long id);

}
