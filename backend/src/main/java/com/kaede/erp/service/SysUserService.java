package com.kaede.erp.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaede.erp.dto.UserQueryDTO;
import com.kaede.erp.vo.SysUserVO;


public interface SysUserService {


    SysUserVO getUser(Long id);


    Page<SysUserVO> list(UserQueryDTO dto);

}