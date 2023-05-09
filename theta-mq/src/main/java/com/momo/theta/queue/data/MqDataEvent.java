package com.momo.theta.queue.data;

import lombok.Getter;

/**
 * 实体监听器
 */
@Getter
public class MqDataEvent {

  private MqData value;

  public void set(MqData value) {
    this.value = value;
  }

  public void clear() {
    this.value = null;
  }

}
