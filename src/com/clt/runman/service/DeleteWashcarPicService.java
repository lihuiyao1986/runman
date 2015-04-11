package com.clt.runman.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;

import com.clt.runman.R;
import com.clt.runman.db.dao.WashcarPicInfoDao;
import com.clt.runman.db.model.WashcarPicInfoDaoModel;
import com.clt.runman.utils.AppContext;
import com.clt.runman.utils.StringUtils;

/**
 * 扫描拍照前和拍照后用户的照片
 * @author yanshengli
 * @since 2015-3-25
 */
public class DeleteWashcarPicService extends IntentService
{
	
	public static final String ACTION = "com.clt.runman.service.DeleteWashcarPicService_ACTION";
	
	public DeleteWashcarPicService()
	{
		this("");
	}
	
	public DeleteWashcarPicService(String name) 
	{
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) 
	{
		AppContext context = (AppContext) getApplicationContext();
		String deletePicEndDate = deletePicEndDate();
        List<WashcarPicInfoDaoModel> picInfoList = WashcarPicInfoDao.newInstance(context).queryPicInfoListBeforeDate(deletePicEndDate);
	    for (WashcarPicInfoDaoModel picInfo : picInfoList) 
	    {
			String filepath = picInfo.getPicUrl();
			String thumbnailPath = picInfo.getThumbnailPath();
			File file = new File(filepath);
			if(file.isFile() && file.exists())
			{
				file.delete();
				File thumbnailFile = new File(thumbnailPath);
				if(thumbnailFile.isFile() && thumbnailFile.exists())
				{
					thumbnailFile.delete();
				}
				WashcarPicInfoDao.newInstance(context).deletePicInfo(picInfo);
			}
		}
	}
	
	
	/**
	 * 获取删除照片的截止日期
	 * @param date
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	private String deletePicEndDate()
	{
		AppContext context = (AppContext) getApplicationContext();
		int day = Integer.parseInt(StringUtils.trimNull(context.getResources().getString(R.string.deleteWashcarPicInfoDay),"2"));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -day);
		return new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
	}

}
