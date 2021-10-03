package com.example.seckill.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String obj2Json(Object obj) {
        String str = null;
        try {
            str = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static <T> T json2Obj(String src, Class<T> clazz) {
        T obj = null;
        try {
            obj = objectMapper.readValue(src.getBytes(StandardCharsets.UTF_8), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
