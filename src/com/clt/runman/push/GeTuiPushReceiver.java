package com.clt.runman.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.clt.runman.push.model.BaseGexinPayLoadBean;
import com.clt.runman.utils.AppUtils;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.DateUtils;
import com.clt.runman.utils.MyLog;
import com.clt.runman.utils.StringUtils;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

/**
 *@Description:个推消息接受Receiver
 *@Author:张聪
 *@Since:2015年1月17日下午3:58:24
 */
public class GeTuiPushReceiver extends BroadcastReceiver 
{
	/** TAG **/
    private final static String TAG = GeTuiPushReceiver.class.getSimpleName();
    
    @Override
    public void onReceive(Context context,Intent intent)
    {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive() action=" + bundle.getInt("action"));
		switch (bundle.getInt(PushConsts.CMD_ACTION)) 
		{
		    case PushConsts.GET_MSG_DATA:
		    	 //1.收到推送消息
		    	 receivePushMessage(context,intent);
			     break;
		    case PushConsts.GET_CLIENTID:
		    	 //2.收到个信给的clientid
		    	 receivePushClientId(context,intent);
			     break;
		    case PushConsts.THIRDPART_FEEDBACK:
		    	 //3.三方接口回调
			     break;
		    default:
			     break;
		}
    }
    
    /**
     * 收到个信的clientId
     * @param context
     * @param intent
     */
    private void receivePushClientId(Context context, Intent intent) 
    {
    	Bundle bundle = intent.getExtras();
    	String clientId = StringUtils.trimNull(bundle.getString ("clientid"));
        MyLog.d (TAG, "clientId为:" + clientId);
        CommonUtils.saveGexinClientId(clientId,AppUtils.getActivity());
	}

	/**
     * 收到个信推送的消息
     * @param context
     * @param intent
     */
	private void receivePushMessage(Context context, Intent intent) 
	{
		Bundle bundle = intent.getExtras();
		byte[] payload = bundle.getByteArray("payload");
		String taskid = bundle.getString("taskid");
		String messageid = bundle.getString("messageid");
		//smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
		boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
		MyLog.d(TAG, "第三方回执接口调用" + (result ? "成功" : "失败"));
		if (payload != null) 
		{
			String data = new String(payload);
			MyLog.d(TAG, "个信推送的消息为：" + data);
			BaseGexinPayLoadBean resultBean = GexinPayLoadParserManager.getInstance().parsePayload(data);
			if(resultBean !=null)
			{
				resultBean.setCreateTime(DateUtils.getNowTimeStamp());
				resultBean.setMessageid(messageid);
				resultBean.setTaskid(taskid);
				GexinPayLoadParserManager.getInstance().dispatchPayload(context, resultBean);
			}
		}
	}

}
