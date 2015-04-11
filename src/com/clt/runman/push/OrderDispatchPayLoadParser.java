package com.clt.runman.push;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.clt.runman.R;
import com.clt.runman.activity.index.OrderReceive2Activity;
import com.clt.runman.activity.index.OrderReceiveActivity;
import com.clt.runman.push.model.BaseGexinPayLoadBean;
import com.clt.runman.push.model.OrderDispatchPayLoadBean;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.AppUtils;
import com.clt.runman.utils.ReflectionUtils;
import com.clt.runman.utils.StringUtils;

/**
 * 解析派单推送结果的解析器
 * @author yanshengli
 * @since 2015-3-31
 */
public class OrderDispatchPayLoadParser implements GexinPayLoadParser {

    public static final int Order_Dispatch_Notify_Id = 10001;

    /**
     * 解析接口
     */
    @Override
    public BaseGexinPayLoadBean parse(String message){
        return parseGexinPayLoad (message);
    }

    /**
    * 解析透传消息
    */
    private OrderDispatchPayLoadBean parseGexinPayLoad(String message){
        OrderDispatchPayLoadBean bean = new OrderDispatchPayLoadBean ();
        String[] array = message.split ("&");
        for ( String temp : array ) {
            if (!StringUtils.isEmpty (temp)) {
                int position = temp.indexOf ("=");
                if (position != -1) {
                    String fieldName = StringUtils.trimNull (temp.substring (0, position));
                    String fieldValue = StringUtils.trimNull (temp.substring (position + 1, temp.length ()));
                    if ("type".equals (fieldName)) {
                        bean.setType (fieldValue);
                        continue;
                    }
                    Method method = ReflectionUtils.getFieldSetMethod (OrderDispatchPayLoadBean.class, fieldName);
                    try {
                        method.invoke (bean, fieldValue);
                    } catch (Exception e) {}
                }
            }
        }
        String bookTime = bean.getBookingTime ();
        if (!StringUtils.isEmpty (bookTime)) {
            String[] items = bookTime.split ("-");
            bean.setServiceBeginTime (items[0]);
            bean.setServiceEndTime (items[1]);
        }
        return bean;
    }

    /**
     * 做消息转发
     */
    @Override
    public void dispatchPayLoad(Context context,BaseGexinPayLoadBean result){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService (android.content.Context.NOTIFICATION_SERVICE);

        // 创建通知
        Notification notification = new Notification (R.drawable.notify_status_icon,"您有新订单",System.currentTimeMillis ());

        // 设置自定义布局
        String packageName = context.getApplicationContext ().getPackageName ();
        RemoteViews contentView = new RemoteViews (packageName,R.layout.order_dispatch_notification);
        notification.contentView = contentView;

        // 设置跳转页面
        Intent intent = new Intent (context,PushActivity.class);
        Bundle bundle = new Bundle ();
        bundle.putSerializable ("data", result);
        bundle.putString (OrderReceiveActivity.source_from_notify_key, OrderReceiveActivity.source_from_notify_value);
        intent.putExtras (bundle);
        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity (context, R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = contentIntent;
        // notification.setLatestEventInfo(context,"您有新订单", "赶紧接单，否则误大事了。", contentIntent);

        // 设置声音
        notification.sound = Uri.parse ("android.resource://" + packageName + "/" + R.raw.notificationsound);

        // 通知时发出的振动
        long[] vir = { 1000, 1000, 1000, 1000 };
        notification.vibrate = vir;

        // 设置闪屏
        notification.ledARGB = Color.BLUE;
        notification.ledOffMS = 0;
        notification.ledOnMS = 1;
        notification.flags = notification.flags | Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;

        // 发送通知
        notificationManager.notify (Order_Dispatch_Notify_Id, notification);

        /***
         * 如果当前在接单页面，则要更新
         */
        Activity activity = AppUtils.getActivity ();
        if (activity != null) {
            String currentActivity = AppUtils.getActivity ().getClass ().getSimpleName ();
            String orderReceiveActivity = OrderReceive2Activity.class.getSimpleName ();
            if (currentActivity.equals (orderReceiveActivity)) {
                // 发送特定action的广播
                Intent intentActivity = new Intent ();
                intentActivity.setAction (AppConstant.Order_Dispatch_Action);
                intentActivity.putExtras (bundle);
                context.sendBroadcast (intentActivity);
            }
        }
    }
}
