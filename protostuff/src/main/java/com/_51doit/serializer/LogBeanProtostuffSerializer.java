package com._51doit.serializer;

import com._51doit.ProtoStuffUtils;
import com._51doit.pojo.LogBean;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 *
 * 使用Protostuff将数据序列化写入到Kafka
 *
 */
public class LogBeanProtostuffSerializer implements Serializer<byte[]> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, byte[] bytes) {
        String jsonStr = new String(bytes);
        //将Json字符串转成对象
        try {
            LogBean logBean = JSON.parseObject(jsonStr, LogBean.class);
            //将User序列化成ProtoStuff的二进制类型
            return ProtoStuffUtils.serialize(logBean);
        } catch (Exception e) {
            //e.printStackTrace();
            //TODO 将有问题的数据记录到对应的存储系统中
            return null;
        }
    }
    @Override
    public void close() {

    }
}
