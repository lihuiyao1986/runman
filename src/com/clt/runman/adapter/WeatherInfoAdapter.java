package com.clt.runman.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.model.WeatherInfoBean;
import com.clt.runman.utils.StringUtils;

/***
 * 天气信息对应的adapter
 * @author yanshengli
 * @since 2015-3-4
 */
public class WeatherInfoAdapter extends RunManBaseAdapter<WeatherInfoBean> 
{
	public WeatherInfoAdapter(Context context, List<WeatherInfoBean> data,int resource) 
	{
		super(context, data, resource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		WeatherInfoHolderView holderView = null;
		WeatherInfoBean weatherInfo = listData.get(position);
		if(convertView == null)
		{
			convertView = listContainer.inflate(itemViewResource, null);
			holderView = new WeatherInfoHolderView();
			holderView.weatherDescTv = (TextView)convertView.findViewById(R.id.weather_info_list_item_weather_desc_tv);
			holderView.weatherLogo = (ImageView)convertView.findViewById(R.id.weather_info_list_item_pic_iv);
			holderView.dateTv = (TextView)convertView.findViewById(R.id.weather_info_list_item_date_tv);
			holderView.temperatureTv = (TextView)convertView.findViewById(R.id.weather_info_list_item_temperature_tv);
			convertView.setTag(holderView);
		}
		else
		{
			holderView = (WeatherInfoHolderView) convertView.getTag();
		}
		display(holderView,weatherInfo);
		return convertView;
	}
	
	/**
	 * 展示
	 * @param holderView
	 * @param weatherInfo
	 */
	private void display(WeatherInfoHolderView holderView,WeatherInfoBean weatherInfo) 
	{
		holderView.dateTv.setText(StringUtils.trimNull(weatherInfo.getDate(), "--"));
		holderView.temperatureTv.setText(StringUtils.trimNull(weatherInfo.getTemperature(), "--"));
		String weatherDesc = StringUtils.trimNull(weatherInfo.getWeather());
		holderView.weatherDescTv.setText(StringUtils.trimNull(weatherDesc,"--"));
		if(!StringUtils.isEmpty(weatherInfo.getDayPictureUrl()))
		{
			FinalBitmap.create (context).display (holderView.weatherLogo, weatherInfo.getDayPictureUrl());
		}
	}

	/**
	 * WeatherInfoHolderView类
	 * @author yanshengli
	 *
	 */
	static class WeatherInfoHolderView 
	{
		public ImageView weatherLogo;//天气情况的图标
		public TextView dateTv;//日期
		public TextView temperatureTv;//温度
		public TextView weatherDescTv;//天气描述
	}

}
