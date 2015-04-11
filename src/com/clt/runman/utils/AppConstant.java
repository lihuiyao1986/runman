package com.clt.runman.utils;

/**
 *@Description:常量类
 *@Author:张聪
 *@Since:2015年1月10日上午11:20:48
 */
public class AppConstant {

    /*************http 请求返回状态码***************/
    /**
     * 返回成功
     */
    public final static String SUCCESS_CODE                         = "0";

    /*************SharePreference 缓存文件名称***************/

    // 保存SharePreference的文件名
    public final static String sharePreferenceFileName              = "runManSharePrefence";

    // 记住密码标识
    public final static String REMEMBER_PWD_FLAG                    = "rememberPwdFlag";

    // 保存后台返回的登录信息
    public final static String LOGIN_AFTER                          = "loginAfter";

    // 登录密码
    public final static String LOGIN_PWD                            = "loginPwd";

    // 登录账号
    public final static String LOGIN_ACCOUNT                        = "loginAccount";

    // 自动登录checkBox
    public final static String AUTOLOGIN_CHECK                      = "autoLoginCheck";

    // 客户端登录信息
    public final static String LOGIN_BEFORE                         = "loginBefore";

    // 个推ClientID
    public final static String GEXIN_CLIENTID                       = "clientId";

    // 当前已接单的订单号
    public final static String ORDER_ID_RECEIVED                    = "orderIdReceived";

    // 跑男信息保存
    public final static String LOGINED_RUN_MAN_INFO                 = "loginedRunmanInfo";

    // 跑男的地址位置信息
    public final static String RUNMAN_LOCATION_INFO                 = "runmanLocationInfo";

    // 登录后的token信息
    public final static String LOGIN_USER_TOKEN                     = "loginUserToken";

    // 是否需要上传跑男位置的表识
    public final static String NEED_UPLOAD_RUNMAN_FLAG              = "needUploadRunmanFlag";

    // 设备对象信息
    public final static String EQUIPMENT_INFO                       = "equipmentInfo";

    // 是否是第一次使用app
    public final static String FIRST_USE_FLAG                       = "firstUseFlag";

    /**仅wifi上传标识**/
    public final static String UPLOAD_ONLY_WIFI                     = "uploadOnlyByWifi";

    /************* 订单状态  ***************/
    /**
     * 待接单
     */
    public final static int    ORDER_WAIT_LIST                      = 0;

    /**
     * 已接单
     */
    public final static int    ORDER_RECEIVED                       = 2000;

    /**
     * 已达到
     */
    public final static int    ORDER_ARRIVE                         = 2010;

    /**
     * 已找到
     */
    public final static int    ORDER_FOUND                          = 2020;

    /**
     * 洗车前拍照
     */
    public final static int    ORDER_WASH_BEFORE                    = 2030;

    /**
     * 开始洗车
     */
    public final static int    ORDER_WASH_CAR                       = 2040;

    /**
     * 洗车完成
     */
    public final static int    ORDER_FINISH_WORK                    = 2050;

    /**
     * 洗车后拍照
     */
    public final static int    ORDER_WASH_AFTER                     = 3000;

    /************* 跑男工作状态  ***************/
    /**
     * 空闲
     */
    public final static int    RUNMAN_STATUS_FREE                   = 10;
    /**
     * 忙碌
     */
    public final static int    RUNMAN_STATUS_BUSY                   = 20;
    /**
     * 下班
     */
    public final static int    RUNMAN_STATUS_OFFDUTY                = 30;

    /************* 注册的通知  ***************/

    /**
     * 派单通知
     */
    public final static String RECEIVE_ORDER_NOTIFICATION_NAME      = "android.intent.action.DISPATCH_RECEIVER";

    /**
     * 订单派送通知的action
     */
    public final static String Order_Dispatch_Action                = "com.clt.runman.activity.index.Order_Dispatch_Action";

    /************* 支付方式 ***************/
    /**
     * 支付方式:0现金
     */
    public final static int    PAYTYPE_CURRENCY                     = 0;
    /**
     * 支付方式:1体验券
     */
    public final static int    PAYTYPE_EXPERIENCE_TICKET            = 1;
    /**
     * 支付方式:2微信支付
     */
    public final static int    PAYTYPE_WECHAT_PAYMENT               = 2;

    /** 洗车后照片 **/
    public final static int    washcar_after_type                   = 1;

    /** 洗车前照片 **/
    public final static int    washcar_before_type                  = 0;

    /** 洗车前整体照片类型 **/
    public final static int    washcar_before_whole_subtype         = 0;

    /** 洗车前正前照片类型 **/
    public final static int    washcar_before_center_front_subtype  = 1;

    /** 洗车前左侧照片类型 **/
    public final static int    washcar_before_left_subtype          = 2;

    /** 洗车前正后照片类型 **/
    public final static int    washcar_before_center_behind_subtype = 3;

    /** 洗车右侧照片类型 **/
    public final static int    washcar_before_right_subtype         = 4;

    /** 洗车顶部照片类型 **/
    public final static int    washcar_before_top_subtype           = 5;

    /** 预约单到期消息提醒 **/
    public final static int    remind_type_book_due                 = 0;

    /** 跑男钱已入账消息提醒 **/
    public final static int    remind_type_money_in                 = 1;
}
