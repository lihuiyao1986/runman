package com.clt.runman.utils;

import android.util.Log;

/**
 *@Description:LOG日志
 *@Author:张聪
 *@Since:2015年1月17日下午3:52:30
 *@deprecate:true显示 ；false 隐藏
 */
public class MyLog 
{

	/**DeBug模式
     * @true：开启
     * @false：关闭
     */
    public static boolean isDeBug     = false;

    /**只是log显示
     * @true：显示
     * @false：隐藏
     */
    public static boolean isLogShow   = true;

    /**只是log显示
     * @true：显示
     * @false：隐藏
     */
    public static boolean getIsLogShow()
    {
        return isLogShow;
    }

    /**只是log显示
     * @true：显示
     * @false：隐藏
     */
    public static void setLogShow(boolean isLogShow)
    {
        MyLog.isLogShow = isLogShow;
    }

    public static boolean isDeBug()
    {
        return isDeBug;
    }

    public static void setDeBug(boolean isDeBug)
    {
        MyLog.isDeBug = isDeBug;
    }

    /**
     * @author Runman
     * @param PAGETAG
     * @param msg
     */
    public static void i(String PAGETAG,String msg)
    {
        if (isLogShow && msg != null) 
        {
            Log.i (PAGETAG, msg);
        }
    }

    /**
     * @author Runman
     * @param PAGETAG
     * @param msg
     * @param e
     */
    public static void i(String PAGETAG,String msg,Throwable e)
    {
        if (isLogShow && msg != null)
        {
            Log.i (PAGETAG, msg, e);
        }
    }


    /**
     * @author Runman
     * @param PAGETAG
     * @param msg
     */
    public static void d(String PAGETAG,String msg)
    {
        if (isLogShow && msg != null && isDeBug) 
        {
            Log.d (PAGETAG, msg);
        }
    }
    
    public static void d(String PAGETAG,String msg,Throwable e)
    {
        if (isLogShow && msg != null && isDeBug) 
        {
            Log.d (PAGETAG, msg,e);
        }
    }

    /**
     * @author Runman
     * @param PAGETAG = LogE = "Runman_EXCEPTION" ;
     * @param msg
     */
    public static void e(String PAGETAG,String msg)
    {
        if (isLogShow && msg != null) 
        {
            Log.e (PAGETAG, msg);
        }
    }


    /**
     * @author Runman
     * @param PAGETAG
     * @param msg
     * @param e
     */
    public static void e(String PAGETAG,String msg,Throwable e)
    {
        if (isLogShow && msg != null)
        {
            Log.e (PAGETAG, msg, e);
        }
    }

    /**
     * @author Runman
     * @param PAGETAG
     * @param msg
     */
    public static void v(String PAGETAG,String msg)
    {
        if (isLogShow && msg != null) 
        {
            Log.v (PAGETAG, msg);
        }
    }

    /**
     * @author Runman
     * @param PAGETAG
     * @param msg
     * @param e
     */
    public static void w(String PAGETAG,String msg,Exception e)
    {
        if (isLogShow && msg != null) 
        {
            Log.w (PAGETAG, msg, e);
        }
    }

    /**
     * @author Runman
     * @param PAGETAG
     * @param msg
     */
    public static void w(String PAGETAG,String msg)
    {
        if (isLogShow && msg != null) 
        {
            Log.w (PAGETAG, msg);
        }
    }

    /**
     * @author Runman
     * @param PAGETAG
     * @param msg
     * @param cause 原因
     */
    public static void w(String PAGETAG,String msg,Throwable cause)
    {
        if (isLogShow && msg != null) 
        {
            Log.w (PAGETAG, msg, cause);
        }
    }

}
