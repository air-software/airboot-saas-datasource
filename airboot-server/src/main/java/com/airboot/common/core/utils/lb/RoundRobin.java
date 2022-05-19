package com.airboot.common.core.utils.lb;

import com.airboot.common.core.redis.RedisCache;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 轮询实现类
 *
 * @author airboot
 */
@Component
public class RoundRobin {
    
    @Resource
    public RedisCache redisCache;
    
    public synchronized <T> T next(String key, List<T> robinList) {
        String redisKey = "round_robin_" + key;
        Integer index = redisCache.getCacheObject(redisKey);
        if (index == null || index + 1 > robinList.size()) {
            index = 0;
        }
        T next = robinList.get(index);
        redisCache.setCacheObject(redisKey, index + 1);
        return next;
    }
    
}
