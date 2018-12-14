package protocol;

import com.alibaba.fastjson.JSON;
import protocol.constant.SerializerAlgorithm;

public class JsonSerializer implements Serializer{

    @Override
    public Byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serializer(Object o) {
        return JSON.toJSONBytes(o);
    }

    @Override
    public <T> T deserializer(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes,clazz);
    }
}
