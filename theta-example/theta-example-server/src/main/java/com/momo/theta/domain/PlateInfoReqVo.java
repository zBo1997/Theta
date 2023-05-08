package com.momo.theta.domain;

import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PlateInfoReqVo implements Serializable {

  /**
   * 图像Base64编码值
   **/
  @NotNull(message = "image cannot be empty")
  private String image;

  /**
   * 搜索条数：默认5
   **/
  @Min(value = 0, message = "limit must greater than or equal to 0")
  private Integer limit = 5;

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }
}
