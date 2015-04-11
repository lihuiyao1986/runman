package com.clt.runman.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;

/**
 * 图片缓存实体类
 * @author yanshengli
 * @since 2015-2-4
 */
public class BitmapCache {

    /**实体对象**/
    private static BitmapCache                     instance;
    /** handler **/
    public Handler                                 handler    = new Handler ();
    /** tag **/
    public final String                            TAG        = getClass ().getSimpleName ();
    /** 应用上下文 **/
    @SuppressWarnings("unused")
    private Context                                context;
    /** 缓存容器对象 **/
    private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>> ();

    private BitmapCache(Context context) {
        this.context = context;
    }

    /**
     * 获取对象
     * @param context
     * @return
     */
    public static BitmapCache getInstance(Context context){
        if (instance == null) {
            instance = new BitmapCache (context);
        }
        return instance;
    }

    /**
     * 添加图片
     * @param path
     * @param bmp
     */
    public void put(String key,Bitmap bmp){
        if (!TextUtils.isEmpty (key) && bmp != null) {
            imageCache.put (key, new SoftReference<Bitmap> (bmp));
        }
    }

    /**
     * 从缓存中获取图片
     * @param key
     */
    public Bitmap get(String key){
        SoftReference<Bitmap> btpSoftRe = imageCache.get (key);
        return btpSoftRe.get ();
    }

    /**
     * 展示图片--1
     * @param iv
     * @param sourcePath
     * @param callback
     */
    public void displayBmpByFilePath(final ImageView iv,final String sourcePath,final ImageCallback callback){
        displayBmpByFilePath (iv, sourcePath, callback, true);
    }

    /**
     * 展示图片--2
     *@Description: TODO(这里用一句话描述这个方法的作用) 
     *@Author: 李焱生
     *@Since: 2015年4月2日下午9:17:31
     *@param iv
     *@param sourcePath
     *@param callback
     *@param cache
     */
    public void displayBmpByFilePath(final ImageView iv,final String sourcePath,final ImageCallback callback,boolean cache){
        if (TextUtils.isEmpty (sourcePath)) { return; }
        if (cache && imageCache.containsKey (sourcePath)) {
            SoftReference<Bitmap> reference = imageCache.get (sourcePath);
            Bitmap bmp = reference.get ();
            if (callback != null && bmp != null) {
                callback.imageLoad (iv, bmp, sourcePath);
            }
            return;
        }
        new Thread () {
            public void run(){
                if (callback != null) {
                    final Bitmap tempBitmap = callback.getBitMapFromFile (sourcePath);
                    if (tempBitmap != null) {
                        put (sourcePath, tempBitmap);
                        handler.post (new Runnable () {

                            @Override
                            public void run(){
                                callback.imageLoad (iv, tempBitmap, sourcePath);
                            }
                        });
                    }

                }
            }
        }.start ();
    }

    /**
     * 回调接口
     * @author yanshengli
     * @since 2015-2-4
     */
    public interface ImageCallback {

        public void imageLoad(ImageView imageView,Bitmap bitmap,Object... params);

        public Bitmap getBitMapFromFile(String sourcePath);
    }
}
