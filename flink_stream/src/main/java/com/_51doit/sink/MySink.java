package com._51doit.sink;


import com._51doit.utils.KafkaUtils;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import redis.clients.jedis.Jedis;
import scala.Tuple3;

public class MySink<A, B, C> extends RichSinkFunction<Tuple3<A, B, C>> {

    private ParameterTool params = KafkaUtils.getParmas();

//  不建议使用连接池
//    private static JedisPoolConfig poolConfig = new JedisPoolConfig();
//
//    static {
//        poolConfig.setMaxIdle(2);
//        poolConfig.setMaxTotal(2);
//    }
//
//    private static JedisPool jedisPool = new JedisPool(poolConfig, params.getRequired("redis-host"), params.getInt("redis-port", 6379), 5000, params.getRequired("redis-pwd"));

    private String additionalKey = "";

    private Jedis jedis = null;


    public MySink(String additionalKey) {
        this.additionalKey = additionalKey;
    }

    /**
     * 打开一个redis的连接
     *
     * @param parameters
     * @throws Exception
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        System.out.println("+++++++开启一个Redis连接：" + this);
        jedis = new Jedis(params.getRequired("redis-host"), 6379, 5000);
        jedis.auth(params.getRequired("redis-pwd"));
    }

    /**
     * 写入数据
     *
     * @param value
     * @param context
     */
    @Override
    public void invoke(Tuple3<A, B, C> value, Context context) {

        if (!jedis.isConnected()) {
            jedis.connect();
        }
        jedis.hset(additionalKey + value._1().toString(), value._2().toString(), value._3().toString());
    }


    /**
     * 关闭连接
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        super.close();
        jedis.close();
        System.out.println("------关闭一个Redis连接：" + this);
    }


}
