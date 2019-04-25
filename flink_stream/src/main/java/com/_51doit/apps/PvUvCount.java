package com._51doit.apps;

import com._51doit.deserializer.LogBeanDeserializer;
import com._51doit.pojo.LogBean;
import com._51doit.sink.MySink;
import com._51doit.utils.KafkaUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

import java.util.Date;

/**
 * 官方带的那个RedisSink不够灵活
 * <p>
 * 将Flink计算好的数据写入到Redis中
 *
 * @param <A>
 * @param <B>
 * @param <C>
 */

public class PvUvCount {
    public static void main(String[] args) throws Exception {

        DataStreamSource<LogBean> kafkaDataStream = KafkaUtils.createKafkaDataStream(args,LogBeanDeserializer.class);

//       先获取数据所携带的时间作为key，出现一次，记作一次pv

        SingleOutputStreamOperator<Tuple4<String, String, String, Integer>> timeGuidAndOne = kafkaDataStream.map(new MapFunction<LogBean, Tuple4<String, String, String, Integer>>() {
            FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyyMMdd");

            @Override
            public Tuple4<String, String, String, Integer> map(LogBean logBean) {
                String androidId = logBean.getU().getPhone().getAndroidId();
                String deviceId = logBean.getU().getPhone().getDeviceId();
                String guid = androidId + deviceId;
//                获取维度信息
                String osName = logBean.getU().getPhone().getOsName();
                String commit_time = logBean.getCommit_time();
                long time = Long.parseLong(commit_time);
                Date date = new Date(time);
                String dateStr = fastDateFormat.format(date);
                return Tuple4.of(dateStr, guid, osName, 1);

            }
        });

//        pv计算
        SingleOutputStreamOperator<Tuple4<String, String, String, Integer>> pv = timeGuidAndOne.keyBy(0).sum(3);


//        uv计算
        SingleOutputStreamOperator<Tuple4<String, String, String, Integer>> uvReduced = timeGuidAndOne.keyBy(0, 1).sum(3);


//     保存到redis

        SingleOutputStreamOperator<Tuple3<String, String, Integer>> timeGuid = timeGuidAndOne.map(new MapFunction<Tuple4<String, String, String, Integer>, Tuple3<String, String, Integer>>() {
            @Override
            public Tuple3<String, String, Integer> map(Tuple4<String, String, String, Integer> value) {
                return Tuple3.of(value.f0, value.f1, value.f3);
            }
        });

        SingleOutputStreamOperator<Tuple3<String, String, Integer>> pvtoredis = timeGuid.keyBy(0, 1).sum(2).map(new MapFunction<Tuple3<String, String, Integer>, Tuple3<String, String, Integer>>() {
            @Override
            public Tuple3<String, String, Integer> map(Tuple3<String, String, Integer> value) {
                return Tuple3.of(value.f0, "PV", value.f2);
            }
        });

        pvtoredis.addSink(new MySink("PV-"));

        SingleOutputStreamOperator<Tuple3<String, String, Integer>> osnamepv = timeGuidAndOne.keyBy(0, 2).sum(3).map(new MapFunction<Tuple4<String, String, String, Integer>, Tuple3<String, String, Integer>>() {
            @Override
            public Tuple3<String, String, Integer> map(Tuple4<String, String, String, Integer> value) {
                return Tuple3.of(value.f0, value.f2, value.f3);
            }
        });

        osnamepv.addSink(new MySink("PV-OS-"));


        KafkaUtils.getEnv().execute("PvUvCount");
    }

}


