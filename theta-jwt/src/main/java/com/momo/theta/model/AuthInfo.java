package com.momo.theta.model;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户认证的相关信息
 *
 * @author zhubo
 */
@Data
@Accessors(chain = true)
public class AuthInfo {

    /**
     * 令牌
     */
    private String token;

    /**
     * 令牌类型
     */
    private String tokenType;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 用户名
     */
    private String name;

    /**
     * 账户
     */
    private String account;

    /**
     * 头像
     */
    private Long avatarId;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 有效期
     */
    private long expire;

    /**
     * 到期时间
     */
    private LocalDateTime expiration;

    /**
     * 有效期
     */
    private Long expireMillis;

}
