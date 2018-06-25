package com.dark.common.redis;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

import com.dark.common.util.Collections3;
import com.dark.common.util.SerializationUtils;

/**
 * JedisTemplate 提供了一个template方法，负责对Jedis连接的获取与归还。
 * JedisAction<T> 和 JedisActionNoResult两种回调接口，适用于有无返回值两种情况。
 * 同时提供一些最常用函数的封装, 如get/set/zadd等。
 */
@Component
public class JedisTemplate {
	private static Logger logger = LoggerFactory.getLogger(JedisTemplate.class);

	private Pool<ShardedJedis> jedisPool;
	
	public JedisTemplate() {
		this.jedisPool = JedisPoolFactory.createShardedJedisPool();
	}

	public JedisTemplate(Pool<ShardedJedis> jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * 执行有返回结果的action。
	 */
	public <T> T execute(JedisAction<T> jedisAction) throws JedisException {
		ShardedJedis jedis = null;
		boolean broken = false;
		try {
			jedis = jedisPool.getResource();
			return jedisAction.action(jedis);
		} catch (JedisConnectionException e) {
			logger.error("Redis connection lost.", e);
			broken = true;
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	/**
	 * 执行无返回结果的action。
	 */
	public void execute(JedisActionNoResult jedisAction) throws JedisException {
		ShardedJedis jedis = null;
		boolean broken = false;
		try {
			jedis = jedisPool.getResource();
			jedisAction.action(jedis);
		} catch (JedisConnectionException e) {
			logger.error("Redis connection lost.", e);
			broken = true;
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	/**
	 * 根据连接是否已中断的标志，分别调用returnBrokenResource或returnResource。
	 */
	protected void closeResource(ShardedJedis jedis, boolean connectionBroken) {
		if (jedis != null) {
			try {
				if (connectionBroken) {
					jedisPool.returnBrokenResource(jedis);
				} else {
					jedisPool.returnResource(jedis);
				}
			} catch (Exception e) {
				logger.error("Error happen when return jedis to pool, try to close it directly.", e);
				JedisUtils.closeJedis(jedis);
			}
		}
	}

	/**
	 * 获取内部的pool做进一步的动作。
	 */
	public Pool<ShardedJedis> getJedisPool() {
		return jedisPool;
	}

	/**
	 * 有返回结果的回调接口定义。
	 */
	public interface JedisAction<T> {
		T action(ShardedJedis jedis);
	}

	/**
	 * 无返回结果的回调接口定义。
	 */
	public interface JedisActionNoResult {
		void action(ShardedJedis jedis);
	}

	// ////////////// 常用方法的封装 ///////////////////////// //

	// ////////////// 公共 ///////////////////////////
	/**
	 * 删除key, 如果key存在返回true, 否则返回false。
	 */
	public Boolean del(final String key) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(ShardedJedis jedis) {
				return jedis.del(key) == 1 ? true : false;
			}
		});
	}
	
	public Boolean del(final byte[] key) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(ShardedJedis jedis) {
				return jedis.del(key) == 1 ? true : false;
			}
		});
	}

	// ////////////// 关于String ///////////////////////////
	/**
	 * 如果key不存在, 返回null.
	 */
	public String get(final String key) {
		return execute(new JedisAction<String>() {

			@Override
			public String action(ShardedJedis jedis) {
				return jedis.get(key);
			}
		});
	}
	
	public byte[] get(final byte[] key) {
		return execute(new JedisAction<byte[]>() {

			@Override
			public byte[] action(ShardedJedis jedis) {
				return jedis.get(key);
			}
		});
	}

	/**
	 * 如果key不存在, 返回null.
	 */
	public Long getAsLong(final String key) {
		String result = get(key);
		return result != null ? Long.valueOf(result) : null;
	}

	/**
	 * 如果key不存在, 返回null.
	 */
	public Integer getAsInt(final String key) {
		String result = get(key);
		return result != null ? Integer.valueOf(result) : null;
	}
	
	public void set(final byte[] key, final byte[] value) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(ShardedJedis jedis) {
				jedis.set(key, value);
			}
			
		});
	}

	public void set(final String key, final String value) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(ShardedJedis jedis) {
				jedis.set(key, value);
			}
		});
	}

	public void setex(final byte[] key, final byte[] value, final int seconds) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(ShardedJedis jedis) {
				jedis.setex(key, seconds, value);
			}
		});
	}
	
	public void setex(final String key, final String value, final int seconds) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(ShardedJedis jedis) {
				jedis.setex(key, seconds, value);
			}
		});
	}

	/**
	 * 如果key还不存在则进行设置，返回true，否则返回false.
	 */
	public Boolean setnx(final String key, final String value) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(ShardedJedis jedis) {
				return jedis.setnx(key, value) == 1 ? true : false;
			}
		});
	}
	
	/**
	 * 如果key还不存在则进行设置，返回true，否则返回false.
	 */
	public Boolean setnx(final byte[] key, final byte[] value) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(ShardedJedis jedis) {
				return jedis.setnx(key, value) == 1 ? true : false;
			}
		});
	}

	public Long incr(final String key) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(ShardedJedis jedis) {
				return jedis.incr(key);
			}
		});
	}

	public Long decr(final String key) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(ShardedJedis jedis) {
				return jedis.decr(key);
			}
		});
	}

	// ////////////// 关于List ///////////////////////////
	public void lpush(final String key, final String... values) {
		execute(new JedisActionNoResult() {
			@Override
			public void action(ShardedJedis jedis) {
				jedis.lpush(key, values);
			}
		});
	}
	
	public void lpush(final String key, final int seconds, final String... values) {
		execute(new JedisActionNoResult() {
			@Override
			public void action(ShardedJedis jedis) {
				jedis.expire(key, seconds);
				jedis.lpush(key,values);
			}
		});
	}

	public String rpop(final String key) {
		return execute(new JedisAction<String>() {

			@Override
			public String action(ShardedJedis jedis) {
				return jedis.rpop(key);
			}
		});
	}

	/**
	 * 返回List长度, key不存在时返回0，key类型不是list时抛出异常.
	 */
	public Long llen(final String key) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(ShardedJedis jedis) {
				return jedis.llen(key);
			}
		});
	}

	/**
	 * 删除List中的第一个等于value的元素，value不存在或key不存在时返回false.
	 */
	public Boolean lremOne(final String key, final String value) {
		return execute(new JedisAction<Boolean>() {
			@Override
			public Boolean action(ShardedJedis jedis) {
				Long count = jedis.lrem(key, 1, value);
				return (count == 1);
			}
		});
	}

	/**
	 * 删除List中的所有等于value的元素，value不存在或key不存在时返回false.
	 */
	public Boolean lremAll(final String key, final String value) {
		return execute(new JedisAction<Boolean>() {
			@Override
			public Boolean action(ShardedJedis jedis) {
				Long count = jedis.lrem(key, 0, value);
				return (count > 0);
			}
		});
	}

	// ////////////// 关于Sorted Set ///////////////////////////
	/**
	 * 加入Sorted set, 如果member在Set里已存在, 只更新score并返回false, 否则返回true.
	 */
	public Boolean zadd(final String key, final String member, final double score) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(ShardedJedis jedis) {
				return jedis.zadd(key, score, member) == 1 ? true : false;
			}
		});
	}

	/**
	 * 删除sorted set中的元素，成功删除返回true，key或member不存在返回false。
	 */
	public Boolean zrem(final String key, final String member) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(ShardedJedis jedis) {
				return jedis.zrem(key, member) == 1 ? true : false;
			}
		});
	}

	/**
	 * 当key不存在时返回null.
	 */
	public Double zscore(final String key, final String member) {
		return execute(new JedisAction<Double>() {

			@Override
			public Double action(ShardedJedis jedis) {
				return jedis.zscore(key, member);
			}
		});
	}

	/**
	 * 返回sorted set长度, key不存在时返回0.
	 */
	public Long zcard(final String key) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(ShardedJedis jedis) {
				return jedis.zcard(key);
			}
		});
	}
	
	/**
	 * 查询key是否存在
	 */
	public boolean exists(final String key) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(ShardedJedis jedis) {
				return jedis.exists(key);
			}
		});
	}
	
	/**
	 * 设置过期时间
	 */
	public void expire(final String key,final int seconds) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(ShardedJedis jedis) {
				jedis.expire(key, seconds);
			}
			
		});
	}
	
	/**
	 * 数据缓存,不存在再从数据库取
	 */
	@SuppressWarnings("rawtypes")
	public <T> T getDataFromDbOrRedis(byte[] key,final String update_key,DBExecutor<T> dbExecutor,Object... params) {
		byte[] datas = this.get(key);
		T t = null;
		if ( datas == null || (update_key != null && this.exists(update_key))) {
			//需检查是否需要更新
			t = dbExecutor.selectFromDb(params);
			if ( t != null  ) {
				if (t instanceof Collection && Collections3.isEmpty((Collection) t)) {
					return t;
				}
				//使用setnx避免重复设置
				this.setnx(key, SerializationUtils.serialize(t));
				if ( update_key != null) {
					this.del(update_key);
				}
			}
		} else {
			t = SerializationUtils.deserialize(datas);
		}
		return t;
	}
	
	public interface DBExecutor<T> {
		T selectFromDb(Object... args);
	}
}
