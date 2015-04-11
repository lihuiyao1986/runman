package com.clt.runman.push;

import android.content.Context;

import com.clt.runman.push.model.BaseGexinPayLoadBean;
import com.clt.runman.push.model.PushType;
import com.clt.runman.utils.StringUtils;

/**
 * 解析个信推送得到的消息解析器manager
 * @author yanshengli
 * @since 2015-1-31
 */
public class GexinPayLoadParserManager
{
	
	/** 实体对象 **/
	private static GexinPayLoadParserManager instance;
	
	/**
	 * 私有的构造方法 
	 */
	private GexinPayLoadParserManager()
	{
		
	}
	
	/**
	 * 获取实体对象
	 * @return
	 */
	public static GexinPayLoadParserManager getInstance()
	{
		if(instance == null)
		{
			instance = new GexinPayLoadParserManager();
		}
		return instance;
	}
	
	/**
	 * 解析
	 * @param message
	 * @return
	 */
	public BaseGexinPayLoadBean parsePayload(String message)
	{
		if(!StringUtils.isEmpty(message))
		{
			String type = StringUtils.trimNull(message.split("&")[0].split("=")[1]);
			GexinPayLoadParser parser = getParserByType(type);
			if(parser!=null)
			{
				return parser.parse(message);
			}
		}
		return null;
	}
	
	/**
	 * 个信消息转发
	 * @param context
	 * @param result
	 */
	public void dispatchPayload(Context context,BaseGexinPayLoadBean result)
	{
		String type = StringUtils.trimNull(result.getType());
		if (!StringUtils.isEmpty(type)) 
		{
			GexinPayLoadParser parser = getParserByType(type);
			if (parser != null) 
			{
				parser.dispatchPayLoad(context, result);
			}
		}
	}

	/**
	 * 根据消息类型获取对应的解析器
	 * @param message
	 * @return
	 */
	private GexinPayLoadParser getParserByType(String type)
	{
		if(StringUtils.isEmpty(type))
		{
			return null;
		}
		if(PushType.ORDER_PUSH_CODE.equals(type))
		{
			 return new OrderDispatchPayLoadParser();
		}
		return null;
	}
}
