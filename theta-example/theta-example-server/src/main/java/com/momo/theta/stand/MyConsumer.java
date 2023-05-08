package com.momo.theta.stand;

import com.momo.theta.queue.NormalQueue;
import java.util.Calendar;

public class MyConsumer extends NormalQueue.ADisruptorConsumer<String> {

  private String name;

  public MyConsumer(String name) {
    this.name = name;
  }

  public void consume(String data) {
    System.out.println(now() + this.name + "：拿到队列中的数据：" + data);
    //等待1秒钟
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  // 获取当前时间（分:秒）
  public String now() {
    Calendar now = Calendar.getInstance();
    return "[" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND) + "] ";
  }
}