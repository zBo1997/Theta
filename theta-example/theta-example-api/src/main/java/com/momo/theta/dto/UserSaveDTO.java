package com.momo.theta.dto;

import com.momo.theta.enums.Sex;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 实体类
 * 用户
 * </p>
 *
 * @author zhubo
 * @since 2021-04-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
public class UserSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账号
     */
    @NotEmpty(message = "账号不能为空")
    @Size(max = 30, message = "账号长度不能超过30")
    private String account;
    /**
     * 姓名
     */
    @NotEmpty(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50")
    private String name;
    /**
     * 组织
     * #c_org
     *
     * @Echo(api = ORG_ID_CLASS,  beanClass = Org.class)
     */
    private Long orgId;
    /**
     * 岗位
     * #c_station
     *
     * @Echo(api = STATION_ID_CLASS)
     */
    private Long stationId;
    /**
     * 邮箱
     */
    @Size(max = 255, message = "邮箱长度不能超过255")
    private String email;
    /**
     * 手机
     */
    @Size(max = 20, message = "手机长度不能超过20")
    private String mobile;
    /**
     * 性别
     * #Sex{W:女;M:男;N:未知}
     */
    private Sex sex;
    /**
     * 状态
     */
    private Boolean state;

    /**
     * 头像
     */
    @Size(max = 255, message = "头像长度不能超过255")
    private String avatar;

    /**
     * 民族
     */
    @Size(max = 2, message = "民族长度不能超过2")
    private String nation;

    /**
     * 学历
     */
    @Size(max = 2, message = "学历长度不能超过2")
    private String education;

    /**
     * 状态
     */
    @Size(max = 2, message = "职位状态长度不能超过2")
    private String status;

    /**
     * 工作描述
     */
    @Size(max = 255, message = "工作描述长度不能超过255")
    private String workDescribe;

    /**
     * 密码
     */
    @Size(min = 6, max = 64, message = "密码长度不能小于6或超过64")
    private String password;

    /**
     * 机构编号
     */
    private Long createdOrgId;

}
