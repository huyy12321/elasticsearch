package com.example.search.util;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * @author Clover
 * @create 2019/8/15
 */
public class JsonUtils {

    /**
     * 把map对象转换为可更新的 esjson串
     * @param map
     * @return
     */
    public static String toUpdateString(Map<String, Object> map) {
        String updateJson = JSON.toJSONString(map);
        return "{\"doc\":" +
                updateJson +
                "}";
    }

    public static String toESUpdateString(Object o) {
        return "{\"doc\":" +
                JSON.toJSON(o) +
                "}";
    }
}
