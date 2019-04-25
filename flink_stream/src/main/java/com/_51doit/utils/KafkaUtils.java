package com._51doit.utils;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class KafkaUtils {
    private static ParameterTool params;

    private static StreamExecutionEnvironment env;

    public static <T> DataStreamSource<T> createKafkaDataStream(String[] args, Class<? extends DeserializationSchema<T>> clazz) throws Exception {
        // 获取参数
        params = ParameterTool.fromArgs(args);
        //创建env
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        //创建checkpoint的相关信息
        env.enableCheckpointing(params.getLong("checkpoint-interval", 5000L), CheckpointingMode.EXACTLY_ONCE);
        // 把状态保存在check-point文件夹，与checkpoint同时使用
        env.setStateBackend(new FsStateBackend(params.getRequired("checkpoint-dir")));
        //设置如果没有记录偏移量就从最开始度，有偏移量就接着读
        Properties kafkaparams = params.getProperties();
        kafkaparams.setProperty("auto.offset.reset", params.get("auto.offset.reset", "earliest"));

        //topics可以设置为一个list集合
        String topics = params.getRequired("input-topic");

        List<String> topicList = Arrays.asList(topics.split(","));

        //创建一个KafkaConsumer
        FlinkKafkaConsumer011<T> kafkasource = new FlinkKafkaConsumer011<>(
                topicList,
                clazz.newInstance(),
                kafkaparams
        );
        // 这两个参数是默认指定的
        //kafkaSource.setStartFromGroupOffsets();
        //kafkaSource.setCommitOffsetsOnCheckpoints(true);

//       创建kafka flink数据流
        DataStreamSource<T> kafkadatastream = env.addSource(kafkasource);

        return kafkadatastream;

    }
    //    创建flink的sink，将kafka读取出来的数据反序列化之后，进行分流，再序列化写入kafka中，
    public static <T> void addSink(DataStream<T> dataStream, String outputTopic, Class<? extends SerializationSchema<T>> clazz) throws Exception {
        FlinkKafkaProducer011<T> kafkaProducer = new FlinkKafkaProducer011<>(
                outputTopic,
                clazz.newInstance(),
                params.getProperties()
        );
        dataStream.addSink(kafkaProducer);
    }
    //    get方法
    public static ParameterTool getParmas() {
        return params;
    }
    public static StreamExecutionEnvironment getEnv() {
        return env;
    }
}
