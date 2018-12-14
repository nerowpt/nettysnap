package protocol;

public interface Serializer {

    Serializer DEFAULT = new JsonSerializer();
    /**
     * 获取序列化算法
     */
    Byte getSerializerAlgorithm();

    /**
     * 序列化
     */
    byte[] serializer(Object o);

    /**
     * 反序列化
     */
    <T> T deserializer(byte[] bytes,Class<T> clazz);
}
