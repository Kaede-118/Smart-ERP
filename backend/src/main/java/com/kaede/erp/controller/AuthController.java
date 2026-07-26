package com.kaede.erp.controller;


import com.kaede.erp.common.result.Result;
import com.kaede.erp.dto.LoginDTO;
import com.kaede.erp.service.AuthService;
import com.kaede.erp.vo.LoginVO;
import com.kaede.erp.vo.SysUserVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;


    public AuthController(AuthService authService){
        this.authService = authService;
    }



    @PostMapping("/login")
    public Result<LoginVO> login(
            @Valid @RequestBody LoginDTO dto
    ){

        System.out.println("进入 AuthController.login");

        Result<LoginVO> result = Result.success(
                authService.login(dto)
        );

        System.out.println(result);

        return result;
    }
    @GetMapping("/me")
    public Result<SysUserVO> me() {
        System.out.println("进入 me");
        return Result.success(authService.getCurrentUser());
    }
}