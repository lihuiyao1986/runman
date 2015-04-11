package com.clt.runman.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.clt.runman.R;
import com.clt.runman.activity.index.OrderReceiveActivity;
import com.clt.runman.core.BaseActivity;
import com.clt.runman.model.PushedOrderInfo;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.AppUtils;


public class RemindService {
    
    public static final int NOTIFICATION_ID = 10001;
    public static String carNum = "";
    public static String distance = "";
    public static String dispatchTime = "";
    public static String orderId = "";
    public static String orderAddress = "";
    
    public static void dispatchNotification(Context context) {
        Notification notification = new Notification(R.drawable.login_run, "您有新订单", System.currentTimeMillis()); 
        notification.setLatestEventInfo(context, "您有新订单", "赶紧接单，否则误大事了。", null);
        //自定义声音 
        //notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "10");
        String packageName = context.getApplicationContext().getPackageName();
        notification.sound = Uri.parse("android.resource://" + packageName + "/" +R.raw.notificationsound);

        //通知时发出的振动 
        long[] vir = {1000,1000,1000,1000,1000,1000,1000};
        notification.vibrate = vir;
        
        //设置闪屏
        notification.ledARGB = Color.BLUE;
        notification.ledOffMS= 0;
        notification.ledOnMS = 1;
        notification.flags = notification.flags | Notification.FLAG_SHOW_LIGHTS;
        
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
        
        //组装订单
        PushedOrderInfo order = new PushedOrderInfo();
        order.setCarNum(carNum);
        order.setOrderId(orderId);
        order.setDistance(distance);
        order.setDispatchTime(dispatchTime);
        order.setCarPosition(orderAddress);
        Bundle bundle = new Bundle();
        bundle.putSerializable("incomingOrder", order);
        
        //发送特定action的广播  
        Intent intentActivity = new Intent();  
        intentActivity.setAction(AppConstant.RECEIVE_ORDER_NOTIFICATION_NAME);
        intentActivity.putExtras(bundle);
        context.sendBroadcast(intentActivity);
        
        //跳转到对应的activity
        String currentActivity = AppUtils.getActivity().getClass().getSimpleName();
        String orderReceiveActivity = OrderReceiveActivity.class.getSimpleName();
        if(!currentActivity.equals(orderReceiveActivity))
        {
        	BaseActivity baseActivity = (BaseActivity) AppUtils.getActivity();
        	baseActivity.redirectToTargetActivity(OrderReceiveActivity.class, bundle);
        }
    }
    
    public static void cancelDispatchNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID); 
    }
}
