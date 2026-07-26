package com.kaede.erp.common.security;


import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;


@Getter
public class SecurityUser extends User {


    private final Long id;

    private final String nickname;

    private final Integer status;


    public SecurityUser(
            Long id,
            String username,
            String password,
            String nickname,
            Integer status
    ){

        super(
                username,
                password,
                AuthorityUtils.createAuthorityList(
                        "ROLE_USER"
                )
        );


        this.id = id;
        this.nickname = nickname;
        this.status = status;

    }

}