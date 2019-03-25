package com.zpself.manage.modules.redis.controller;

import com.zpself.manage.common.controller.BaseController;
import com.zpself.manage.modules.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by zengpeng on 2019/3/7
 */
@RestController
@RequestMapping("/redis")
public class RedisController extends BaseController {
    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping(value = "/test")
    public String testRedis(@RequestParam String key,@RequestParam String value) {
        redisService.setStr(key, value);
        Map map = new HashMap();
        map.put("123","aaad");
        redisService.setMap("sys.dict.findDictByCode", map);
        return key+"已经放入缓存！值为："+redisService.getStr(key);
    }

    @PostMapping(value = "/test/multiSet")
    public String testMultiSet(@RequestParam String key,@RequestParam String value) {
        Map map = new HashMap();
        map.put("1","1");
        map.put("2","2");
        map.put("3","3");

        Map map2 = new HashMap();
        map2.put("1","1");
        map2.put("2","2");
        map2.put("3","3");

        Map mapAll = new HashMap();
        mapAll.put("map",map);
        mapAll.put("map2",map2);
        redisService.multiSet(mapAll);
        return key+"已经放入缓存！值为："+redisService.getStr(key);
    }
    @PostMapping(value = "/test/testTreeSet")
    public String testTreeSet(@RequestParam String key,@RequestParam String value) {
        /*Set set = new HashSet();
        set.add("1");
        set.add("2");
        set.add("3");
        redisService.setSet("大学",set);*/
        /*ZSetOperations.TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<Object>("zset-5",9.6);
        ZSetOperations.TypedTuple<Object> objectTypedTuple2 = new DefaultTypedTuple<Object>("zset-6",9.9);
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<ZSetOperations.TypedTuple<Object>>();
        tuples.add(objectTypedTuple1);
        tuples.add(objectTypedTuple2);
        System.out.println(redisTemplate.opsForZSet().add("zset1",tuples));
        System.out.println(redisTemplate.opsForZSet().range("zset1",0,-1));*/


        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
       /* zSetOperations.add("book1","数学",80);
        zSetOperations.add("book1","语文",50);
        zSetOperations.add("book1","物理", 95);*/
        zSetOperations.incrementScore("book1","语文",-35);
        //Set book1 = zSetOperations.range("book1", 0, -1);
       //System.out.println(book1);
        return key+"已经放入缓存！值为："+redisService.getStr(key);
    }

}
