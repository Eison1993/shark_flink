package com._51doit.serializer;

import com._51doit.ProtoStuffUtils;
import com._51doit.pojo.LogBean;
import org.apache.flink.api.common.serialization.SerializationSchema;

public class LogBeanSerializer implements SerializationSchema<LogBean> {
    @Override
    public byte[] serialize(LogBean logBean) {
        byte[] data = null;
        try {
            data = ProtoStuffUtils.serialize(logBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
}
