package com.momo.theta.condition;

/**
 * User条件类
 */
public class UserCondition extends BaseCondition {

  private String id;
  private String userId;
  private String userName;
  private String phone;
  private String lanId;
  private String regionId;
  private String createTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

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

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }
}
