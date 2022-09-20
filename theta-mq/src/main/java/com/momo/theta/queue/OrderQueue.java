package com.momo.theta.queue;

import com.lmax.disruptor.dsl.Disruptor;
import com.momo.theta.queue.data.MqDataEvent;
import com.momo.theta.service.MqProducer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;


@Slf4j
public class OrderQueue extends DisruptorQueue {
    public static final String fineName = "ORDER.DATA";

    private final MqProducer producer;
    private String filePath;

    public OrderQueue(MqProducer producer, String filePath) {
        this.producer = producer;
        this.filePath = filePath + File.separator + OrderQueue.fineName;

    }

    @Override
    public void start() {
        init();
        Disruptor<MqDataEvent> disruptor = getDisruptor();
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            if (SAVE_FILE_FLAG) {
                log.info("OrderQueue 消息转存本地文件 key={} ", event.getValue().getKeys());
                FileStore.write(filePath, event.getValue());
            } else {
                log.info("OrderQueue 消息发送mq key={} ", event.getValue().getKeys());
                producer.sendMsg(event.getValue());
            }
        }).then((event, sequence, endOfBatch) -> event.clear());
        loadFromFile(filePath);
        FileStore.delete(filePath);
        disruptor.start();
    }

}
