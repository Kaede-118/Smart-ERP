package com.kaede.erp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaede.erp.common.constant.ResultCode;
import com.kaede.erp.common.converter.UserConverter;
import com.kaede.erp.common.exception.BusinessException;
import com.kaede.erp.dto.CreateUserDTO;
import com.kaede.erp.dto.ResetPasswordDTO;
import com.kaede.erp.dto.UpdateUserDTO;
import com.kaede.erp.dto.UserQueryDTO;
import com.kaede.erp.entity.SysUser;
import com.kaede.erp.mapper.SysUserMapper;
import com.kaede.erp.service.SysUserService;
import com.kaede.erp.vo.SysUserVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class SysUserServiceImpl implements SysUserService {


    private final SysUserMapper mapper;

    private final PasswordEncoder passwordEncoder;


    public SysUserServiceImpl(SysUserMapper mapper, PasswordEncoder passwordEncoder) {
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<SysUserVO> list(UserQueryDTO dto) {


        Page<SysUser> page =
                new Page<>(
                        dto.getPage(),
                        dto.getSize()
                );


        Page<SysUser> result =
                mapper.selectPage(
                        page,
                        null
                );


        Page<SysUserVO> voPage =
                new Page<>();


        voPage.setCurrent(result.getCurrent());
        voPage.setSize(result.getSize());
        voPage.setTotal(result.getTotal());


        voPage.setRecords(
                result.getRecords()
                        .stream()
                        .map(UserConverter::toVO)
                        .toList()
        );


        return voPage;
    }

    @Override
    public SysUserVO getUser(Long id) {

        SysUser user = mapper.selectById(id);

        if (user == null) {

            throw new BusinessException(
                    ResultCode.USER_NOT_FOUND
            );

        }

        return UserConverter.toVO(user);
    }

    @Override
    public SysUserVO create(CreateUserDTO dto) {


        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, dto.getUsername());

        SysUser exist = mapper.selectOne(wrapper);

        if (exist != null) {
            throw new BusinessException(40000, "用户名已存在");
        }


        SysUser user = new SysUser();

        user.setUsername(dto.getUsername());

        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );

        user.setNickname(dto.getNickname());

        user.setStatus(dto.getStatus());


        mapper.insert(user);


        return UserConverter.toVO(user);
    }

    @Override
    public SysUserVO update(UpdateUserDTO dto) {


        SysUser user = mapper.selectById(dto.getId());

        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }


        if (!user.getUsername().equals(dto.getUsername())) {

            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getUsername, dto.getUsername());

            SysUser exist = mapper.selectOne(wrapper);

            if (exist != null) {
                throw new BusinessException(40000, "用户名已存在");
            }

            user.setUsername(dto.getUsername());
        }


        user.setNickname(dto.getNickname());

        user.setStatus(dto.getStatus());


        mapper.updateById(user);


        return UserConverter.toVO(user);
    }

    @Override
    public void delete(Long id) {


        SysUser user = mapper.selectById(id);

        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }


        mapper.deleteById(id);

    }

    @Override
    public void resetPassword(Long id, ResetPasswordDTO dto) {


        SysUser user = mapper.selectById(id);

        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }


        String newPassword = dto.getNewPassword();

        if (newPassword == null || newPassword.isBlank()) {
            newPassword = "123456";
        }


        user.setPassword(
                passwordEncoder.encode(newPassword)
        );


        mapper.updateById(user);

    }

}