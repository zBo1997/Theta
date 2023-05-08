package com.momo.theta.service;

import com.momo.theta.queue.DisruptorQueue;
import com.momo.theta.queue.NormalQueue;
import com.momo.theta.queue.OrderQueue;
import com.momo.theta.queue.data.MqData;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 *
 */
@Slf4j
@Data
public class MsgQueue implements AutoCloseable {

  private final MqProducer mqProducer;
  private NormalQueue normalQueue;
  private OrderQueue orderQueue;

  private String filePath;

  public MsgQueue(MqProducer mqProducer) {
    this.mqProducer = mqProducer;
  }


  /**
   * 统一发送队列
   *
   * @param data 数据
   * @return 返回值
   */
  public boolean publish(MqData data) {

    if (Objects.isNull(data) || StringUtils.isEmpty(data.getTopic()) || StringUtils.isEmpty(
        data.getBody())) {
      return false;
    }

    if (data.isOrderly() && StringUtils.isEmpty(data.getKeys())) {
      return false;
    }

    if (data.isOrderly()) {
      return this.orderQueue.publish(data);
    } else {
      return this.normalQueue.publish(data);
    }

  }

  @Override
  public void close() throws InterruptedException {

    DisruptorQueue.SAVE_FILE_FLAG = true;
    //等待15s，防止系统在处理的业务消息发送队列失败
    int i = 1;
    while (i <= 15) {
      //尝试等待业务消息
      log.info("wait business semaphore on {} second", i);
      TimeUnit.SECONDS.sleep(1);
      i++;
    }
    //将队列置为关闭状态，队列不在接收业务模块传送来的消息
    DisruptorQueue.SHUTDOWN_FLAG = true;

    new Thread(() -> {
      if (Objects.isNull(normalQueue) || Objects.isNull(normalQueue.getDisruptor())) {
        log.debug("normalQueue is shutdown");
      } else {
        this.normalQueue.shutdown();
      }
    }).start();

    new Thread(() -> {
      if (Objects.isNull(orderQueue) || Objects.isNull(normalQueue.getDisruptor())) {
        log.debug("orderQueue is shutdown");
      } else {
        this.orderQueue.shutdown();
      }
    }).start();

  }

  public void init() {
    if (Objects.isNull(normalQueue)) {
      this.normalQueue = new NormalQueue(mqProducer, filePath);
      this.normalQueue.start();
    }
    if (Objects.isNull(orderQueue)) {
      this.orderQueue = new OrderQueue(mqProducer, filePath);
      this.orderQueue.start();
    }
  }


}
