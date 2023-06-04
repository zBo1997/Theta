package com.momo.theta.enums;

import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author zhubo
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Sex {

  /**
   * M="男"
   */
  M("男"),
  /**
   * W="女"
   */
  W("女"),
  /**
   * N="未知"
   */
  N("未知"),
  ;
  private String desc;


  /**
   * 根据当前枚举的name匹配
   */
  public static Sex match(String val, Sex def) {
    return Stream.of(values()).parallel().filter(item -> item.name().equalsIgnoreCase(val))
        .findAny().orElse(def);
  }

  public static Sex get(String val) {
    return match(val, null);
  }

  public String getCode() {
    return this.name();
  }

}
