package com.springbootutils.util;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonTemplate {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 获取字符串对象
     *
     * @param objectName
     * @return
     */
    public <T> RBucket<T> getRBucket(String objectName) {
        return redissonClient.getBucket(objectName);
    }

    public String getRBucketValue(String objectName) {
        RBucket bucket = redissonClient.getBucket(objectName);
        if (bucket != null) {
            return (String) bucket.get();
        } else {
            return null;
        }
    }

    /**
     * 获取Map对象
     *
     * @param objectName
     * @return
     */
    public <K, V> RMap<K, V> getRMap(String objectName) {
        RMap<K, V> map = redissonClient.getMap(objectName);
        return map;
    }

    /**
     * 获取有序集合
     *
     * @param objectName
     * @return
     */
    public <V> RSortedSet<V> getRSortedSet(String objectName) {
        RSortedSet<V> sortedSet = redissonClient.getSortedSet(objectName);
        return sortedSet;
    }

    /**
     * 获取集合
     *
     * @param objectName
     * @return
     */
    public <V> RSet<V> getRSet(String objectName) {
        RSet<V> rSet = redissonClient.getSet(objectName);
        return rSet;
    }

    public <T> void rsetAdd(String key, T t) {
        redissonClient.getSet(key).add(t);
    }

    /**
     * 获取列表
     *
     * @param objectName
     * @return
     */
    public <V> RList<V> getRListAndExpire(String objectName, long time, TimeUnit timeUnit) {
        RList<V> rList = redissonClient.getList(objectName);
        rList.expire(time, timeUnit);
        return rList;
    }

    public <V> RList<V> getRList(String objectName) {
        RList<V> rList = redissonClient.getList(objectName);
        return rList;
    }

    /**
     * 获取队列
     *
     * @param objectName
     * @return
     */
    public <V> RQueue<V> getRQueue(String objectName) {
        RQueue<V> rQueue = redissonClient.getQueue(objectName);
        return rQueue;
    }

    /**
     * 获取双端队列
     *
     * @param objectName
     * @return
     */
    public <V> RDeque<V> getRDeque(String objectName) {
        RDeque<V> rDeque = redissonClient.getDeque(objectName);
        return rDeque;
    }

//    /**
//     * 此方法不可用在Redisson 1.2 中
//     * 在1.2.2版本中 可用
//     *
//     * @param redisson
//     * @param objectName
//     * @return
//     */
//    public <V> RBlockingQueue<V> getRBlockingQueue(Redisson redisson, String objectName) {
//        RBlockingQueue rb = redisson.getBlockingQueue(objectName);
//        return rb;
//    }

    /**
     * 获取锁
     *
     * @param objectName
     * @return
     */
    public RLock getRLock(String objectName) {
        RLock rLock = redissonClient.getLock(objectName);
        return rLock;
    }

    /**
     * 获取原子数
     *
     * @param objectName
     * @return
     */
    public RAtomicLong getRAtomicLong(String objectName) {
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong(objectName);
        return rAtomicLong;
    }

    /**
     * 获取记数锁
     *
     * @param objectName
     * @return
     */
    public RCountDownLatch getRCountDownLatch(String objectName) {
        return redissonClient.getCountDownLatch(objectName);
    }

    /**
     * 获取消息的Topic
     *
     * @param objectName
     * @return
     */
    public RTopic getRTopic(String objectName) {
        return redissonClient.getTopic(objectName);
    }

    public void setBucketValueAndExpire(String key, String value, int expires_time, TimeUnit timeUnit) {
        RBucket<String> rBucket = getRBucket(key);
        rBucket.set(value, expires_time, timeUnit);
    }

    public boolean addKeyNxThreadSafe(String lockKey, String key, String value, int expireTime, TimeUnit timeUnit) {
        RLock lock = null;
        try {
            lock = getRLock(lockKey);
            lock.lock();
            if (!getRBucket(key).isExists()) {
                setBucketValueAndExpire(key, "1", expireTime, timeUnit);
                return true;
            }
            return false;
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }

    public boolean keyExist(String key) {
        return getRBucket(key).isExists();
    }
}
