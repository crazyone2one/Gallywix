package cn.master.gallywix.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author 11's papa
 * @since 11/08/2023
 **/
@Configuration
@RequiredArgsConstructor
public class RedisUtils {
    private final StringRedisTemplate redisTemplate;

    public void setString(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
    public String getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
