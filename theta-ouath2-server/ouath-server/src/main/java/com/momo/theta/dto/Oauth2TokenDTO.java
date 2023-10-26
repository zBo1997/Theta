package com.momo.theta.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * @author funcas
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Oauth2TokenDTO {
    /**
     * 访问令牌
     */
    private String token;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 访问令牌头前缀
     */
    private String tokenType;
    /**
     * 有效时间（秒）
     */
    private int expiresIn;

    /**
     * 范围
     */
    private Set<String> scope;

    private String userCode;

    private Integer organizeType;

    private String loginName;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 是否租户管理员 0-否 1-是
     */
    @TableField("admin_flag")
    private Integer adminFlag;

    private Integer requireSetPassword;

    /**
     * 注册步骤，空的代表已完成注册，1：账号密码步骤，2：绑定手机号，3：昵称头像
     */
    private Integer regStep;

    /**
     * 注册地区编码（中国大陆：CN）
     */
    private String regCountryNameCode;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 是否是自动注册
     */
    private Boolean autoRegister;

    private Integer tenantType;

    private int frozenType;


    private Integer status;
    /**
     * 冻结时间
     */
    private Long frozenTime;

    private String serviceNumber;

    private String encryptedDeviceNo;

    private String internationalCode;

    private String name;

    private Integer isAuthDevice;


    private Integer isAuthorizationExceedsLimit;

    /**
     * 审核状态（无需审核 0 ,待审核 1,审核通过 2,审核拒绝 3 ）
     */
    private Integer auditStatus;

    /**
     * 审核失败原因
     */
    private String auditFailureRemarks;

}
