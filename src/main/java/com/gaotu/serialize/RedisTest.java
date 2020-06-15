package com.gaotu.serialize;

import com.gaotu.serialize.model.Person;
import com.gaotu.serialize.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private ValueOperations<String, Object> valueOperations;

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    private ListOperations<String, Object> listOperations;

    @Autowired
    private SetOperations<String, Object> setOperations;

    @Autowired
    private ZSetOperations<String, Object> zSetOperations;

    @Resource
    private RedisService redisService;

    @Test
    public void testValueOption() throws Exception {
        Person example = new Person();
        example.setName("mian");
        example.setAge(18);
        example.setSex("男");
        example.setMoney(1000000.00);
        valueOperations.set("protobuf", example);
        System.out.println(valueOperations.get("protobuf"));
//        Country country = new Country();
//        country.setName("兔子");
//        List<City> cities = new ArrayList<>();
//        City shanghai = new City();
//        shanghai.setName("上海");
//        Person p1 = new Person();
//        p1.setName("李四");
//        p1.setAge(12);
//        p1.setSex("男");
//        p1.setMoney(100);
//        Person p2 = new Person();
//        p2.setName("张三");
//        p2.setAge(14);
//        p2.setSex("女");
//        p2.setMoney(1);
//        List<Person> people = new ArrayList<>();
//        people.add(p1);
//        people.add(p2);
//        shanghai.setPeople(people);
//        City beijing = new City();
//        beijing.setName("北京");
//        beijing.setPeople(people);
//        cities.add(shanghai);
//        cities.add(beijing);
//        country.setCities(cities);
//        valueOperations.set("rabbit", country);
//        System.out.println(valueOperations.get("rabbit"));
    }

}
