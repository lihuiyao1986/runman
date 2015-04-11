package com.clt.runman.widget.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.clt.runman.R;

/**
 * 自定义toast对话框
 * @author yanshengli
 * @since 2015-2-9
 */
public class CustomToastDialog extends Toast 
{
	/** 播放器 **/
	private MediaPlayer mPlayer;
	
	/** 是否播放 **/
	private boolean isSound;
	
	/**
	 * 构造函数－1
	 * @param context
	 */
	public CustomToastDialog(Context context) {
		this(context, false);
	}
	
	/**
	 * 构造函数-2
	 * @param context
	 * @param isSound
	 */
	public CustomToastDialog(Context context, boolean isSound) 
	{
		super(context);
		this.isSound = isSound;
        mPlayer = MediaPlayer.create(context, R.raw.newdatatoast);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}        	
        });
    }
	
	@Override
	public void show() 
	{
		super.show();
		if(isSound)
		{
			mPlayer.start();
		}
	}
	
	/**
	 * 设置是否播放声音
	 */
	public void setIsSound(boolean isSound) 
	{
		this.isSound = isSound;
	}
	
	/**
	 * 获取控件实例
	 * @param context
	 * @param text 提示消息
	 * @param isSound 是否播放声音
	 * @return
	 */
	@SuppressLint("InflateParams")
	public static CustomToastDialog makeText(Context context,CharSequence title,CharSequence message,int duration ,boolean isSound) 
	{
		CustomToastDialog result = new CustomToastDialog(context, isSound);
        LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        View view = inflate.inflate(R.layout.custom_toast_dialog, null);
        view.setMinimumWidth(dm.widthPixels);//设置控件最小宽度为手机屏幕宽度
       
        //1.标题
        TextView titleTV = (TextView)view.findViewById(R.id.toast_view_title_tv);
        titleTV.setText(title);
        
        //2.消息
        TextView messageTV = (TextView)view.findViewById(R.id.toast_view_message_tv);
        messageTV.setText(message);
        
        //3.设置视图
        result.setView(view);
        
        //4.设置显示的时常
        result.setDuration(duration);
        
        //5.显示最顶部
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        
        //6.返回结果
        return result;
    }

}
