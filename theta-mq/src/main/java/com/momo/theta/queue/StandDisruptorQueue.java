package com.momo.theta.queue;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.momo.theta.queue.data.ObjectEvent;
import java.util.List;

/**
 * 自定自己的ringBuffer 和 disruptor
 *
 * @param <T>
 */
public class StandDisruptorQueue<T> {

  private Disruptor<ObjectEvent<T>> disruptor;
  private RingBuffer<ObjectEvent<T>> ringBuffer;

  public StandDisruptorQueue(Disruptor<ObjectEvent<T>> disruptor) {
    this.disruptor = disruptor;
    this.ringBuffer = disruptor.getRingBuffer();
    this.disruptor.start();
  }

  public void add(T t) {
    if (t != null) {
      long sequence = this.ringBuffer.next();

      try {
        ObjectEvent<T> event = (ObjectEvent) this.ringBuffer.get(sequence);
        event.setObj(t);
      } finally {
        this.ringBuffer.publish(sequence);
      }
    }
  }

  public void addAll(List<T> ts) {
    if (ts != null) {

      for (T t : ts) {
        if (t != null) {
          this.add(t);
        }
      }
    }
  }

  public long cursor() {
    return this.disruptor.getRingBuffer().getCursor();
  }

  public void shutdown() {
    this.disruptor.shutdown();
  }

  public Disruptor<ObjectEvent<T>> getDisruptor() {
    return this.disruptor;
  }

  public void setDisruptor(Disruptor<ObjectEvent<T>> disruptor) {
    this.disruptor = disruptor;
  }

  public RingBuffer<ObjectEvent<T>> getRingBuffer() {
    return this.ringBuffer;
  }

  public void setRingBuffer(RingBuffer<ObjectEvent<T>> ringBuffer) {
    this.ringBuffer = ringBuffer;
  }
}