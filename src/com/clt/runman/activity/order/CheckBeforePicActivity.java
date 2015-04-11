package com.clt.runman.activity.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.clt.runman.R;
import com.clt.runman.core.BaseActivity;
import com.clt.runman.db.dao.WashcarPicInfoDao;
import com.clt.runman.db.model.WashcarPicInfoDaoModel;
import com.clt.runman.model.OrderQueryDetailBean;
import com.clt.runman.model.RunMan;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.BitmapCache;
import com.clt.runman.utils.BitmapCache.ImageCallback;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.PictureUtils;
import com.clt.runman.utils.StringUtils;
import com.clt.runman.utils.SwipeDirect;

/**
 * 检查洗车前照片
 * @author yanshengli
 * @since  2015-3-21
 */
public class CheckBeforePicActivity extends BaseActivity implements OnClickListener
{

	/** 当前订单 **/
	private OrderQueryDetailBean orderItem;
	
	/** 整体照片**/
    private ImageButton washcar_before_0_0_0;
    
    /** 正前照片 **/
    private ImageButton washcar_before_0_1_0;
    private ImageButton washcar_before_0_1_1;
    private ImageButton washcar_before_0_1_2;
    
    /** 左侧照片 **/
    private ImageButton washcar_before_0_2_0;
    private ImageButton washcar_before_0_2_1;
    private ImageButton washcar_before_0_2_2;
    
    /** 正后照片 **/
    private ImageButton washcar_before_0_3_0;
    private ImageButton washcar_before_0_3_1;
    private ImageButton washcar_before_0_3_2;
    
    /** 右侧照片 **/
    private ImageButton washcar_before_0_4_0;
    private ImageButton washcar_before_0_4_1;
    private ImageButton washcar_before_0_4_2;
    
    /** 顶部照片 **/
    private ImageButton washcar_before_0_5_0;
    private ImageButton washcar_before_0_5_1;
    private ImageButton washcar_before_0_5_2;
    
    /** 返回按钮 **/
    private Button check_before_pic_nav_bar_left_btn;
    
    /** 按钮 **/
    private Map<String,ImageButton> buttons;
    
    /** 图片路径 **/
    private Map<String,String> picFilePaths;
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//1.初始化视图
		initUIView();
		
		//2.初始化数据
		initData();
		
		//3.注册监听器
		initListeners();
	}

	/**
	 * 注册监听器
	 */
	private void initListeners() 
	{
		//返回按钮
		check_before_pic_nav_bar_left_btn.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
				CommonUtils.outAnim ();
			}
		});
		
        washcar_before_0_0_0.setOnClickListener(this);
    	
    	washcar_before_0_1_0.setOnClickListener(this);
      	washcar_before_0_1_1.setOnClickListener(this);
      	washcar_before_0_1_2.setOnClickListener(this);
      	
      	washcar_before_0_2_0.setOnClickListener(this);
      	washcar_before_0_2_1.setOnClickListener(this);
      	washcar_before_0_2_2.setOnClickListener(this);
      	
      	washcar_before_0_3_0.setOnClickListener(this);
      	washcar_before_0_3_1.setOnClickListener(this);
      	washcar_before_0_3_2.setOnClickListener(this);
      	
      	washcar_before_0_4_0.setOnClickListener(this);
      	washcar_before_0_4_1.setOnClickListener(this);
      	washcar_before_0_4_2.setOnClickListener(this);
      	
      	washcar_before_0_5_0.setOnClickListener(this);
      	washcar_before_0_5_1.setOnClickListener(this);
      	washcar_before_0_5_2.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initData() 
	{
		//1.获取订单信息
		Bundle bundle = getIntent().getExtras();
		orderItem = (OrderQueryDetailBean) bundle.get("orderItem");
		
		//2.初始化按钮
    	buttons = new HashMap<String, ImageButton>();
    	buttons.put("0_0_0", washcar_before_0_0_0);
    	buttons.put("0_1_0", washcar_before_0_1_0);
    	buttons.put("0_1_1", washcar_before_0_1_1);
    	buttons.put("0_1_2", washcar_before_0_1_2);
    	buttons.put("0_2_0", washcar_before_0_2_0);
    	buttons.put("0_2_1", washcar_before_0_2_1);
    	buttons.put("0_2_2", washcar_before_0_2_2);
    	buttons.put("0_3_0", washcar_before_0_3_0);
    	buttons.put("0_3_1", washcar_before_0_3_1);
    	buttons.put("0_3_2", washcar_before_0_3_2);
    	buttons.put("0_4_0", washcar_before_0_4_0);
    	buttons.put("0_4_1", washcar_before_0_4_1);
    	buttons.put("0_4_2", washcar_before_0_4_2);
    	buttons.put("0_5_0", washcar_before_0_5_0);
    	buttons.put("0_5_1", washcar_before_0_5_1);
    	buttons.put("0_5_2", washcar_before_0_5_2);
    	
    	//3.初始化之前拍照的图片
    	initPrePicsInfoList();
    	
	}
	
	/**
     * 初始化之前拍照的图片
     */
	private void initPrePicsInfoList() 
	{
		// 3.查询图片集合
		List<WashcarPicInfoDaoModel> list = queryPicInfoList(AppConstant.washcar_before_type);
		for (WashcarPicInfoDaoModel washcarPicInfoDaoModel : list) 
		{
			int type = washcarPicInfoDaoModel.getPicType();
			int subtype = washcarPicInfoDaoModel.getPicSubtype();
			int index = washcarPicInfoDaoModel.getPicIndex();
			String key = String.valueOf(type) + "_" + String.valueOf(subtype) + "_" + String.valueOf(index);
			String thumbnailPath = StringUtils.trimNull(washcarPicInfoDaoModel.getThumbnailPath());
			String filePath = StringUtils.trimNull(washcarPicInfoDaoModel.getPicUrl());
			if(StringUtils.isEmpty(thumbnailPath))
			{
				continue;
			}
			if(picFilePaths == null)
			{
				picFilePaths = new HashMap<String, String>();
			}
			picFilePaths.put(key, StringUtils.trimNull(filePath));
			ImageButton button = buttons.get(key);
			BitmapCache.getInstance(this).displayBmpByFilePath(button, thumbnailPath, new ImageCallback() 
			{
				@Override
				public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) 
				{
					imageView.setImageBitmap(bitmap);
				}
				
				@Override
				public Bitmap getBitMapFromFile(String sourcePath) 
				{
					return PictureUtils.getBitmapByPath(sourcePath);
				}
			});
		}
	}

	/**
	 * 初始化视图
	 */
	private void initUIView() 
	{
		//1.布局文件
		setContentView(R.layout.check_before_pic_info_activity);
		
		//2.初始化按钮
		initTakePicBtn();
		
		//3.返回按钮
		check_before_pic_nav_bar_left_btn = (Button)findViewById(R.id.check_before_pic_nav_bar_left_btn);
	}
	
	/**
     * 初始化按钮
     */
    private void initTakePicBtn() 
    {
    	washcar_before_0_0_0 = (ImageButton)findViewById(R.id.washcar_before_0_0_0);
    	washcar_before_0_0_0.setTag("0_0_0");
    	
    	washcar_before_0_1_0 = (ImageButton)findViewById(R.id.washcar_before_0_1_0);
    	washcar_before_0_1_0.setTag("0_1_0");
    	washcar_before_0_1_1 = (ImageButton)findViewById(R.id.washcar_before_0_1_1);
    	washcar_before_0_1_1.setTag("0_1_1");
    	washcar_before_0_1_2 = (ImageButton)findViewById(R.id.washcar_before_0_1_2);
    	washcar_before_0_1_2.setTag("0_1_2");
    	
    	washcar_before_0_2_0 = (ImageButton)findViewById(R.id.washcar_before_0_2_0);
    	washcar_before_0_2_0.setTag("0_2_0");
    	washcar_before_0_2_1 = (ImageButton)findViewById(R.id.washcar_before_0_2_1);
    	washcar_before_0_2_1.setTag("0_2_1");
    	washcar_before_0_2_2 = (ImageButton)findViewById(R.id.washcar_before_0_2_2);
    	washcar_before_0_2_2.setTag("0_2_2");
    	
    	washcar_before_0_3_0 = (ImageButton)findViewById(R.id.washcar_before_0_3_0);
    	washcar_before_0_3_0.setTag("0_3_0");
    	washcar_before_0_3_1 = (ImageButton)findViewById(R.id.washcar_before_0_3_1);
    	washcar_before_0_3_1.setTag("0_3_1");
    	washcar_before_0_3_2 = (ImageButton)findViewById(R.id.washcar_before_0_3_2);
    	washcar_before_0_3_2.setTag("0_3_2");
    	
    	washcar_before_0_4_0 = (ImageButton)findViewById(R.id.washcar_before_0_4_0);
    	washcar_before_0_4_0.setTag("0_4_0");
    	washcar_before_0_4_1 = (ImageButton)findViewById(R.id.washcar_before_0_4_1);
    	washcar_before_0_4_1.setTag("0_4_1");
    	washcar_before_0_4_2 = (ImageButton)findViewById(R.id.washcar_before_0_4_2);
    	washcar_before_0_4_2.setTag("0_4_2");
    	
    	washcar_before_0_5_0 = (ImageButton)findViewById(R.id.washcar_before_0_5_0);
    	washcar_before_0_5_0.setTag("0_5_0");
    	washcar_before_0_5_1 = (ImageButton)findViewById(R.id.washcar_before_0_5_1);
    	washcar_before_0_5_1.setTag("0_5_1");
    	washcar_before_0_5_2 = (ImageButton)findViewById(R.id.washcar_before_0_5_2);
    	washcar_before_0_5_2.setTag("0_5_2");
	}
	
	/**
	 * 查询之前拍照过的照片信息列表
	 */
	private List<WashcarPicInfoDaoModel> queryPicInfoList(int picType) 
	{
		WashcarPicInfoDaoModel picInfo = new WashcarPicInfoDaoModel();
		RunMan runMan = CommonUtils.getLoginedRunManInfo (this);
		String orderTime = orderItem.getOrderTime();
		picInfo.setPicDate(orderTime.substring(0,8));//日期
		picInfo.setRunmanid(runMan.getRunManId());//跑男ID
		picInfo.setOrderid(orderItem.getOrderId());//订单号
		picInfo.setPicType(picType);
		return WashcarPicInfoDao.newInstance(this).queryPicInfoList(picInfo);
	}

	/**
	 * 按钮被点击
	 */
	@Override
	public void onClick(View v)
	{
		if(v instanceof ImageButton)
		{
			ImageButton btn = (ImageButton)v;
			String tag = StringUtils.trimNull((String) btn.getTag());
			String picUrl = StringUtils.trimNull(picFilePaths.get(tag));
			if(!StringUtils.isEmpty(picUrl))
			{
				Bundle bundle = new Bundle();
				bundle.putString("picUrl", picUrl);
				redirectToTargetActivity(CheckBeforePicActivity.this,SinglePicCheckActivity.class, bundle, SwipeDirect.DIRECT_NONE);
			    CommonUtils.fadeAlphaAnim();
			}
		}
	}
}
