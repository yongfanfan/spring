package com.dark.common.redis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import com.dark.common.domain.Constants;

public class JedisPoolFactory {
	
	public static final int DEFAULT_THREAD_COUNT = 4;

	public static JedisPool createJedisPool(String defaultHost, int defaultPort, int defaultTimeout, int threadCount) {
		// 合并命令行传入的系统变量与默认值
		String host = System.getProperty("benchmark.host", defaultHost);
		String port = System.getProperty("benchmark.port", String.valueOf(defaultPort));
		String timeout = System.getProperty("benchmark.timeout", String.valueOf(defaultTimeout));

		// 设置Pool大小，设为与线程数等大，并屏蔽掉idle checking
		JedisPoolConfig poolConfig = JedisUtils.createPoolConfig(threadCount, threadCount);

		// create jedis pool
		return new JedisPool(poolConfig, host, Integer.valueOf(port), Integer.valueOf(timeout));
	}
	
	public static ShardedJedisPool createShardedJedisPool(int threadCount) {
		// 设置Pool大小，设为与线程数等大，并屏蔽掉idle checking
		JedisPoolConfig poolConfig = JedisUtils.createPoolConfig(threadCount, threadCount);

		// create shardedjedis pool
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		JedisShardInfo master = new JedisShardInfo(Constants.REDIS_MASTER_HOST, 6379, "master");
		if ( StringUtils.isNotBlank(Constants.REDIS_MASTER_PASSWORD) ) {
			master.setPassword(Constants.REDIS_MASTER_PASSWORD);
		}
        shards.add(master);
		return new ShardedJedisPool(poolConfig, shards);
	}
	
	public static ShardedJedisPool createShardedJedisPool() {
		return createShardedJedisPool(DEFAULT_THREAD_COUNT);
	}
}
