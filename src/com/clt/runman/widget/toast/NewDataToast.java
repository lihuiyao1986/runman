package com.clt.runman.widget.toast;

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
 * 新数据Toast提示控件(带音乐播放)
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-8-30
 */
public class NewDataToast extends Toast{
	
	private MediaPlayer mPlayer;
	private boolean isSound;
	
	public NewDataToast(Context context) {
		this(context, false);
	}
	
	public NewDataToast(Context context, boolean isSound) {
		super(context);
		
		this.isSound = isSound;

        mPlayer = MediaPlayer.create(context, R.raw.newdatatoast);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}        	
        });

    }

	@Override
	public void show() {
		super.show();
		
		if(isSound){
			mPlayer.start();
		}
	}
	
	/**
	 * 设置是否播放声音
	 */
	public void setIsSound(boolean isSound) {
		this.isSound = isSound;
	}
	
	/**
	 * 获取控件实例
	 * @param context
	 * @param resId 文本资源oid
	 * @param isSound 是否播放声音
	 * @return
	 */
	public static NewDataToast makeText(Context context, int resId, boolean isSound) {
		return makeText(context, context.getString(resId), isSound);
	}
	
	/**
	 * 获取控件实例
	 * @param context
	 * @param text 提示消息
	 * @param isSound 是否播放声音
	 * @return
	 */
	public static NewDataToast makeText(Context context, CharSequence text, boolean isSound) {
		NewDataToast result = new NewDataToast(context, isSound);
		
        LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        
        View v = inflate.inflate(R.layout.new_data_toast, null);
        v.setMinimumWidth(dm.widthPixels);//设置控件最小宽度为手机屏幕宽度
        
        TextView tv = (TextView)v.findViewById(R.id.new_data_toast_message);
        tv.setText(text);
        
        result.setView(v);
        result.setDuration(Toast.LENGTH_SHORT);
        //显示最顶部
        result.setGravity(Gravity.TOP, 0, 0);

        return result;
    }
	
}
