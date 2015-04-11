package com.clt.runman.push;

import android.content.Context;

import com.clt.runman.push.model.BaseGexinPayLoadBean;


/**
 * 解析个信透传消息的解析器
 * @author yanshengli
 * @since liys
 */
public interface GexinPayLoadParser
{
	/**
	 * 解析推送消息
	 * @param message
	 * @return
	 */
	public BaseGexinPayLoadBean parse(String message);
	
	/**
	 * 做消息转发
	 * @param result
	 */
	public void dispatchPayLoad(Context context,BaseGexinPayLoadBean result);
}
