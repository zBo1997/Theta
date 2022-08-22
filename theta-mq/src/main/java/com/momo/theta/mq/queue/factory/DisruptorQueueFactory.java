package com.momo.theta.mq.queue.factory;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.momo.theta.mq.queue.NormalQueue;
import com.momo.theta.mq.queue.StandDisruptorQueue;
import com.momo.theta.mq.queue.data.ObjectEvent;
import com.momo.theta.mq.queue.data.ObjectEventFactory;

import java.util.concurrent.Executors;

/***
 * -- BlockingWaitStrategy:
 * 性能最低
 * 对CPU的消耗最小
 * 能够在不同的部署环境中提供更加一致的性能
 * -- SleepingWaitStrategy:
 * 性能以及对CPU的消耗和BlockingWaitStrategy差不多
 * 对生产者线程影响最小
 * 适合应用于异步日志等场景
 * -- YieldingWaitStrategy:
 * 性能最高
 * 适合应用于低延迟的系统
 * 在要求极高的性能并且事件处理线程个数小于CPU逻辑核心个数的场景中,推荐使用这个等待策略
 * 比如CPU开启超线程的特性
 */
public class DisruptorQueueFactory {
    public DisruptorQueueFactory() {
    }

    // 创建"点对电模式"的操作队列，即同一事件会被一组消费者其中之一消费
    public static <T> StandDisruptorQueue<T> getWorkPoolQueue(int queueSize, boolean isMoreProducer,
                                                              NormalQueue.ADisruptorConsumer<T>... consumers) {
        Disruptor<ObjectEvent<T>> _disruptor = new Disruptor(new ObjectEventFactory(),
                queueSize, Executors.defaultThreadFactory(),
                isMoreProducer ? ProducerType.MULTI : ProducerType.SINGLE,
                new SleepingWaitStrategy());
        _disruptor.handleEventsWithWorkerPool(consumers);
        return new StandDisruptorQueue(_disruptor);
    }

    // 创建"发布订阅模式"的操作队列，即同一事件会被多个消费者并行消费
    public static <T> StandDisruptorQueue<T> getHandleEventsQueue(int queueSize, boolean isMoreProducer,
                                                                  NormalQueue.ADisruptorConsumer<T>... consumers) {
        Disruptor<ObjectEvent<T>> _disruptor = new Disruptor(new ObjectEventFactory(),
                queueSize, Executors.defaultThreadFactory(),
                isMoreProducer ? ProducerType.MULTI : ProducerType.SINGLE,
                new BlockingWaitStrategy());
        _disruptor.handleEventsWith(consumers);
        return new StandDisruptorQueue(_disruptor);
    }

    // 直接通过传入的 Disruptor 对象创建操作队列（如果消费者有依赖关系的话可以用此方法）
    public static <T> StandDisruptorQueue<T> getQueue(Disruptor<ObjectEvent<T>> disruptor) {
        return new StandDisruptorQueue(disruptor);
    }
}