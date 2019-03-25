package com.zpself.manage.modules.annotation;

import java.lang.reflect.Method;

/**
 * Created by zengpeng on 2019/3/12
 */
public class TestHello {
    @Hello("hello")
    public static void main(String[] args) throws NoSuchMethodException {
        Class cls = TestHello.class;
        Method method = cls.getMethod("main", String[].class);
        Hello hello = method.getAnnotation(Hello.class);
        System.out.println(hello.value());
    }

}