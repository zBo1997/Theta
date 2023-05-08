package com.momo.theta.queue;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.momo.theta.queue.data.MqData;
import com.momo.theta.queue.data.MqDataEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
public abstract class DisruptorQueue {

  /**
   * disruptor 队列大小 ，默认为 1<<13，即8192
   */
  public static final int DEFAULT_SIZE = 1 << 13;

  /**
   * 是否关闭当前队列
   */
  public static volatile boolean SHUTDOWN_FLAG = false;

  /**
   * 队列消息是否转存本地文件
   */
  public static volatile boolean SAVE_FILE_FLAG = false;

  /**
   * disruptor 环形队列大小
   */
  private int bufferSize;

  /**
   * disruptor 对象
   */
  private Disruptor<MqDataEvent> disruptor;

  /**
   * 启动队列
   */
  public abstract void start();


  /**
   * 关闭队列
   */
  public void shutdown() {
    Disruptor<MqDataEvent> mqDisruptor = getDisruptor();
    try {
      //等待disruptor进行业务消费
      while (mqDisruptor.getRingBuffer().remainingCapacity() != getBufferSize()) {
        TimeUnit.SECONDS.sleep(1);
        log.info("{} 中的消息正在转存文件，剩余 {} 条", this.getClass().getName(), getQueueCount());
      }
      mqDisruptor.shutdown();
      log.info("》》》》》》》》》》{} 中的消息消费完成，队列关闭 《《《《《《《《《《", this.getClass().getName());
    } catch (InterruptedException e) {
      log.error("休眠发生异常", e);
      Thread.currentThread().interrupt();
    }
  }


  /**
   * 发送消息
   *
   * @param data
   * @return
   */
  public boolean publish(MqData data) {
    if (DisruptorQueue.SHUTDOWN_FLAG) {
      log.error("已经收到关闭服务信号，停止提供服务，发送消息内容为data={}", data);
      return false;
    }
    log.info("进入消息发送队列，key={}", data.getKeys());
    if (Objects.nonNull(disruptor.getRingBuffer())
        && disruptor.getRingBuffer().hasAvailableCapacity(1)) {
      disruptor.getRingBuffer().publishEvent((event, sequence, buffer) -> event.set(data), data);
      return true;
    }
    log.info("进入消息发送队列，无空间");
    return false;
  }


  /**
   * 初始化队列
   */
  public void init() {
    if (Objects.nonNull(disruptor)) {
      return;
    }
    if (bufferSize <= 0) {
      bufferSize = DisruptorQueue.DEFAULT_SIZE;
    }
    disruptor = new Disruptor<>(MqDataEvent::new, bufferSize, Executors.defaultThreadFactory(),
        ProducerType.SINGLE, new BlockingWaitStrategy());
  }

  /**
   * 获取队列数据数量
   *
   * @return
   */
  public long getQueueCount() {
    if (Objects.isNull(disruptor) || Objects.isNull(disruptor.getRingBuffer())) {
      return 0;
    }
    return this.bufferSize - this.disruptor.getRingBuffer().remainingCapacity();
  }

  protected void loadFromFile(String path) {
    long point = 0;
    int size = 10;
    Map<String, Object> map;
    List<MqData> data;
    while (true) {
      log.info("读取文件 path={} size={} ,point={}", path, size, point);
      map = FileStore.read(path, point, size);
      data = (ArrayList<MqData>) map.get("data");
      point = (long) map.get("point");
      data.forEach(this::publish);
      if (data.size() < size) {
        break;
      }
    }
  }

}
