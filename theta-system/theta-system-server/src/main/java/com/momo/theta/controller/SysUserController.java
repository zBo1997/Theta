package com.momo.theta.controller;

import com.momo.theta.Result;
import com.momo.theta.code.ResultCode;
import com.momo.theta.dto.UserAuthInfo;
import com.momo.theta.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 */
@RestController
@RequestMapping ("/api/v1/users")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    @GetMapping ("/{username}/authInfo")
    public Result<UserAuthInfo> getUserAuthInfo(@PathVariable String username) {
        UserAuthInfo user = userService.getUserAuthInfo(username);
        if (user == null) {
            return Result.fail(ResultCode.USER_NOT_EXIST.getMsg());
        } else {
            return Result.success(user);
        }
    }
}