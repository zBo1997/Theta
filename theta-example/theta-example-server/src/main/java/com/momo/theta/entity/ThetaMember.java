package com.momo.theta.entity;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author author
 * @since 2023-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("theta_member")
public class ThetaMember implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 账号
     */
    private String account;

    /**
     * 姓名
     */
    @TableField(value = "name", condition = LIKE)
    private String name;

    /**
     * 组织
     */
    private Long orgId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 性别
     */
    private String sex;

    /**
     * 状态
     */
    private byte state;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 民族
     */
    private String nation;

    /**
     * 学历
     */
    private String education;

    /**
     * 最后一次输错密码时间
     */
    private LocalDateTime passwordErrorLastTime;

    /**
     * 密码错误次数
     */
    private Integer passwordErrorNum;

    /**
     * 密码过期时间
     */
    private LocalDateTime passwordExpireTime;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码盐
     */
    private String salt;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 创建人id
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人id
     */
    private Long updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
