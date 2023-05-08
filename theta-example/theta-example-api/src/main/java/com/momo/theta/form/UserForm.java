package com.momo.theta.form;

import javax.validation.constraints.NotBlank;

public class UserForm {

  @NotBlank(message = "用户名称不能为空")
  private String userName;

  @NotBlank(message = "电话号码不能为空")
  private String phone;

  @NotBlank(message = "lan编号不能为空")
  private String lanId;

  @NotBlank(message = "区域编号不能为空")
  private String regionId;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getLanId() {
    return lanId;
  }

  public void setLanId(String lanId) {
    this.lanId = lanId;
  }

  public String getRegionId() {
    return regionId;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }
}
