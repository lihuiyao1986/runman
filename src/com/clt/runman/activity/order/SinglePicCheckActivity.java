package com.clt.runman.activity.order;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.clt.runman.R;
import com.clt.runman.core.BaseActivity;
import com.clt.runman.utils.BitmapCache;
import com.clt.runman.utils.BitmapCache.ImageCallback;
import com.clt.runman.utils.CommonUtils;
import com.clt.runman.utils.PictureUtils;
import com.clt.runman.utils.StringUtils;

/**
 * 查看单个图片的activity
 * @author yanshengli
 * @since 2015-3-23
 */
public class SinglePicCheckActivity extends BaseActivity 
{
	// 存放图片的imageview
	private ImageView imageView;
	
	// single_pic_check_ll
	private LinearLayout single_pic_check_ll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// 1.调用父类方法
		super.onCreate(savedInstanceState);
		
		// 2.初始化视图
		initUIView();
		
		// 3.初始化数据
		initData();
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() 
	{
		Bundle bundle = getIntent().getExtras();
		String picFilePath = StringUtils.trimNull(bundle.getString("picUrl"));
		BitmapCache.getInstance(this).displayBmpByFilePath(imageView, picFilePath,new ImageCallback() 
		{
			@Override
			public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) 
			{
				if(bitmap != null)
				{
					imageView.setImageBitmap(bitmap);
				}
			}
			@Override
			public Bitmap getBitMapFromFile(String sourcePath) 
			{
				return PictureUtils.getBitmapByPath(sourcePath);
			}
		});
	}

	/**
	 * 初始化视图
	 */
	@SuppressLint("ClickableViewAccessibility")
	private void initUIView() 
	{
		//1.布局文件
		setContentView(R.layout.single_pic_check_activity);
		//2.single_pic_check_ll
		single_pic_check_ll = (LinearLayout)findViewById(R.id.single_pic_check_ll);
		single_pic_check_ll.setOnTouchListener(new View.OnTouchListener() 
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				finish();
				CommonUtils.fadeAlphaAnim();
				return true;
			}
		});
		//3.图片
		imageView = (ImageView)findViewById(R.id.single_pic_check_img);
		
	}
	
}
