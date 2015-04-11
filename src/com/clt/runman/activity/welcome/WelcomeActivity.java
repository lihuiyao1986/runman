package com.clt.runman.activity.welcome;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.clt.runman.R;
import com.clt.runman.activity.login.LoginActivity;
import com.clt.runman.adapter.BasePagerAdapter;
import com.clt.runman.core.BaseActivity;
import com.clt.runman.utils.AppUtils;

/**
 *@Description:引导页
 *@Author:张聪
 *@Since:2015年1月20日下午10:12:59
 */
public class WelcomeActivity extends BaseActivity implements OnPageChangeListener, OnClickListener {

    private ViewPager       viewPager;
    private PagerAdapter    pagerAdapter;
    private Button          startButton;
    private LinearLayout    indicatorLayout;
    private ArrayList<View> views;
    private ImageView[]     indicators = null;
    private int[]           images;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        setContentView (R.layout.guide_activity);
        // 仅需在这设置图片 指示器和page自动添加
        images = new int[] { R.drawable.guide_01, R.drawable.guide_02, R.drawable.guide_03 };
        initView ();
    }

    // 初始化视图
    private void initView(){
        // 实例化视图控件
        viewPager = (ViewPager) findViewById (R.id.viewpage);
        startButton = (Button) findViewById (R.id.start_button);
        startButton.setOnClickListener (this);
        indicatorLayout = (LinearLayout) findViewById (R.id.indicator);
        views = new ArrayList<View> ();
        indicators = new ImageView[images.length]; // 定义指示器数组大小
        for ( int i = 0 ; i < images.length ; i++ ) {
            // 循环加入图片
            ImageView imageView = new ImageView (AppUtils.getActivity ());
            imageView.setBackgroundResource (images[i]);
            views.add (imageView);
            // 循环加入指示器
            indicators[i] = new ImageView (AppUtils.getActivity ());
            indicators[i].setBackgroundResource (R.drawable.indicators_default);
            if (i == 0) {
                indicators[i].setBackgroundResource (R.drawable.indicators_now);
            }
            indicatorLayout.addView (indicators[i]);
        }
        pagerAdapter = new BasePagerAdapter (views);
        viewPager.setAdapter (pagerAdapter); // 设置适配器
        viewPager.setOnPageChangeListener (this);
    }

    // 按钮的点击事件
    @Override
    public void onClick(View v){
        if (v.getId () == R.id.start_button) {
            Intent intent = new Intent (AppUtils.getActivity (),LoginActivity.class);
            startActivity (intent);
            overridePendingTransition (R.anim.in_from_right, R.anim.out_to_left);
            this.finish ();
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0){
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageScrolled(int arg0,float arg1,int arg2){
        // TODO Auto-generated method stub
    }

    // 监听viewpage
    @Override
    public void onPageSelected(int arg0){
        // 显示最后一个图片时显示按钮
        if (arg0 == indicators.length - 1) {
            startButton.setVisibility (View.VISIBLE);
        } else {
            startButton.setVisibility (View.INVISIBLE);
        }
        // 更改指示器图片
        for ( int i = 0 ; i < indicators.length ; i++ ) {
            indicators[arg0].setBackgroundResource (R.drawable.indicators_now);
            if (arg0 != i) {
                indicators[i].setBackgroundResource (R.drawable.indicators_default);
            }
        }
    }
}
