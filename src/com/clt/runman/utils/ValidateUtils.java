package com.clt.runman.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *@Description:参数验证工具类
 *@Author:张聪
 *@Since:2014年12月30日上午11:34:59
 */
public class ValidateUtils {

    // 判断小数
    private static final String NUMBER_PATTERN  = "^[0-9]+(.[0-9]{0,1})?$";
    // 判断数字的正则表达
    private static final String CNUMBER_PATTERN = "^[0-9]*$";

    /**
     *  私有构造器
     */
    private ValidateUtils() {

    }

    /**
     * 验证是不是数字(验证到小数点后一位)
     *
     * @param number
     * @return
     */
    public static boolean isDecimalNumber(String number){
        return match (NUMBER_PATTERN, number);
    }

    /**
     * 验证是不是数字(没有小数点)
     * @param number
     * @return
     */
    public static boolean isInteger(String number){
        return match (CNUMBER_PATTERN, number);
    }

    /**
     * 执行正则表达式
     * @param pattern  表达式
     * @param str s待验证字符串
     * @return 返回 <b>true </b>,否则为 <b>false </b>
     */
    private static boolean match(String pattern,String str){
        Pattern p = Pattern.compile (pattern);
        Matcher m = p.matcher (str);
        return m.find ();
    }

    /**
     * 手机号验证
     * @param  str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str){
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile ("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher (str);
        b = m.matches ();
        return b;
    }

    /**
     * 验证输入的邮箱格式是否符合
     * @param email
     * @return 是否合法
     */
    public static boolean emailFormat(String email){
        boolean tag = true;
        final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        final Pattern pattern = Pattern.compile (pattern1);
        final Matcher mat = pattern.matcher (email);
        if (!mat.find ()) {
            tag = false;
        }
        return tag;
    }

    /**
     * 电话号码验证
     * @param  str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str){
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile ("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
        p2 = Pattern.compile ("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
        if (str.length () > 9) {
            m = p1.matcher (str);
            b = m.matches ();
        } else {
            m = p2.matcher (str);
            b = m.matches ();
        }
        return b;
    }
}
