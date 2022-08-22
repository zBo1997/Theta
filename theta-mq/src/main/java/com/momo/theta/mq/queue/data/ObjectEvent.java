package com.momo.theta.mq.queue.data;

/**
 * 实体监听对象
 * @param <T>
 */
public class ObjectEvent<T> {
    private T obj;
 
    public ObjectEvent() {
    }
 
    public T getObj() {
        return this.obj;
    }
 
    public void setObj(T obj) {
        this.obj = obj;
    }
}