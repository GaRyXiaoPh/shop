package cn.kt.mall.common.util;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	public String get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	public void set(String key, String value) {
		stringRedisTemplate.opsForValue().set(key, value);
	}

	public Long incr(String key, long liveTime) {
		RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
		Long increment = entityIdCounter.getAndIncrement();

		if ((null == increment || increment.longValue() == 0) && liveTime > 0) {// 初始设置过期时间
			entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
		}

		return increment;
	}
}
