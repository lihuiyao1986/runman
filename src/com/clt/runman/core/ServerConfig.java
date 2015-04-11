package com.clt.runman.core;

import com.clt.runman.R;
import com.clt.runman.utils.AppUtils;

public class ServerConfig {

    // 请求API对应的地址
    public static String API_HOST    = AppUtils.getContext ().getString (R.string.APIHOST);

    // 请求图片的图片服务器API地址
    public static String IMG_HOST    = AppUtils.getContext ().getString (R.string.IMGHOST);

    // 文件上传的服务器API地址
    public static String UPLOAD_HOST = AppUtils.getContext ().getString (R.string.UPLOADHOST);
}
