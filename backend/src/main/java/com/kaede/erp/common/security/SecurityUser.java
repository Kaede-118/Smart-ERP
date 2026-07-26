package com.kaede.erp.common.security;


import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;


@Getter
public class SecurityUser extends User {


    private final Long id;

    private final String nickname;

    private final Integer status;

    private final List<String> roleCodes;

    private final List<String> permissionCodes;


    public SecurityUser(
            Long id,
            String username,
            String password,
            String nickname,
            Integer status,
            List<String> roleCodes,
            List<String> permissionCodes
    ){

        super(
                username,
                password,
                permissionCodes.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );

        this.id = id;
        this.nickname = nickname;
        this.status = status;
        this.roleCodes = roleCodes;
        this.permissionCodes = permissionCodes;

    }

}
