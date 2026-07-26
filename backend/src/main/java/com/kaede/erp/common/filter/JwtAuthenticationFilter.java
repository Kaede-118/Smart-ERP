package com.kaede.erp.common.filter;


import com.kaede.erp.common.context.UserContext;
import com.kaede.erp.common.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {


    private final JwtTokenProvider jwtTokenProvider;



    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {System.out.println("====== JWT Filter ======");
            System.out.println(request.getRequestURI());

            String header = request.getHeader("Authorization");
            System.out.println("Header = " + header);

            if (header != null && header.startsWith("Bearer ")) {

                String token = header.substring(7);

                boolean valid = jwtTokenProvider.validate(token);
                System.out.println("validate = " + valid);

                if (valid) {

                    Long userId = jwtTokenProvider.getUserId(token);
                    System.out.println("userId = " + userId);

                    List<String> permissions =
                            jwtTokenProvider.getPermissions(token);

                    UserContext.setUserId(userId);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userId,
                                    null,
                                    permissions.stream()
                                            .map(SimpleGrantedAuthority::new)
                                            .toList()
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println("authentication set with " + permissions.size() + " permissions");
                }
            }

            filterChain.doFilter(request, response);

        } finally {

            UserContext.clear();

        }
    }

}
