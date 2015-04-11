package com.clt.runman.machineAccess;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.clt.runman.R;
import com.clt.runman.utils.AppUtils;

/**
 *@Description:设备错误信息描述
 *@Author:张聪
 *@Since:2015年4月1日下午3:47:16
 */
public class MachineErrors {

    /**找不到蓝牙设备**/
    public static final int            BLUETOOTH_NONE           = 101;

    /**打开蓝牙失败**/
    public static final int            BLUETOOTH_OPEN_FAILED    = 102;

    /**蓝牙连接失败**/
    public static final int            BLUETOOTH_CONNECT_FAILED = 103;

    /**蓝牙连接丢失**/
    public static final int            BLUETOOTH_CONNECT_LOST   = 104;

    /**找不到设备**/
    public static final int            MACINE_NOT_FOUND         = 201;

    /**设备连接失败**/
    public static final int            MACINE_CONNECT_FAILED    = 202;
    /**设备还没开始工作**/
    public static final int            MACINE_WORK_FAILED       = 203;
    /**关闭水泵失败**/
    public static final int            MACINE_STOP_FAILED       = 204;
    /**设备电量低**/
    public static final int            MACINE_LOW_POWER         = 206;
    /**设备正在工作中**/
    public static final int            MACINE_NOT_READY         = 207;

    // /**交互设备关闭失败**/
    // public static final int MACINE_OFF_FAILED = 205;

    // /**设备不在工作状态**/
    // public static final int MACINE_NOT_IN_WORK = 208;

    /**
     * 错误描述map
     */
    private static Map<String, String> machineErrorsMap;

    static {
        machineErrorsMap = new HashMap<String, String> ();
        machineErrorsMap.put (String.valueOf (BLUETOOTH_NONE), AppUtils.getContext ().getString (R.string.bluetooth_none));
        machineErrorsMap.put (String.valueOf (BLUETOOTH_OPEN_FAILED), AppUtils.getContext ().getString (R.string.bluetooth_open_failed));
        machineErrorsMap.put (String.valueOf (BLUETOOTH_CONNECT_FAILED), AppUtils.getContext ().getString (R.string.bluetooth_connect_failed));
        machineErrorsMap.put (String.valueOf (BLUETOOTH_CONNECT_LOST), AppUtils.getContext ().getString (R.string.bluetooth_connect_lost));
        machineErrorsMap.put (String.valueOf (MACINE_NOT_FOUND), AppUtils.getContext ().getString (R.string.machine_not_found));
        machineErrorsMap.put (String.valueOf (MACINE_CONNECT_FAILED), AppUtils.getContext ().getString (R.string.machine_connect_failed));
        machineErrorsMap.put (String.valueOf (MACINE_WORK_FAILED), AppUtils.getContext ().getString (R.string.machine_work_failed));
        machineErrorsMap.put (String.valueOf (MACINE_STOP_FAILED), AppUtils.getContext ().getString (R.string.machine_stop_failed));
        machineErrorsMap.put (String.valueOf (MACINE_LOW_POWER), AppUtils.getContext ().getString (R.string.machine_lower_power));
        machineErrorsMap.put (String.valueOf (MACINE_NOT_READY), AppUtils.getContext ().getString (R.string.machine_not_ready));
        // machineErrorsMap.put (String.valueOf (MACINE_OFF_FAILED), AppUtils.getContext ().getString (R.string.machine_off_failed));
        // machineErrorsMap.put (String.valueOf (MACINE_NOT_IN_WORK), AppUtils.getContext ().getString (R.string.machine_not_in_work));
    }

    /**
     * 根据错误码获取错误消息
     * @param errorcode
     * @return
     */
    public static String machineErrorMsg(int errorcode){
        String errorMsg = machineErrorsMap.get (String.valueOf (errorcode));
        return StringUtils.isEmpty (errorMsg) ? "未知错误" : errorMsg;
    }
}
