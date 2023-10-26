package com.momo.theta.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * @author zhubo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("theta_user_info")
public class Info extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @TableField("account_id")
    private BigInteger accountId;
    /**
     * 邮箱地址
     */
    @TableField("email")
    private String email;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 证件类型 1：身份证   2 护照
     */
    @TableField("id_type")
    private Integer idType;

    /**
     * 注册来源1 app 2官网
     */
    @TableField("source")
    private Integer source;

    /**
     * 证件号码
     */
    @TableField("id_no")
    private String idNo;

    /**
     * 证件号码hash
     */
    @TableField("id_no_hash")
    private String idNoHash;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 手机号hash
     */
    @TableField("mobile_hash")
    private String mobileHash;

    /**
     * 通讯地址
     */
    @TableField("address")
    private String address;
    /**
     * 生日
     */
    @TableField("birthday")
    private String birthday;
    /**
     * 性别(1男 2女)
     */
    @TableField("gender")
    private Integer gender;

    @TableField("nick_name")
    private String nickName;

    /**
     * 创建人手机区号
     */
    @TableField("international_code")
    private String internationalCode;

    /**
     * 默认公司名片
     */
    @TableField("default_vcard_company_code")
    private String  defaultVcardCompanyCode;


    @TableField("is_real")
    private Integer isReal;

    /**
     * 是否接收招聘消息，1接收；0不接收
     */
    @TableField("is_receive_recruit_messages")
    private Integer isReceiveRecruitMessages;


    /**
     * 类型 0长期;1某个时间段
     */
    @TableField("disturb_type")
    private Integer disturbType;

    /**
     * 免打扰开始时间
     */
    @TableField("no_disturb_start_time")
    private LocalDateTime noDisturbStartTime;

    /**
     * 免打扰结束时间
     */
    @TableField("no_disturb_end_time")
    private LocalDateTime noDisturbEndTime;
}

