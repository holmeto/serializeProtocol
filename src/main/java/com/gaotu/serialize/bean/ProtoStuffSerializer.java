package com.gaotu.serialize.bean;

import com.gaotu.serialize.InvalidTagException;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.Tag;
import io.protostuff.runtime.RuntimeSchema;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProtoStuffSerializer implements RedisSerializer<Object> {


    private static final Schema<ObjectWrapper> schema = RuntimeSchema.getSchema(ObjectWrapper.class);

    @Override
    public byte[] serialize(Object t) throws SerializationException {
        verifyTagValid(t);
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] bytes;
        try {
            bytes = ProtostuffIOUtil.toByteArray(new ObjectWrapper(t), schema, buffer);
        } finally {
            buffer.clear();
        }
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            ObjectWrapper objectWrapper = new ObjectWrapper();
            ProtostuffIOUtil.mergeFrom(bytes, objectWrapper, schema);
            return objectWrapper.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *      * 获取类clazz的所有Field，包括其父类的Field，如果重名，以子类Field为准。
     *      * @param clazz
     *      * @return Field数组
     *     
     */
    public static Field[] getAllField(Class<?> clazz) {
        ArrayList<Field> fieldList = new ArrayList<>();
        Field[] dFields = clazz.getDeclaredFields();
        if (dFields.length > 0) {
            fieldList.addAll(Arrays.asList(dFields));
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != Object.class) {
            Field[] superFields = getAllField(superClass);
            if (superFields.length > 0) {
                for (Field field : superFields) {
                    if (!isContain(fieldList, field)) {
                        fieldList.add(field);
                    }
                }
            }
        }
        Field[] result = new Field[fieldList.size()];
        fieldList.toArray(result);
        return result;
    }

    /**
     * 检测Field List中是否已经包含了目标field
     *      * @param fieldList
     *      * @param field 带检测field
     *      * @return
     *     
     */
    public static boolean isContain(ArrayList<Field> fieldList, Field field) {
        for (Field temp : fieldList) {
            if (temp.getName().equals(field.getName())) {
                return true;
            }
        }
        return false;
    }

    public void verifyTagValid(Object obj) {
        Class clazz = obj.getClass();
        Field[] fields = getAllField(clazz);
        List<TagField> list = new ArrayList<>();
        for (Field field : fields) {
            Tag tag = field.getAnnotation(Tag.class);
            if (tag == null) {
                throw new InvalidTagException("类中有没绑定tag标签的属性，无法完成序列化，字段名为：" + field.getName());
            } else {
                list.add(new TagField(tag.value(), field));
            }
        }
        Map<Integer, List<TagField>> map = list.stream().collect(Collectors.groupingBy(TagField::getRank));
        if (map.size() < list.size()) {
            for (Map.Entry<Integer, List<TagField>> entry : map.entrySet()) {
                if (entry.getValue().size() > 1) {
                    throw new InvalidTagException("类中属性在绑定tag标签时，有重复的值，无法完成序列化。重复的字段名为："
                            + entry.getValue().stream().map(TagField::getField).map(Field::getName).collect(Collectors.joining(";")));
                }
            }
            throw new InvalidTagException("类中属性在绑定tag标签时，有重复的值，无法完成序列化，请仔细检查！");
        }
    }

    static class TagField {

        int rank;

        Field field;

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public TagField() {
        }

        public TagField(int rank, Field field) {
            this.rank = rank;
            this.field = field;
        }
    }

    static class ObjectWrapper {
        private Object object;

        ObjectWrapper() {
        }

        ObjectWrapper(Object object) {
            this.object = object;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }

}
