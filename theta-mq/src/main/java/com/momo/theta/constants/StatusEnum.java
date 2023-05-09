package com.momo.theta.constants;

import lombok.Getter;

@Getter
public enum StatusEnum {
  ENABLE("true", "有效"),
  DISABLE("false", "无效");

  private String code;
  private String msg;

  StatusEnum(String code, String msg) {
    this.code = code;
    this.msg = msg;
  }
}
