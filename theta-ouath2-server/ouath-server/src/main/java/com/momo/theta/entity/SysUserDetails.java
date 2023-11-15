package com.momo.theta.entity;

import cn.hutool.core.collection.CollectionUtil;
import com.momo.theta.dto.UserAuthInfo;
import com.momo.theta.enums.PasswordEncoderTypeEnum;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * 系统用户认证信息
 *
 */
@Data
public class SysUserDetails implements UserDetails {

    /**
     * 扩展字段：用户ID
     */
    private Long userId;

    /**
     * 扩展字段：部门ID
     */
    private Long deptId;

    /**
     * 用户角色数据权限集合
     */
    private Integer dataScope;

    /**
     * 默认字段
     */
    private String username;
    private String password;
    private Boolean enabled;
    private Collection<SimpleGrantedAuthority> authorities;

    private Set<String> perms;

    /**
     * 系统管理用户
     */
   public SysUserDetails(UserAuthInfo user) {
       this.setUserId(user.getUserId());
       this.setUsername(user.getUsername());
       this.setDeptId(user.getDeptId());
       this.setDataScope(user.getDataScope());
       this.setPassword(PasswordEncoderTypeEnum.BCRYPT.getPrefix() + user.getPassword());
       this.setEnabled("ENABLE".equals(user.getStatus()));
       if (CollectionUtil.isNotEmpty(user.getRoles())) {
           authorities = user.getRoles().stream()
                   .map(SimpleGrantedAuthority::new)
                   .collect(Collectors.toSet());
       }
       this.setPerms(user.getPerms());
   }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}