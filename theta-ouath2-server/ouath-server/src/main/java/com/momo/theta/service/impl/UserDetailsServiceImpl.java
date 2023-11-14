package com.momo.theta.service.impl;

import javax.security.auth.login.AccountExpiredException;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service("sysUserDetailsService")
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl  implements UserDetailsService {

    //private final UserFeignClient userFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) {
//        UserAuthInfo userAuthInfo = userFeignClient.getUserAuthInfo(username);
//
//        Assert.isTrue(userAuthInfo!= null,
//            "用户不存在");
//
//        if (!StatusEnum.ENABLE.getValue().equals(userAuthInfo.getStatus()) ) {
//            throw new DisabledException("该账户已被禁用!");
//        }
//
//        SysUserDetails userDetails = new SysUserDetails(userAuthInfo);
//        return userDetails;
        return null;
    }

}