package com.gaotu.serialize;

import com.gaotu.serialize.model.City;
import com.gaotu.serialize.model.Person;
import com.gaotu.serialize.model.avro.AvroExample;
import com.gaotu.serialize.model.json.JacksonJsonExample;
import com.gaotu.serialize.model.protobuf.ProtoBuf;
import com.gaotu.serialize.model.thrift.ThriftExample;
import com.gaotu.serialize.util.ProtoBufUtil;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TIOStreamTransport;
import org.apache.thrift.transport.TTransport;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        execJdkSerializer();
        System.out.println("-------------------------------");
        execAvroSerializer();
        System.out.println("-------------------------------");
        execProtoBufSerializer();
        System.out.println("-------------------------------");
        execThriftSerializer();
        System.out.println("-------------------------------");
        execJacksonJsonSerializer();
        System.out.println("-------------------------------");
        execProtoStuffSerializer();
    }
    //     {"name": "money", "type": ["double", "null"]}
    //java -jar /Users/bjhl/DownLoads/avro-tools-1.9.2.jar compile schema /Users/bjhl/Public/jdk/example.avsc .

    // JDK自带序列化器
    public static void execJdkSerializer() {
        Person example = new Person();
        example.setName("mian");
        example.setAge(18);
        example.setSex("男");
        example.setMoney(1000000.00);
        ObjectOutputStream oos = null;
        ByteArrayOutputStream bos = null;
        ObjectInputStream ois = null;
        ByteArrayInputStream bis = null;
        long start = System.currentTimeMillis();
        System.out.println("JDK序列化器开始序列化：" + start);
        for (int i = 0; i < 1000000; i++) {
            try {
                bos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(bos);
                oos.writeObject(example);
                byte[] bytes = bos.toByteArray();
                bis = new ByteArrayInputStream(bytes);
                ois = new ObjectInputStream(bis);
                example = (Person) ois.readObject();
                if (i == 1) {
                    System.out.println("example：" + example);
                    System.out.println("size：" + bytes.length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("JDK序列化器完成序列化：" + end);
        System.out.println("JDK序列化器总用时：" + (end - start));
    }

    // avro序列化协议
    public static void execAvroSerializer() {
        AvroExample example = new AvroExample();
        example.setName("mian");
        example.setAge(18);
        example.setSex("男");
        example.setMoney(1000000.00);
        DatumWriter<AvroExample> userDatumWriter = new SpecificDatumWriter<>(AvroExample.class);
        ByteArrayOutputStream outputStream = null;
        DatumReader<AvroExample> userDatumReader = new SpecificDatumReader<>(AvroExample.class);
        long start = System.currentTimeMillis();
        System.out.println("Avro序列化器开始序列化：" + start);
        for (int i = 0; i < 1000000; i++) {
            outputStream = new ByteArrayOutputStream();
            BinaryEncoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);
            try {
                userDatumWriter.write(example, binaryEncoder);
                byte[] bytes = outputStream.toByteArray();
                BinaryDecoder binaryDecoder = DecoderFactory.get().directBinaryDecoder(new ByteArrayInputStream(bytes), null);
                example = userDatumReader.read(new AvroExample(), binaryDecoder);
                if (i == 1) {
                    System.out.println("example：" + example);
                    System.out.println("size：" + bytes.length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Avro序列化器完成序列化：" + end);
        System.out.println("Avro序列化器总用时：" + (end - start));
    }

    // protobuf序列化
    public static void execProtoBufSerializer() {
        ProtoBuf.ProtoBufExample.Builder builder = ProtoBuf.ProtoBufExample.newBuilder();
        builder.setName("mian");
        builder.setAge(18);
        builder.setSex("男");
        builder.setMoney(100000.00);
        ProtoBuf.ProtoBufExample example = builder.build();
        long start = System.currentTimeMillis();
        System.out.println("ProtoBuf序列化器开始序列化：" + start);
        for (int i = 0; i < 1000000; i++) {
            byte[] bytes = example.toByteArray();
            try {
                example = ProtoBuf.ProtoBufExample.parseFrom(bytes);
                if (i == 1) {
                    System.out.println("example：" + example);
                    System.out.println("size：" + bytes.length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("ProtoBuf序列化器完成序列化：" + end);
        System.out.println("ProtoBuf序列化器总用时：" + (end - start));
    }
    // protoc -I=/Users/bjhl/Public/jdk --java_out=/Users/bjhl/Public/jdk/ /Users/bjhl/Public/jdk/example.proto
    // export PATH=/Users/bjhl/Public/bin:$PATH
    // export PROTOBUF/Users/bjhl/Public
    // export PATH=$PROTOBUF/bin:$PATH

    // thrift序列化
    public static void execThriftSerializer() {
        ThriftExample example = new ThriftExample();
        example.setName("mian");
        example.setAge(18);
        example.setSex("男");
        example.setMoney(100000.00);
        ByteArrayOutputStream out;
        TTransport transport;
        // 二进制编码格式进行数据传输
        TBinaryProtocol tp;
        long start = System.currentTimeMillis();
        System.out.println("Thrift序列化器开始序列化：" + start);
        for (int i = 0; i < 1000000; i++) {
            out = new ByteArrayOutputStream();
            transport = new TIOStreamTransport(out);
            tp = new TBinaryProtocol(transport);
            try {
                example.write(tp);
            } catch (TException e) {
                e.printStackTrace();
            }
            byte[] bytes = out.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            example = new ThriftExample();
            transport = new TIOStreamTransport(bis);
            tp = new TBinaryProtocol(transport);
            try {
                example.read(tp);
                if (i == 1) {
                    System.out.println("example：" + example);
                    System.out.println("size：" + bytes.length);
                }
            } catch (TException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Thrift序列化器完成序列化：" + end);
        System.out.println("Thrift序列化器总用时：" + (end - start));
    }

    // json序列化
    public static void execJacksonJsonSerializer() {
        JacksonJsonExample example = new JacksonJsonExample();
        example.setName("mian");
        example.setAge(18);
        example.setSex("男");
        example.setMoney(100000.00);
        ObjectMapper mapper = new ObjectMapper();
        long start = System.currentTimeMillis();
        System.out.println("JacksonJson序列化器开始序列化：" + start);
        for (int i = 0; i < 1000000; i++) {
            try {
                String jsonValue = mapper.writeValueAsString(example);
                byte[] bytes = jsonValue.getBytes();
                example = mapper.readValue(jsonValue, JacksonJsonExample.class);
                if (i == 1) {
                    System.out.println("example：" + example);
                    System.out.println("size：" + bytes.length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("JacksonJson序列化器完成序列化：" + end);
        System.out.println("JacksonJson序列化器总用时：" + (end - start));
    }

    // 使用protostuff序列化
    // byte[] serializerResult = ProtoBufUtil.serializer(student);
    public static void execProtoStuffSerializer() {
        Person example = new Person();
        example.setName("mian");
        example.setAge(18);
        example.setSex("男");
        example.setMoney(100000.00);
        ObjectMapper mapper = new ObjectMapper();
        long start = System.currentTimeMillis();
        System.out.println("ProtoStuff序列化器开始序列化：" + start);
        for (int i = 0; i < 1000000; i++) {
                byte[] bytes = ProtoBufUtil.serializer(example);
                example = ProtoBufUtil.deserializer(bytes, Person.class);
                if (i == 1) {
                    System.out.println("example：" + example);
                    System.out.println("size：" + bytes.length);
                }
        }
        long end = System.currentTimeMillis();
        System.out.println("ProtoStuff序列化器完成序列化：" + end);
        System.out.println("ProtoStuff序列化器总用时：" + (end - start));
    }

    public static void execComplexProtoStuffSerializer() {
        Country country = new Country();
        country.setName("兔子");
        List<City> cities = new ArrayList<>();
        City shanghai = new City();
        shanghai.setName("上海");
        Person p1 = new Person();
        p1.setName("李四");
        p1.setAge(12);
        p1.setSex("男");
        p1.setMoney(100);
        Person p2 = new Person();
        p1.setName("张三");
        p1.setAge(14);
        p1.setSex("女");
        p1.setMoney(1);
        List<Person> people = new ArrayList<>();
        people.add(p1);
        people.add(p2);
        shanghai.setPeople(people);
        City beijing = new City();
        beijing.setName("北京");
        beijing.setPeople(people);
        cities.add(shanghai);
        cities.add(beijing);

    }


}



