package com.clt.runman.utils;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class JsonUtils {

	private final static String TAG = JsonUtils.class.getSimpleName();
	
    public static <T> T parseObject(String json,Class<T> classz){
        try {
            T t = JSON.parseObject (json, classz);
            return t;
        } catch (Exception e) {
            MyLog.e(TAG, "parseObject error", e);
        }
        return null;
    }

    public static <T> List<T> parseArray(String json,Class<T> classz){
        try {
            List<T> list = JSON.parseArray (json, classz);
            return list;
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return null;
    }

    /**
     * 将对象转换程json串
     * @param obj
     * @return
     */
    public static String toJsonFromObject(Object obj){
        return JSON.toJSONString (obj, true);
    }
}
