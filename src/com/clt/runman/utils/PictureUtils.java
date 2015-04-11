package com.clt.runman.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * 图片工具类
 * @author yanshengli
 * @since 2015-3-26
 */
public class PictureUtils {

    /**
     * getBitmapFromFile method
     * @param dst
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapFromFile(File dst,int width,int height){
        if (null != dst && dst.exists ()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options ();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile (dst.getPath (), opts);
                // 计算图片缩放比例
                final int minSideLength = Math.max (width, height);
                opts.inSampleSize = computeSampleSize (opts, minSideLength, width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile (dst.getPath (), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace ();
            }
        }
        return null;
    }

    /**
     * getBitmapFromFile重载
     * 
     * @param filepath
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapFromFile(String filepath,int width,int height){
        return getBitmapFromFile (new File (filepath), width, height);
    }

    /**
     * computeSampleSize method
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options,int minSideLength,int maxNumOfPixels){
        int initialSize = computeInitialSampleSize (options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    /**
     * computeInitialSampleSize method
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength,int maxNumOfPixels){
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil (Math.sqrt (w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min (Math.floor (w / minSideLength), Math.floor (h / minSideLength));
        if (upperBound < lowerBound) { return lowerBound; }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 将bitmap保存到文件中
     * @param filepath
     * @param bitmap
     */
    public static void saveBitmapToFile(String filepath,Bitmap bitmap){
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream (filepath);
            bitmap.compress (Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush ();
        } catch (Exception e) {
            e.printStackTrace ();
        } finally {
            if (fOut != null) {
                try {
                    fOut.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }

    }

    /**
     * 将洗车拍照后的照片重新保存一下
     * @param filepath
     * @param context
     */
    public static void convertWashcarTakePicture(String filepath,Context context){
        int height = GestureUtils.getScreenPix (context).heightPixels;
        int width = GestureUtils.getScreenPix (context).widthPixels;
        saveBitmapToFile (filepath, getBitmapFromFile (filepath, width, height));
    }

    /**
     * 生成缩图
     * @param largeImagePath
     * @param thumbnailPath
     * @param width
     * @param height
     */
    public static void createImageThumbnail(String largeImagePath,String thumbnailPath,int width,int height){
        Bitmap bitmap = getBitmapByPath (largeImagePath, null);
        if (bitmap != null) {
            bitmap = zoomBitmap (bitmap, width, height);
            saveBitmapToFile (thumbnailPath, bitmap);
        }
    }

    /**
     * 放大缩小图片
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bgimage,double newWidth,double newHeight){
        // 获取这个图片的宽和高
        float width = bgimage.getWidth ();
        float height = bgimage.getHeight ();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix ();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale (scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap (bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 等比例缩放
     * @param bitmap
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmap,int scale){
        // 获取这个图片的宽和高
        int width = bitmap.getWidth ();
        int height = bitmap.getHeight ();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix ();
        // 缩放图片动作
        matrix.postScale (scale, scale);
        // 创建新的图片
        return Bitmap.createBitmap (bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 计算缩放图片的宽高
     *
     * @param img_size
     * @param square_size
     * @return
     */
    public static int[] scaleImageSize(int[] img_size,int square_size){
        if (img_size[0] <= square_size && img_size[1] <= square_size) { return img_size; }
        double ratio = square_size / (double) Math.max (img_size[0], img_size[1]);
        return new int[] { (int) (img_size[0] * ratio), (int) (img_size[1] * ratio) };
    }

    /**
     * 根据路径获取图片的bitmap
     * @param filePath
     * @param opts
     * @return
     */
    public static Bitmap getBitmapByPath(String filePath,BitmapFactory.Options opts){
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            File file = new File (filePath);
            fis = new FileInputStream (file);
            bitmap = BitmapFactory.decodeStream (fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        } catch (OutOfMemoryError e) {
            e.printStackTrace ();
        } finally {
            try {
                fis.close ();
            } catch (Exception e) {}
        }
        return bitmap;
    }

    /**
     * 获取bitmap
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapByPath(String filePath){
        return getBitmapByPath (filePath, null);
    }
}
