package com.child.common.redis;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("redisUtils")
public class RedisUtils {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    public void delete(String key){
        redisTemplate.delete(key);
    }

    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean set(String key,String value){
        try {
            redisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception e){
            logger.error("设置redisKey:{},value:{}失败", key, value);
            return false;
        }
    }

    public Boolean setEx(String key,String value,long timeout){
        try {
            if (timeout > 0){
                redisTemplate.opsForValue().set(key,value,timeout, TimeUnit.SECONDS);
            }else {
                set(key,value);
            }
            return true;
        }catch (Exception e){
            logger.error("设置redisKey:{},value:{},timeout:{}失败", key, value, timeout);
            return false;
        }
    }
}
