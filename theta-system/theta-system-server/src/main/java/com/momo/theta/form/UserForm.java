package com.momo.theta.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserForm {

    private Long id;

    @NotBlank (message = "用户名不能为空")
    private String username;

    @NotBlank (message = "昵称不能为空")
    private String nickname;

    @Pattern (regexp = "^$|^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "手机号码格式不正确")
    private String mobile;

    private Integer gender;

    private String avatar;

    private String email;

    private Integer status;
}