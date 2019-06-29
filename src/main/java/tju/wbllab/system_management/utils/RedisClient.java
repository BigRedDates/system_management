package tju.wbllab.system_management.utils;

import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.*;

public class RedisClient {
    private static RedisClient uniqueInstance;
    private Jedis jedis;
    private JedisPool jedisPool;
    private ShardedJedis shardedJedis;
    private ShardedJedisPool shardedJedisPool;
    private int lifeTime;
    private String ip;
    private String port;
    private String password;

    private RedisClient() {
        this.initialPool("127.0.0.1", "6379");
        this.initialShardedPool("127.0.0.1", Integer.valueOf("6379"));
        this.shardedJedis = this.shardedJedisPool.getResource();
        this.jedis = this.jedisPool.getResource();
        this.jedis.auth("123456");
        this.jedis.connect();
        this.lifeTime = 172800;
    }

    public RedisClient(String ip, String port, String psw) {
        this.initialPool(ip, port);
        this.initialShardedPool(ip, Integer.valueOf(port));
        this.shardedJedis = this.shardedJedisPool.getResource();
        this.jedis = this.jedisPool.getResource();
        //this.jedis.auth(psw);
        this.jedis.connect();
        this.password = psw;
        this.lifeTime = 172800;
    }

    public void setTestLifeTime() {
        this.lifeTime = 1;
    }

    public void reSetLifeTime() {
        this.lifeTime = 172800;
    }

    public static RedisClient getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new RedisClient();
        }

        return uniqueInstance;
    }

    public static RedisClient getInstance(String ip, String port, String psw) {
        if (uniqueInstance == null) {
            uniqueInstance = new RedisClient(ip, port, psw);
        }

        return uniqueInstance;
    }

    private void initialPool(String ip, String port) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(500);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(1000L);
        config.setTestOnBorrow(false);
        this.jedisPool = new JedisPool(config, ip, Integer.parseInt(port), 100000);
    }

    public void setKV(String key, String value, Integer seconds) {
        this.jedis.setex(key, seconds.intValue(), value);
    }

    private void initialShardedPool(String ip, Integer port) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(-1);
        config.setMaxIdle(-1);
        config.setMaxWaitMillis(1000L);
        config.setTestOnBorrow(false);
        List<JedisShardInfo> shards = new ArrayList();
        shards.add(new JedisShardInfo(ip, port.intValue(), "master"));
        this.shardedJedisPool = new ShardedJedisPool(config, shards);
    }
    //将值value关联到key，并将key的生存时间设为seconds(秒)。
    public void setKV(String key, String value) {
        this.jedis.setex(key, this.lifeTime, value);
    }
    //字符串值value关联到key。
    public void setStr(String key, String value) {
        this.jedis.set(key, value);
    }
    //通过键获取字符串value
    public String getValue(String key) {
        String result = this.jedis.get(key);
        return result;
    }

    //查找特定的key
    public Set<String> keys(String pattern) {
        return this.jedis.keys(pattern);
    }

    public boolean restoreMap(Map<String, String> map) {
        try {
            Iterator var2 = map.keySet().iterator();

            while (var2.hasNext()) {
                String key = (String) var2.next();
                this.setKV(key, (String) map.get(key));
            }

            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public boolean restoreMapWithAutoDel(Map<String, String> map, int existingTime) {
        try {
            Iterator var3 = map.keySet().iterator();

            while (var3.hasNext()) {
                String key = (String) var3.next();
                this.jedis.setex(key, existingTime, (String) map.get(key));
            }

            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }
    //列出所有的key
    public Set<String> getKeys() {
        return this.jedis.keys("*");
    }
    //移除给定的一个或多个key,如果key不存在,则忽略该命令.
    public void deleteKey(String key) {
        this.jedis.del(key);
    }
   //存储hash表中的值
    public void setHashValue(String key, String filed, String command) {
        this.jedis.hset(key, filed, command);
    }
    //取出hash表中对应的值
    public String getHashValue(String key, String filed) {
        return this.jedis.hget(key, filed);
    }
   //取出hash表中所有域和值     key, filed, command    key只有一个，  filed与command是K-V
    public Map<String, String> getMap(String key) {
        return this.jedis.hgetAll(key);
    }

    public void delMap(String mapKey) {
        this.jedis.del(mapKey);
    }

    public void delFieldOfMap(String key, String field) {
        this.jedis.hdel(key, new String[]{field});
    }

    private Jedis getJedis() {
        Long timeoutCount = 0L;

        while (true) {
            try {
                Jedis jedis = this.jedisPool.getResource();
                jedis.auth(this.password);
                return jedis;
            } catch (Exception var5) {
                if (var5 instanceof JedisConnectionException) {
                    timeoutCount = timeoutCount.longValue() + 1L;
                    System.out.println("getJedis timeoutCount=" + timeoutCount);
                    if (timeoutCount.longValue() <= 3L) {
                        continue;
                    }
                } else {
                    System.out.println(var5);
                    System.out.println("getJedis numactive=" + this.jedisPool.getNumActive());
                    System.out.println("getJedis numIdle=" + this.jedisPool.getNumIdle());
                    System.out.println("getJedis waiter=" + this.jedisPool.getNumWaiters());
                    this.jedisPool.isClosed();
                }

                return null;
            }
        }
    }

    public void disConnect() {
        this.jedis.disconnect();
    }
    //检查给定key是否存在
    public boolean hasKey(String key) {
        return this.jedis.exists(key).booleanValue();
    }
}

