package com.clt.runman.interfaces;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;

/**
 * 百度地图定位回调接口
 * @author yanshengli
 * @since 2015-1-30
 */
public interface BaiMapLocationCallBack 
{

	/**
	 * 收到百度地图定位的回调
	 * @param location
	 * @param client
	 */
	public void onLocationReceived(BDLocation location,LocationClient client);
}
