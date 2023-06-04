package com.momo.theta;

import com.momo.theta.api.LockCallback;
import com.momo.theta.exception.TimeoutException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 默认的换窜实现方式
 */
@Slf4j
public class DefaultRedisCache implements Cache {


  /**
   * 默认超时时间
   */
  private final static Long DEFAULT_EXPIRE = 12 * 60 * 60L;
  /**
   * 默认的序列化方式
   */
  private final RedisSerializer DEFAULT_SERIALIZER = new DefaultRedisSerializer();
  /**
   * redisTemplate
   */
  private RedisTemplate redisTemplate;
  /**
   * redissonClient
   */
  private RedissonClient redissonClient;
  /**
   * 是否是集群
   */
  private boolean isCluster = false;


  public DefaultRedisCache() {
  }

  public DefaultRedisCache(RedisTemplate redisTemplate, RedissonClient redissonClient,
      boolean isCluster) {
    this.redisTemplate = redisTemplate;
    this.redissonClient = redissonClient;
    this.isCluster = isCluster;
  }

  public RedisTemplate getRedisTemplate() {
    return redisTemplate;
  }

  public void setRedisTemplate(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public RedissonClient getRedissonClient() {
    return redissonClient;
  }

  public void setRedissonClient(RedissonClient redissonClient) {
    this.redissonClient = redissonClient;
  }

  public boolean isCluster() {
    return isCluster;
  }

  public void setCluster(boolean cluster) {
    isCluster = cluster;
  }

  @Override
  public <T> T acquireForRedissonCallable(String lockKey, Long expire, LockCallback<T> callback,
      Long timeout) throws TimeoutException {
    RLock lock = redissonClient.getFairLock(lockKey);
    boolean lockRes = false;
    try {
      lockRes = lock.tryLock(timeout, expire, TimeUnit.MILLISECONDS);
    } catch (Throwable e) {
      log.error("acquire tryLock timeout ,lockKey:[" + lockKey + "] expire[" + expire + "] timeout["
          + timeout + "]", e);
    }
    if (!lockRes) {
      try {
        lock.unlock();
      } catch (Throwable t) {
      }
      String error =
          "acquire lock timeout ,lockKey:[" + lockKey + "] expire[" + expire + "] timeout["
              + timeout + "]";
      log.error(error);
      throw new TimeoutException("cache", "acquire", error);
    }
    log.info("acquire redisson lock key : [" + lockKey + "]");
    T result = null;
    try {
      result = callback.callBack();
    } finally {
      //事务完成后释放锁
      transCompatible(() -> {
        try {
          lock.unlock();
          log.info("redisson lock key : [" + lockKey + "] release");
        } catch (Throwable e) {
          log.error("redisson lock key : [" + lockKey + "] release error", e);
        }
      });
    }
    return result;
  }

  /**
   * 默认超时时间的放入
   *
   * @param key
   * @param value
   */
  @Override
  public void put(Object key, Object value) {
    put(key, value, DEFAULT_EXPIRE);
  }

  /***
   * 使用默已经指定的Jakson序列化方式
   * @param key key 值
   * @param value 入参
   * @param expire 超时时间(s）
   */
  @Override
  public void put(Object key, Object value, long expire) {
    redisTemplate.execute((RedisCallback) connection -> {
      if (key == null) {
        return null;
      }
      //这里需要指定默认的序列化方式 没有编写边界条件

      byte[] k = redisTemplate.getKeySerializer().serialize(key);
      byte[] v = redisTemplate.getValueSerializer().serialize(value);

      connection.setEx(k, expire, v);
      return null;
    });
  }

  /**
   * @param key key 值
   * @return
   */
  @Override
  public Object get(Object key) {
    return redisTemplate.execute(new RedisCallback() {
      @Override
      public Object doInRedis(RedisConnection connection) throws DataAccessException {
        byte[] k = redisTemplate.getKeySerializer().serialize(key);
        if (k == null) {
          return null;
        }
        byte[] bytes = connection.get(k);
        if (bytes == null) {
          return null;
        }
        return redisTemplate.getValueSerializer().deserialize(bytes);
      }
    });
  }

  /**
   * 删除方法
   *
   * @param key 值
   * @return 删除的值
   */
  @Override
  public Object remove(Object key) {
    return redisTemplate.execute(new RedisCallback() {
      @Override
      public Object doInRedis(RedisConnection connection) throws DataAccessException {

        RedisSerializer<Object> keySerializer = redisTemplate.getKeySerializer();

        if (key == null) {
          return null;
        }

        byte[] bytes = keySerializer.serialize(key);

        return connection.del(bytes);
      }
    });
  }


  /**
   * 分布式锁是在事务里面，假如有多个服务同时执行到了获取锁这一步， 只会有一个服务能获取到锁，其他服务会等待锁的释放（redission是使用订阅的方式，由redis-server通知client锁的释放事件）。
   * 待方法业务逻辑执行完成之后，锁就进行了释放， 但是事务还没有提交。其他服务这时获取到了锁， 虽然在执行前有进行重复检查，但是因为前一个服务的事务还没有提交，
   * 这里是获取不到结果的（数据库隔离级别为可重复读）， 还是能正常执行下去。这就导致了重复数据入库。
   *
   * @param supplier
   */
  protected void transCompatible(TransactionSupport supplier) {
    if (TransactionSynchronizationManager.isActualTransactionActive()) {
      TransactionSynchronizationManager.registerSynchronization(
          new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {
              log.info("afterCompletion ... status:[{}]",
                  STATUS_COMMITTED == status ? "STATUS_COMMITTED"
                      : (STATUS_ROLLED_BACK == status ? "STATUS_ROLLED_BACK" : "STATUS_UNKNOWN"));
              supplier.process();
              super.afterCompletion(status);
            }
          });
    } else {
      supplier.process();
    }
  }
}
