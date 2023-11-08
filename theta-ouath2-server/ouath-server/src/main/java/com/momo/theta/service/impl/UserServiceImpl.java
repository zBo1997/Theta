package com.momo.theta.service.impl;

import com.momo.theta.entity.UserInfo;
import com.momo.theta.service.SystemUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private SystemUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = sysUserService.getUserByUserName(username);
        log.warn("user: " + userInfo.getUsername());
        if (userInfo == null){
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        List<GrantedAuthority> authorities=new ArrayList<>();
        //获取用户权限
        List<String> permissions = userInfo.getPermissions();
        permissions.forEach(permission->{
            authorities.add(new SimpleGrantedAuthority("ROLE_" + permission));
        });

        // 这里一定要基于 BCrypt 加密,不然会不通过
        UserDetails user = new User(userInfo.getUsername(), new BCryptPasswordEncoder().encode(userInfo.getPassword()), authorities);
        log.warn(user.toString());
        return user;
    }

}
