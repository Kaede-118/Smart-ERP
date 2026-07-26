package com.kaede.erp.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaede.erp.dto.CreateUserDTO;
import com.kaede.erp.dto.ResetPasswordDTO;
import com.kaede.erp.dto.UpdateUserDTO;
import com.kaede.erp.dto.UserQueryDTO;
import com.kaede.erp.vo.SysUserVO;


public interface SysUserService {


    SysUserVO getUser(Long id);


    Page<SysUserVO> list(UserQueryDTO dto);


    SysUserVO create(CreateUserDTO dto);


    SysUserVO update(UpdateUserDTO dto);


    void delete(Long id);


    void resetPassword(Long id, ResetPasswordDTO dto);

}