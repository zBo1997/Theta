package com.momo.theta.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private BigInteger id;

    /**
     * 登陆账号
     */
    @Length (max = 50)
    private String loginName;

    /**
     * 手机号Hash
     */
    private String mobileHash;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态（0:正常 1:冻结 ）
     */
    private Integer status;

    /**
     * 冻结时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime frozenTime;

    /**
     * 用户信息id
     */
    private BigInteger userInfoId;

    /**
     * 用户编号
     */
    private String userCode;

    /**
     * 是否删除(0:未删除  1:删除)
     */
    private Integer deleteFlag;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 修改时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 手机号
     */
    private String phone;
    /**
     * 名称
     */
    @NotBlank (message = "姓名不能为空")
    private String name;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 推送ID
     */
    private String registerId;

    /**
     * 错误码标记
     */
    private String errorCode;
    private String errorParams;


    private String errorPlaceholders;
    /**
     * 验证码value
     */
    @NotBlank ()
    private String verificationValue;
    /**
     * 验证码key
     */
    @NotBlank ()
    private String verificationKey;
    /**
     * 修改密码时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime pwdUpdateTime;
    /**
     * 最后一次登录时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastLoginTime;
    /**
     * 是否获取基本信息
     */
    private Integer isGetBasic;

    /** clientId **/
    private String clientId;


    /**
     * 是否发送密码0 ：发送  1：不发送
     */
    private Integer isSendPwd;
    /**
     * 是否发送邮箱
     */
    private Integer isSendEmail;
    /**
     * key
     */
    private String key;

    /**
     *昵称
     */
    private String nickName;
    /**
     *手机号
     */
    private String mobile;

    /**
     * 全局唯一
     */
    private String thetaId;

    /**
     * 号修改时间
     */
    private LocalDateTime thetaIdUpdateTime;

    /**
     * 注册步骤，空的代表已完成，1：账号密码步骤，2：绑定手机号，3：昵称头像
     */
    private Integer regStep;

    /**
     * 注册国家/地区
     */
    private String countryCode;

    /**
     * 注册-省
     */
    private String provinceCode;

    /**
     * 注册-市
     */
    private String cityCode;

    /**
     * 注册-区
     */
    private String regionCode;
    /**
     * 地区编码
     */
    private String internationalCode;

    /**
     * 注销时间
     */
    private String cancellationTime;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 设备号
     * */
    private String deviceNo;

    private String address;

    /**
     * 审核状态（无需审核 0 ,待审核 1,审核通过 2,审核拒绝 3 ）
     */
    private Integer auditStatus;


    /**
     * 审核失败原因
     */
    private String auditFailureRemarks;


}
