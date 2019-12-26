package com.qiuxs.cuteframework.tech.mc.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.utils.io.IOUtils;
import com.qiuxs.cuteframework.core.basic.utils.net.NetUtils;
import com.qiuxs.cuteframework.core.persistent.redis.RedisConfiguration;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

public class RedisFacade {

	private static final Logger log = LogManager.getLogger(RedisFacade.class);

	private Pool<Jedis> jedisPool;

	public RedisFacade() {
		jedisPool = RedisConfiguration.getJedisPool();
	}

	public RedisFacade(String poolName) {
		jedisPool = RedisConfiguration.getJedisPool(poolName);
	}
	
	public RedisFacade(String pollName, int dbIndex) {
		this.jedisPool = RedisConfiguration.getJedisPool(pollName, dbIndex);
	}

	/**
	 * 获取 key 对应的 string 值,如果 key 不存在返回 null
	 * 
	 * @param key
	 * @return
	 */
	public byte[] get(byte[] key) {
		return (byte[]) run("get", key);
	}

	public String get(String key) {
		return (String) run("getS", key);
	}

	/**
	 * 一次获取多个 key 的值，如果对应 key 不存在，则对应返回 nil
	 * 
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<byte[]> mget(byte[]... keys) {
		return (List<byte[]>) run("mget", (Object[]) keys);// 不强转Object[]，key被封装到Object[1]中
	}

	@SuppressWarnings("unchecked")
	public List<String> mget(String... keys) {
		return (List<String>) run("mgetS", (Object[]) keys);
	}

	/**
	 * 
	 * 设置 key 对应的值为 string 类型的 value
	 * 
	 * @param key
	 * @param value
	 * @return 状态码，如成功为OK
	 */
	public byte[] set(byte[] key, byte[] value) {
		return (byte[]) run("set", key, value);
	}

	public String set(String key, String value) {
		return (String) run("setS", key, value);
	}

	/**
	 * 
	 * @param key
	 * @return 如果key不存在，将key的值设为val,返回1;key已存在，不做任何动作，返回0；
	 */
	public Long setnx(byte[] key, byte[] val) {
		return (Long) run("setnx", key, val);
	}

	public Long setnx(String key, String val) {
		return (Long) run("setnxS", key, val);
	}

	/**
	 * 设置 key 对应的值为 string 类型的 value，并指定此键值的有效期为seconds
	 * 
	 * @param key
	 * @param expire_s
	 *            有效期，单位秒
	 * @return 状态码，如成功为OK
	 */
	public String setex(byte[] key, int expire_s, byte[] val) {
		return (String) run("setex", key, expire_s, val);
	}

	public String setex(String key, int expire_s, String val) {
		return (String) run("setexS", key, expire_s, val);
	}

	/** 设置一个键的超时时间 */
	public Long expire(byte[] key, int seconds) {
		return (Long) run("expire", key, seconds);
	}

	/** 设置一个键的超时时间 */
	public Long expire(String key, int seconds) {
		return (Long) run("expireS", key, seconds);
	}

	/**
	 * 将key 中储存的数字值增一
	 * 
	 * @param key
	 * @return 执行INCR 命令之后key 的值。
	 */
	public Long incr(byte[] key) {
		return (Long) run("incr", key);
	}

	public Long incr(String key) {
		return (Long) run("incrS", key);
	}

	/**
	 * 将key 中储存的数字值减一
	 * 
	 * @param key
	 * @return 执行DECR 命令之后key 的值。
	 */
	public Long decr(byte[] key) {
		return (Long) run("decr", key);
	}

	public Long decr(String key) {
		return (Long) run("decrS", key);
	}

	/**
	 * 将key 中储存的数字值增increment
	 * 
	 * @param key
	 * @return 执行INCR 命令之后key 的值。
	 */
	public Long incrBy(byte[] key, long increment) {
		return (Long) run("incrby", key, increment);
	}

	public Long incrBy(String key, long increment) {
		return (Long) run("incrbyS", key, increment);
	}

	/**
	 * 将key 中储存的数字值减decrement
	 * 
	 * @param key
	 * @return 执行decrBy命令之后key 的值。
	 */
	public Long decrBy(byte[] key, long decrement) {
		return (Long) run("decrby", key, decrement);
	}

	public Long decrBy(String key, long decrement) {
		return (Long) run("decrbyS", key, decrement);
	}

	/**
	 * 删除key
	 * 
	 * @param keys
	 * @return
	 */
	public Long del(byte[] key) {
		return (Long) run("del", key);
	}

	public Long del(String key) {
		return (Long) run("delS", key);
	}

	/**
	 * 确认一个key是否存在
	 * 
	 * @param key
	 * @return
	 */
	public Boolean exists(byte[] key) {
		return (Boolean) run("exists", key);
	}

	public Boolean exists(String key) {
		return (Boolean) run("existsS", key);
	}

	/**
	 * 重命名key
	 * 
	 * @param oldkey
	 * @param newkey
	 * @return
	 */
	public String rename(byte[] oldkey, byte[] newkey) {
		return (String) run("rename", oldkey, newkey);
	}

	public String rename(String oldkey, String newkey) {
		return (String) run("renameS", oldkey, newkey);
	}

	/**
	 * 返回满足给定 pattern 的所有 key
	 * 
	 * @param pattern
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<byte[]> keys(byte[] pattern) {
		return (Set<byte[]>) run("keys", pattern);
	}

	@SuppressWarnings("unchecked")
	public Set<String> keys(String pattern) {
		return (Set<String>) run("keysS", pattern);
	}

	/**
	 * 返回哈希表key 中给定域field 的值。给定key 不存在时，返回nil 。
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public byte[] hget(byte[] key, byte[] field) {
		return (byte[]) run("hget", key, field);
	}

	public String hget(String key, String field) {
		return (String) run("hgetS", key, field);
	}

	@SuppressWarnings("unchecked")
	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		return (List<byte[]>) run("hmget", key, fields);
	}

	@SuppressWarnings("unchecked")
	public List<String> hmget(String key, String... fields) {
		return (List<String>) run("hmget", key, fields);
	}

	/**
	 * 返回哈希表key 中的所有域
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<byte[]> hkeys(byte[] key) {
		return (Set<byte[]>) run("hkeys", key);
	}

	@SuppressWarnings("unchecked")
	public Set<String> hkeys(String key) {
		return (Set<String>) run("hkeysS", key);
	}

	/**
	 * 返回哈希表key 中所有域的值。
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<byte[]> hvals(byte[] key) {
		return (List<byte[]>) run("hvals", key);
	}

	@SuppressWarnings("unchecked")
	public List<String> hvals(String key) {
		return (List<String>) run("hvalsS", key);
	}

	/**
	 * 返回哈希表key 中，所有的域和值；
	 * 
	 * @param key
	 * @return 不存在的key返回空Map
	 */
	@SuppressWarnings("unchecked")
	public Map<byte[], byte[]> hgetAll(byte[] key) {
		return (Map<byte[], byte[]>) run("hgetall", key);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> hgetAll(String key) {
		return (Map<String, String>) run("hgetallS", key);
	}

	/**
	 * 将哈希表key 中的域field 的值设为value 。<br>
	 * 如果key 不存在，一个新的哈希表被创建并进行HSET 操作,返回1。<br>
	 * 如果域field 已经存在于哈希表中，旧值将被覆盖,返回0。<br>
	 * 
	 * @param keys
	 * @return
	 */
	public Long hset(byte[] key, byte[] field, byte[] value) {
		return (Long) run("hset", key, field, value);
	}

	public Long hset(String key, String field, String value) {
		return (Long) run("hsetS", key, field, value);
	}

	/**
	 * 将哈希表key 中的域field 的值设为value 。<br>
	 * 如果key 不存在，一个新的哈希表被创建并进行HSET 操作,返回1。<br>
	 * 如果域field 已经存在于哈希表中,返回0。<br>
	 * 
	 * @param keys
	 * @return
	 */
	public Long hsetNx(byte[] key, byte[] field, byte[] value) {
		return (Long) run("hsetnx", key, field, value);
	}

	public Long hsetNx(String key, String field, String value) {
		return (Long) run("hsetnxS", key, field, value);
	}

	/**
	 * 
	 * redis 127.0.0.1:6379> hmset myhash field1 Hello field2 World OK
	 * 
	 * @author fengdg
	 * @param key
	 * @param fields
	 * @return "OK"
	 */
	public byte[] hmset(byte[] key, Map<byte[], byte[]> fields) {
		return (byte[]) run("hmset", key, fields);
	}

	public String hmset(String key, Map<String, String> fields) {
		return (String) run("hmsetS", key, fields);
	}

	/**
	 * redis 127.0.0.1:6379> hincrby myhash field3 -8 (integer) 12
	 * 
	 * @author fengdg
	 * @param key
	 * @param field
	 * @return
	 */
	public Long hincrBy(byte[] key, byte[] field, long value) {
		return (Long) run("hincrby", key, field, value);
	}

	public Long hincrBy(String key, String field, long value) {
		return (Long) run("hincrbyS", key, field, value);
	}

	/**
	 * 删除哈希表key 中的一个指定域，不存在的域将被忽略。
	 * 
	 * @param key
	 * @param field
	 * @return 被成功移除的域的数量，不包括被忽略的域。
	 */
	public Long hdel(byte[] key, byte[] field) {
		return (Long) run("hdel", key, field);
	}

	public Long hdel(String key, String field) {
		return (Long) run("hdelS", key, field);
	}

	/**
	 * 查看哈希表key 中，给定域field 是否存在。
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public Boolean hexists(byte[] key, byte[] field) {
		return (Boolean) run("hexists", key, field);
	}

	public Boolean hexists(String key, String field) {
		return (Boolean) run("hexistsS", key, field);
	}

	/**
	 * 返回哈希表key 中域的数量。当key 不存在时，返回0 。
	 * 
	 * @param key
	 * @return
	 */
	public Long hlen(byte[] key) {
		return (Long) run("hlen", key);
	}

	public Long hlen(String key) {
		return (Long) run("hlenS", key);
	}

	/**
	 * 返回列表key 中，下标为index 的元素。
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public byte[] lindex(byte[] key, long index) {
		return (byte[]) run("lindex", key, index);
	}

	public String lindex(String key, long index) {
		return (String) run("lindexS", key, index);
	}

	/**
	 * 返回列表key 中指定区间内的元素，区间以偏移量start 和stop 指定。
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<byte[]> lrange(byte[] key, long start, long end) {
		return (List<byte[]>) run("lrange", key, start, end);
	}

	@SuppressWarnings("unchecked")
	public List<String> lrange(String key, long start, long end) {
		return (List<String>) run("lrangeS", key, start, end);
	}

	/**
	 * 将列表key 下标为index 的元素的值设置为value 。
	 * 
	 * @param key
	 * @param index
	 * @param value
	 * @return 操作成功返回ok ，否则返回错误信息。
	 */
	public String lset(byte[] key, long index, byte[] value) {
		return (String) run("lset", key, index, value);
	}

	public String lset(String key, long index, String value) {
		return (String) run("lsetS", key, index, value);
	}

	/**
	 * 将一个值插入到列表key 的表尾(最右边)。
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Long rpush(byte[] key, byte[]... memeber) {
		return (Long) run("rpush", key, memeber);
	}

	public Long rpush(String key, String... memeber) {
		return (Long) run("rpushS", key, memeber);
	}

	/**
	 * 将一个值插入到列表key 的表头(最左边)。
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Long lpush(byte[] key, byte[]... memeber) {
		return (Long) run("lpush", key, memeber);
	}

	public Long lpush(String key, String... memeber) {
		return (Long) run("lpushS", key, memeber);
	}

	/**
	 * 移除并返回列表key 的最右边元素。
	 * 
	 * @param key
	 * @return
	 */
	public byte[] rpop(byte[] key) {
		return (byte[]) run("rpop", key);
	}

	public String rpop(String key) {
		return (String) run("rpopS", key);
	}

	/**
	 * 移除并返回列表key 的最左边元素。
	 * 
	 * @param key
	 * @return
	 */
	public byte[] lpop(byte[] key) {
		return (byte[]) run("lpop", key);
	}

	public String lpop(String key) {
		return (String) run("lpopS", key);
	}

	/**
	 * 根据参数count 的值，移除列表中与参数value 相等的元素。count 的值可以是以下几种：<br>
	 * count > 0 : 从表头开始向表尾搜索，移除与value 相等的元素，数量为count 。<br>
	 * count < 0 : 从表尾开始向表头搜索，移除与value 相等的元素，数量为count 的绝对值。<br>
	 * count = 0 : 移除表中所有与value 相等的值。<br>
	 * 
	 * @param key
	 * @param count
	 * @param memeber
	 * @return
	 */
	public Long lrem(byte[] key, long count, byte[] memeber) {
		return (Long) run("lrem", key, count, memeber);
	}

	public Long lrem(String key, long count, String memeber) {
		return (Long) run("lremS", key, count, memeber);
	}

	/**
	 * 返回列表key 的长度。如果key 不存在，返回0 .
	 * 
	 * @param key
	 * @return
	 */
	public Long llen(byte[] key) {
		return (Long) run("llen", key);
	}

	public Long llen(String key) {
		return (Long) run("llenS", key);
	}

	/**
	 * 返回集合key 中的所有成员。不存在的key 被视为空集合。
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<byte[]> smembers(byte[] key) {
		return (Set<byte[]>) run("smembers", key);
	}

	@SuppressWarnings("unchecked")
	public Set<String> smembers(String key) {
		return (Set<String>) run("smembersS", key);
	}

	/**
	 * 将一个或member 元素加入到集合key 当中,返回1，已经存在于集合的member 元素将被忽略，返回0。
	 * 
	 * @param key
	 * @param memeber
	 * @return
	 */
	public Long sadd(byte[] key, byte[]... memeber) {
		return (Long) run("sadd", key, memeber);
	}

	public Long sadd(String key, String... memeber) {
		return (Long) run("saddS", key, memeber);
	}

	/**
	 * 移除集合key中的一个member 元素返回1，不存在的member 元素会被忽略返回0。
	 * 
	 * @param key
	 * @param memeber
	 * @return
	 */
	public Long srem(byte[] key, byte[] memeber) {
		return (Long) run("srem", key, memeber);
	}

	public Long srem(String key, String memeber) {
		return (Long) run("sremS", key, memeber);
	}

	/**
	 * 返回集合key中元素的数量,不存在的集合返回0
	 * 
	 * @param key
	 * @return
	 */
	public Long scard(byte[] key) {
		return (Long) run("scard", key);
	}

	public Long scard(String key) {
		return (Long) run("scardS", key);
	}

	/**
	 * 判断member 元素是否集合key 的成员。是返回1，不是或key不存在，返回0
	 * 
	 * @param key
	 * @param memeber
	 * @return
	 */
	public Boolean sismember(byte[] key, byte[] memeber) {
		return (Boolean) run("sismember", key, memeber);
	}

	public Boolean sismember(String key, String memeber) {
		return (Boolean) run("sismemberS", key, memeber);
	}

	/** 删除当前连接的库,返回状态码 */
	public String flushDB() {
		return (String) run("flushDB");
	}

	/**
	 * 先设置key的field属性值，然后获取key的最新的所有属性值
	 * 
	 * @author fengdg
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<byte[], byte[]> hsetGetAll(byte[] key, byte[] field, byte[] value) {
		return (Map<byte[], byte[]>) run("hsetgetall", key, field, value);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> hsetGetAll(String key, String field, String value) {
		return (Map<String, String>) run("hsetgetallS", key, field, value);
	}

	/** 获取jedis连接的本地与远程ip,端口 */
	public String getAddrPort() {
		return (String) run("getaddrport");
	}

	/**
	 * 运行lua脚本
	 * 
	 * @param script
	 *            redis-lua脚本
	 * @param keys
	 *            redis-key-list，脚本里用KEYS[1]等获取
	 * @param args
	 *            输入值，脚本里用ARGV[1]等获取
	 * @return
	 */
	public Object eval(String script, List<String> keys, List<String> args) {
		return run("eval", script, keys, args);
	}

	/**
	 * 通用运行redis命令方法
	 * 
	 * @param cmd
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object run(String cmd, Object... args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null) {
				throw new IllegalArgumentException("RedisDAO.run(),input args cann't be null");
			}
		}

		Object ret = null;

		Jedis jedis = null;
		try {
			jedis = this.jedisPool.getResource();
			switch (cmd) {
			// strings操作
			case "get":
				ret = jedis.get((byte[]) args[0]);
				break;
			case "getS":
				ret = jedis.get((String) args[0]);
				break;
			case "mget":
				// args=Object[1]；
				ret = jedis.mget((byte[][]) args);
				break;
			case "mgetS":
				// args=Object[n]；每个key对应一个Object，
				ret = jedis.mget((String[]) args);
				break;
			case "set":
				ret = jedis.set((byte[]) args[0], (byte[]) args[1]);
				break;
			case "setS":
				ret = jedis.set((String) args[0], (String) args[1]);
				break;
			case "setnx":
				ret = jedis.setnx((byte[]) args[0], (byte[]) args[1]);
				break;
			case "setnxS":
				ret = jedis.setnx((String) args[0], (String) args[1]);
				break;
			case "setex":
				ret = jedis.setex((byte[]) args[0], (int) args[1], (byte[]) args[2]);
				break;
			case "setexS":
				ret = jedis.setex((String) args[0], (int) args[1], (String) args[2]);
				break;
			case "expire":
				ret = jedis.expire((byte[]) args[0], (int) args[1]);
				break;
			case "expireS":
				ret = jedis.expire((String) args[0], (int) args[1]);
				break;
			case "incr":
				ret = jedis.incr((byte[]) args[0]);
				break;
			case "incrS":
				ret = jedis.incr((String) args[0]);
				break;
			case "decr":
				ret = jedis.decr((byte[]) args[0]);
				break;
			case "decrS":
				ret = jedis.decr((String) args[0]);
				break;
			case "incrby":
				ret = jedis.incrBy((byte[]) args[0], (long) args[1]);
				break;
			case "incrbyS":
				ret = jedis.incrBy((String) args[0], (long) args[1]);
				break;
			case "decrby":
				ret = jedis.decrBy((byte[]) args[0], (long) args[1]);
				break;
			case "decrbyS":
				ret = jedis.decrBy((String) args[0], (long) args[1]);
				break;
			case "del":
				ret = jedis.del((byte[]) args[0]);
				break;
			case "delS":
				ret = jedis.del((String) args[0]);
				break;
			case "exists":
				ret = jedis.exists((byte[]) args[0]);
				break;
			case "existsS":
				ret = jedis.exists((String) args[0]);
				break;
			case "rename":
				ret = jedis.rename((byte[]) args[0], (byte[]) args[1]);
				break;
			case "renameS":
				ret = jedis.rename((String) args[0], (String) args[1]);
				break;
			case "keys":
				ret = jedis.keys((byte[]) args[0]);
				break;
			case "keysS":
				ret = jedis.keys((String) args[0]);
				break;
			// hashes操作
			case "hget":
				ret = jedis.hget((byte[]) args[0], (byte[]) args[1]);
				break;
			case "hgetS":
				ret = jedis.hget((String) args[0], (String) args[1]);
				break;
			case "hmget":
				ret = jedis.hmget((byte[]) args[0], (byte[][]) args[1]);
				break;
			case "hmgetS":
				ret = jedis.hmget((String) args[0], (String[]) args[1]);
				break;
			case "hkeys":
				ret = jedis.hkeys((byte[]) args[0]);
				break;
			case "hkeysS":
				ret = jedis.hkeys((String) args[0]);
				break;
			case "hvals":
				ret = jedis.hvals((byte[]) args[0]);
				break;
			case "hvalsS":
				ret = jedis.hvals((String) args[0]);
				break;
			case "hgetall":
				ret = jedis.hgetAll((byte[]) args[0]);
				break;
			case "hgetallS":
				ret = jedis.hgetAll((String) args[0]);
				break;
			case "hset":
				ret = jedis.hset((byte[]) args[0], (byte[]) args[1], (byte[]) args[2]);
				break;
			case "hsetS":
				ret = jedis.hset((String) args[0], (String) args[1], (String) args[2]);
				break;
			case "hsetnx":
				ret = jedis.hsetnx((byte[]) args[0], (byte[]) args[1], (byte[]) args[2]);
				break;
			case "hsetnxS":
				ret = jedis.hsetnx((String) args[0], (String) args[1], (String) args[2]);
				break;
			case "hmset":
				ret = jedis.hmset((byte[]) args[0], (Map<byte[], byte[]>) args[1]);
				break;
			case "hmsetS":
				ret = jedis.hmset((String) args[0], (Map<String, String>) args[1]);
				break;
			case "hincrby":
				ret = jedis.hincrBy((byte[]) args[0], (byte[]) args[1], (long) args[2]);
				break;
			case "hincrbyS":
				ret = jedis.hincrBy((String) args[0], (String) args[1], (long) args[2]);
				break;
			case "hdel":
				ret = jedis.hdel((byte[]) args[0], (byte[]) args[1]);
				break;
			case "hdelS":
				ret = jedis.hdel((String) args[0], (String) args[1]);
				break;
			case "hexists":
				ret = jedis.hexists((byte[]) args[0], (byte[]) args[1]);
				break;
			case "hexistsS":
				ret = jedis.hexists((String) args[0], (String) args[1]);
				break;
			case "hlen":
				ret = jedis.hlen((byte[]) args[0]);
				break;
			case "hlenS":
				ret = jedis.hlen((String) args[0]);
				break;
			// lists操作
			case "lindex":
				ret = jedis.lindex((byte[]) args[0], (long) args[1]);
				break;
			case "lindexS":
				ret = jedis.lindex((String) args[0], (long) args[1]);
				break;
			case "lrange":
				ret = jedis.lrange((byte[]) args[0], (long) args[1], (long) args[2]);
				break;
			case "lrangeS":
				ret = jedis.lrange((String) args[0], (long) args[1], (long) args[2]);
				break;
			case "lset":
				ret = jedis.lset((byte[]) args[0], (long) args[1], (byte[]) args[2]);
				break;
			case "lsetS":
				ret = jedis.lset((String) args[0], (long) args[1], (String) args[2]);
				break;
			case "rpush":
				ret = jedis.rpush((byte[]) args[0], (byte[][]) args[1]);
				break;
			case "rpushS":
				ret = jedis.rpush((String) args[0], (String[]) args[1]);
				break;
			case "lpush":
				ret = jedis.lpush((byte[]) args[0], (byte[][]) args[1]);
				break;
			case "lpushS":
				ret = jedis.lpush((String) args[0], (String[]) args[1]);
				break;
			case "lpop":
				ret = jedis.lpop((byte[]) args[0]);
				break;
			case "lpopS":
				ret = jedis.lpop((String) args[0]);
				break;
			case "rpop":
				ret = jedis.rpop((byte[]) args[0]);
				break;
			case "rpopS":
				ret = jedis.rpop((String) args[0]);
				break;
			case "lrem":
				ret = jedis.lrem((byte[]) args[0], (long) args[1], (byte[]) args[2]);
				break;
			case "lremS":
				ret = jedis.lrem((String) args[0], (long) args[1], (String) args[2]);
				break;
			case "llen":
				ret = jedis.llen((String) args[0]);
				break;
			case "llenS":
				ret = jedis.llen((String) args[0]);
				break;
			// sets操作
			case "smembers":
				ret = jedis.smembers((byte[]) args[0]);
				break;
			case "smembersS":
				ret = jedis.smembers((String) args[0]);
				break;
			case "sadd":
				// args=Object[2]
				ret = jedis.sadd((byte[]) args[0], (byte[][]) args[1]);// args=Object[]
				break;
			case "saddS":
				// args=Object[2]
				ret = jedis.sadd((String) args[0], (String[]) args[1]);// args=Object[]
				break;
			case "srem":
				ret = jedis.srem((byte[]) args[0], (byte[]) args[1]);
				break;
			case "sremS":
				ret = jedis.srem((String) args[0], (String) args[1]);
				break;
			case "scard":
				ret = jedis.scard((String) args[0]);
				break;
			case "scardS":
				ret = jedis.scard((String) args[0]);
				break;
			case "sismember":
				ret = jedis.sismember((byte[]) args[0], (byte[]) args[1]);
				break;
			case "sismemberS":
				ret = jedis.sismember((String) args[0], (String) args[1]);
				break;
			// 管理命令
			case "flushDB":
				ret = jedis.flushDB();
				break;
			// 自定义命令
			case "hsetgetall":
				ret = jedis.hset((byte[]) args[0], (byte[]) args[1], (byte[]) args[2]);
				ret = jedis.hgetAll((byte[]) args[0]);
				break;
			case "hsetgetallS":
				ret = jedis.hset((String) args[0], (String) args[1], (String) args[2]);
				ret = jedis.hgetAll((String) args[0]);
				break;
			case "getaddrport":
				ret = NetUtils.getAddrPort(jedis.getClient().getSocket());
				break;
			case "eval":
				ret = jedis.eval((String) args[0], (List<String>) args[1], (List<String>) args[2]);
				break;
			default:
				log.error("unsupported cmd=" + cmd);
			}
		} catch (JedisException e) {
			StringBuffer sbLog = new StringBuffer(cmd);
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					sbLog.append(",arg").append(i).append("=").append(args[i]);
				}
			}
			sbLog.append(",ex=").append(e.getMessage());
			throw e;
		} finally {
			IOUtils.closeQuietly(jedis);
		}
		return ret;
	}

}
