package com.momo.theta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 默认的序列化加载器
 */
public class DefaultRedisSerializer implements RedisSerializer<Object> {

  private final static Logger LOGGER = LoggerFactory.getLogger(DefaultRedisSerializer.class);

  /**
   * 序列化
   *
   * @param object 对象
   * @return 返回字节码
   */
  @Override
  public byte[] serialize(Object object) {
    if (object == null) {
      return null;
    }
    try (// 序列化
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);) {
      oos.writeObject(object);
      byte[] bytes = baos.toByteArray();
      return bytes;
    } catch (Exception e) {
      LOGGER.error("serialize error", e);
    }

    return null;
  }

  /**
   * 反序列化
   *
   * @param bytes
   * @return
   */
  @Override
  public Object deserialize(byte[] bytes) {

    if (bytes == null) {
      return null;
    }
    try (// 反序列化
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);) {
      Object result = ois.readObject();
      return result;
    } catch (Exception e) {
      LOGGER.error("deserialize error", e);
    }
    return null;
  }
}
