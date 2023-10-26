package com.momo.theta.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.momo.theta.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode (callSuper = false)
@Builder
@ToString
@TableName ("theta_user_account")
public class Account extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 登陆账号
     */
    @TableField ("login_name")
    private String loginName;
    /**
     * 密码
     */
    @TableField ("password")
    private String password;
    /**
     * 临时密码
     */
    @TableField (value = "tem_password", insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String temPassword;
    /**
     * 状态（0:正常 1:冻结  ）
     */
    @TableField ("status")
    private Integer status;

    /**
     * 审核状态（无需审核 0 ,待审核 1,审核通过 2,审核拒绝 3 ）
     */
    @TableField ("audit_status")
    private Integer auditStatus;

    /**
     * 审核失败原因
     */
    @TableField ("audit_failure_remarks")
    private String auditFailureRemarks;
    /**
     * 冻结时间
     */
    @TableField ("frozen_time")
    private LocalDateTime frozenTime;

    /**
     * 用户编号
     */
    @TableField ("user_code")
    private String userCode;
    /**
     * 组织类型（0：无组织用户，1:有组织用户）默认1
     */
    @TableField ("organize_type")
    private Integer organizeType;
    /**
     * 是否租户管理员 0-否 1-是
     */
    @TableField ("admin_flag")
    private Integer adminFlag;

    /**
     * 修改密码时间
     */
    @TableField ("pwd_update_time")
    private LocalDateTime pwdUpdateTime;
    /**
     * 最后一次登录时间
     */
    @TableField ("last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 全局唯一
     */
    @TableField ("theta_id")
    private String thetaId;

    /**
     * 修改时间
     */
    @TableField ("theta_id_update_time")
    private LocalDateTime thetaIdUpdateTime;

    /**
     * 注册步骤，-1的代表已完成，1：账号密码步骤，2：绑定手机号，3：昵称头像
     */
    @TableField ("reg_step")
    private Integer regStep;

    /**
     * 注册国家/地区
     */
    @TableField ("country_code")
    private String countryCode;

    /**
     * 注册-省
     */
    @TableField ("province_code")
    private String provinceCode;

    /**
     * 注册-市
     */
    @TableField ("city_code")
    private String cityCode;

    /**
     * 注册-区
     */
    @TableField ("region_code")
    private String regionCode;

    /**
     * 注销时间
     */
    @TableField ("cancellation_time")
    private LocalDateTime cancellationTime;
}
