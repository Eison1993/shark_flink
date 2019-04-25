package com._51doit.deserializer;

import com._51doit.ProtoStuffUtils;
import com._51doit.pojo.LogBean;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

public class LogBeanDeserializer implements DeserializationSchema<LogBean> {
    @Override
    public LogBean deserialize(byte[] message) throws IOException {
        LogBean logBean = null;
        try {
            //使用ProtoStuff反序列化
            ProtoStuffUtils.deserialize(message, LogBean.class);
        }catch (Exception e){
            e.printStackTrace();
            //TODO 把有问题的数据单独记录下来
        }
        return logBean;
    }

    @Override
    public boolean isEndOfStream(LogBean logBean) {
        return false;
    }

    @Override
    public TypeInformation<LogBean> getProducedType() {
        return null;
    }
}
