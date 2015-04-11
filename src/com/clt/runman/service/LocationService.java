package com.clt.runman.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.clt.runman.R;
import com.clt.runman.http.HttpRequestClient;
import com.clt.runman.http.HttpRequestClientCallback;
import com.clt.runman.interfaces.BaiMapLocationCallBack;
import com.clt.runman.model.BaseRespBean;
import com.clt.runman.model.RunMan;
import com.clt.runman.model.RunmanLocationBean;
import com.clt.runman.utils.AppContext;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.DateUtils;
import com.clt.runman.utils.MyLog;
import com.clt.runman.utils.StringUtils;

/**
 * 定位服务
 * @author yanshengli
 * @since 2015-3-3
 */
public class LocationService extends Service
{
	private static final String TAG = LocationService.class.getSimpleName();
	
	/** ACTION **/
	public static final String ACTION = "com.clt.runman.service.LocationService_ACTION";

	/** binder **/
	private IBinder binder = new LocalBinder();

	/** 计时器 **/
	private Timer timer;

	/** 线程 **/
	private Thread thread;

	@Override
	public IBinder onBind(Intent intent) 
	{
		return binder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		thread = null;
		stopTimer();
		final AppContext context = (AppContext) getApplicationContext();
		String updateLocInterval = StringUtils.trimNull(context.getResources().getString(R.string.update_runman_loc_interval),String.valueOf(60*10));
		final int locUpdateInterval = Integer.parseInt(updateLocInterval);
		thread = new Thread(new Runnable() 
		{
			@Override
			public void run()
			{
				stopTimer();
				timer = new Timer();
				timer.schedule(new TimerTask() 
				{
					@Override
					public void run() {
						context.startLocation(null,new BaiMapLocationCallBack() 
						{
						     @Override
							 public void onLocationReceived(BDLocation location,LocationClient client) 
						     {
							    double longitude = location.getLongitude();
							    double latitude = location.getLatitude();
							    String city = StringUtils.trimNull(location.getCity());
								String address = StringUtils.trimNull(location.getAddrStr());
								RunmanLocationBean locBean = new RunmanLocationBean();
								locBean.setAddress(address);
								locBean.setCreatetime(DateUtils.getNowTimeStamp());
								locBean.setLatitude(String.valueOf(latitude));
								locBean.setLongitude(String.valueOf(longitude));
								locBean.setCity(city);
								RunMan runman = CommonUtils.getLoginedRunManInfo(context);
								if (runman != null)
								{
									locBean.setRunmanId(runman.getRunManId());
								}
								MyLog.i(TAG, locBean.toString());
								// 保存跑男的地理位置信息
								CommonUtils.saveRunmanLocInfo(locBean,context);
								// 上传跑男的地理位置信息
								uploadRunmanLocation(locBean);
							}
						});
					}
				}, 0, locUpdateInterval * 1000);
			}
		});
		thread.start();
		return super.onStartCommand(intent, flags, startId);
	}
	

	/**
	 * 上传跑男的地理位置信息
	 */
	private void uploadRunmanLocation(RunmanLocationBean locBean) 
	{
		AppContext context = (AppContext) getApplicationContext();
		RunMan runman = CommonUtils.getLoginedRunManInfo(context);
		String needUploadRunmanLocFlag = CommonUtils.getUploadRunmanLocFlag(context);
		if(runman != null && !"false".equalsIgnoreCase(needUploadRunmanLocFlag))
		{
			String url = context.getResources().getString(R.string.uploadRunmanLocUrl);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			NameValuePair runmanid = new BasicNameValuePair("runmanid", StringUtils.trimNull(runman.getRunManId()));
			NameValuePair longitude = new BasicNameValuePair("longitude", StringUtils.trimNull(locBean.getLongitude()));
			NameValuePair latitude = new BasicNameValuePair("latitude", StringUtils.trimNull(locBean.getLatitude()));
			params.add(latitude);
			params.add(longitude);
			params.add(runmanid);
			HttpRequestClient.doPost(context,params, url, BaseRespBean.class, 0, new HttpRequestClientCallback() 
			{
				@Override
				public void httpRespSuccess(BaseRespBean result, int reqTag, Class<?> clazz) 
				{
					MyLog.i(TAG, "上传跑男的地理位置成功");
				}
				@Override
				public void httpRespFail(String errorcode, String errorMsg, int reqTag) 
				{
					MyLog.e(TAG, "上传跑男的地理位置失败");
				}
				@Override
				public void httpReqBegin(int reqTag) 
				{
					
				}
				@Override
				public void httpOnloading(long count, long current, int reqTag)
				{
					
				}
				@Override
				public void httpOnProgress(boolean progress, int rate, int reqTag) 
				{
					
				}
			});
		}
	}
	
	@Override
	public void onDestroy()
	{
		stopTimer();
		super.onDestroy();
	}

	/**
	 *  定义内容类继承Binder
	 *  @author yanshengli
	 *  @since 2015-3-3
	 */
	public class LocalBinder extends Binder
	{
		// 返回本地服务
		LocationService getService() 
		{
			return LocationService.this;
		}
	}
	
	/**
	* 停止计时器
	*/
	private void stopTimer() 
	{
		if (timer != null)
		{
			timer.cancel();
			timer = null;
		}
	}

}
