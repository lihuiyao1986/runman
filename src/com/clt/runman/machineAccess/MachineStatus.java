package com.clt.runman.machineAccess;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.clt.runman.R;
import com.clt.runman.utils.AppUtils;

/**
 * Created by yanshengli on 15-1-17.
 * 机器的连接状态
 */
public class MachineStatus {

    /**
     * 关机
     */
    public static final int            OFF         = 0;
    /**
     * 打开中
     */
    public static final int            OPENNING    = 1;
    /**
     * 蓝牙扫描中
     */
    public static final int            SCANNING    = 2;
    /**
     * 蓝牙连接中
     */
    public static final int            CONNECTTING = 3;
    /**
     * 蓝牙已连接
     */
    public static final int            CONNECTED   = 4;
    /**
     * 设备配对中
     */
    public static final int            MATCHING    = 5;
    /**
     * 设备已配对
     */
    public static final int            MATCHED     = 6;
    /**
     * 设备准备就绪
     */
    public static final int            READY       = 7;
    /**
     * 工作中
     */
    public static final int            TURN_WORK   = 8;
    /**
     * 停止工作
     */
    public static final int            STOP        = 9;
    /**
     * 正在关机
     */
    public static final int            TURN_OFF    = 10;

    // /**
    // * 正在切换停止状态
    // */
    // public static final int TURN_STOP = 10;
    // /**
    // * 设备处于停止状态
    // */
    // public static final int STOP = 11;
    // /**
    // * 正在关机
    // */
    // public static final int TURN_OFF = 12;
    /**
     * 存放设备状态码和描述的map
     */
    private static Map<String, String> machineStatusMap;

    /**
     * 设置状态map
     */
    static {
        machineStatusMap = new HashMap<String, String> ();
        machineStatusMap.put (String.valueOf (OFF), AppUtils.getActivity ().getString (R.string.machine_status_off));
        machineStatusMap.put (String.valueOf (OPENNING), AppUtils.getActivity ().getString (R.string.machine_status_openning));
        machineStatusMap.put (String.valueOf (SCANNING), AppUtils.getActivity ().getString (R.string.machine_status_booth_scanning));
        machineStatusMap.put (String.valueOf (CONNECTTING), AppUtils.getContext ().getString (R.string.machine_status_booth_connecting));
        machineStatusMap.put (String.valueOf (CONNECTED), AppUtils.getContext ().getString (R.string.machine_status_booth_connected));
        machineStatusMap.put (String.valueOf (MATCHING), AppUtils.getActivity ().getString (R.string.machine_status_matching));
        machineStatusMap.put (String.valueOf (MATCHED), AppUtils.getContext ().getString (R.string.machine_status_matched));
        machineStatusMap.put (String.valueOf (READY), AppUtils.getContext ().getString (R.string.machine_status_ready));
        machineStatusMap.put (String.valueOf (TURN_WORK), AppUtils.getContext ().getString (R.string.machine_status_switch_working));
        machineStatusMap.put (String.valueOf (STOP), AppUtils.getContext ().getString (R.string.machine_status_stopped));
        machineStatusMap.put (String.valueOf (TURN_OFF), AppUtils.getContext ().getString (R.string.machine_status_offlining));
        // machineStatusMap.put (String.valueOf (TURN_STOP), AppUtils.getContext ().getString (R.string.machine_status_switch_stop));
        // machineStatusMap.put (String.valueOf (WORK), AppUtils.getContext ().getString (R.string.machine_status_working));

    }

    /**
     * 根据状态码获取对应的状态描述
     * @param status
     * @return
     */
    public static String statusDesc(int status){
        String statusDesc = machineStatusMap.get (String.valueOf (status));
        return StringUtils.isEmpty (statusDesc) ? "未知状态" : statusDesc;
    }
}
