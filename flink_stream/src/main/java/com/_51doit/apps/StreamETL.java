package com._51doit.apps;

import com._51doit.deserializer.LogBeanDeserializer;
import com._51doit.pojo.LogBean;
import com._51doit.serializer.LogBeanSerializer;
import com._51doit.utils.KafkaUtils;
import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.connectors.fs.StringWriter;
import org.apache.flink.streaming.connectors.fs.bucketing.BucketingSink;
import org.apache.flink.streaming.connectors.fs.bucketing.DateTimeBucketer;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.ZoneId;

public class StreamETL {
    public static void main(String[] args) throws Exception {
        DataStreamSource<LogBean> kafkaDataStream =KafkaUtils.createKafkaDataStream(args,LogBeanDeserializer.class);

//        将数据清洗过滤
        SingleOutputStreamOperator<LogBean> filteredDataStream =kafkaDataStream.filter(new FilterFunction<LogBean>() {
            @Override
            public boolean filter(LogBean logBean) {
                return logBean!=null;
            }
        });
        //按照数据的主题进行分流,但是该方法已经过时
//        SplitStream<LogBean> splitedStream = filteredDataStream.split(new OutputSelector<LogBean>() {
//            @Override
//            public Iterable<String> select(LogBean logBean) {
//                List<String> output = new ArrayList<String>();
//                String logType = logBean.getLogType();
//                if (logType != null && logType.startsWith("act_")) {
//                    output.add("activity");
//                } else {
//                    output.add("flow");
//                }
//                return output;
//            }
//        });

        //测流输出，替代split，相比filter，效率有很大提升
        OutputTag<LogBean> activityTag =new OutputTag<LogBean>("activity"){};

        OutputTag<LogBean> flowTag =new OutputTag<LogBean>("flow"){};

        SingleOutputStreamOperator<LogBean> mainDataStream = filteredDataStream.process(new ProcessFunction<LogBean, LogBean>() {
            @Override
            public void processElement(LogBean logBean, Context context, Collector<LogBean> collector) {
//                输出主流，保留原始的流
                collector.collect(logBean);
//                分流，非主流
                String logType = logBean.getLogType();
                if (logType != null && logType.startsWith("act_")) {
                    context.output(activityTag, logBean);
                } else {
                    context.output(flowTag, logBean);
                }

            }
        });

        DataStream<LogBean> activityDataStream = mainDataStream.getSideOutput(activityTag);

        DataStream<LogBean> flowDataStream = mainDataStream.getSideOutput(flowTag);

//        将测流保存到kafka中
        KafkaUtils.addSink(activityDataStream, "doit-activity", LogBeanSerializer.class);

        KafkaUtils.addSink(flowDataStream, "doit-flow", LogBeanSerializer.class);


//        将主流保存到hdfs（作业）
        BucketingSink<String> sink = new BucketingSink<String>("hdfs://zys01:9000/mainout");
//        设置时间
        sink.setBucketer(new DateTimeBucketer<>("yyyy-MM-dd--HHmm", ZoneId.of("Asia/Shanghai")));
//        设定写入的格式
        sink.setWriter(new StringWriter<String>());
//        设定批次的大小
        sink.setBatchSize(1024 * 1024 * 200); // this is 400 MB,
//        设定每一个批次的时间
        sink.setBatchRolloverInterval(10 * 1000); // this is 30 sec

        mainDataStream.map(new MapFunction<LogBean, String>() {
            @Override
            public String map(LogBean value) {
                return JSON.toJSONString(value);
            }
        }).addSink(sink);

        KafkaUtils.getEnv().execute("StreamETL");
    }
}

