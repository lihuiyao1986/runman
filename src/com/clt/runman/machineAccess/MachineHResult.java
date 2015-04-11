package com.clt.runman.machineAccess;

/**
 *@Description:设备返回字符所代表的含义
 *@Author:张聪
 *@Since:2015年3月30日下午1:37:54
 */
public class MachineHResult {

    /**
     *准备就绪
     */
    public static final String READY      = "r";

    /**
     * 返回成功
     */
    public static final String SUCCESS    = "s";
    /**
     * 失败
     */
    public static final String FAIL       = "f";
    /**
     * 待机
     */
    public static final String STANDBY    = "d";
    /**
     * 工作中
     */
    public static final String WORKING    = "i";

    /**
     * 电量低
     */
    public static final String BATTERYLOW = "w";

    /**
     * 发送t指令，返回成功
     */
    public static final String STOP       = "z";

}
