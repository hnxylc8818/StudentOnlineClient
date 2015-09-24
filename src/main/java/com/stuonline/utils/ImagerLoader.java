package com.stuonline.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

public class ImagerLoader {
	
	private static LruCache<String, Bitmap> mMemoryCache;
	
	private static ImagerLoader mImagerLoader;
	
	private ImagerLoader(){
		//　获取应用程序最大可用内存
		int maxMemory=(int) Runtime.getRuntime().maxMemory();
		int cacheSize=maxMemory/8;
		//　设置图片缓存大小为应用程序最大可用内存大小的1/8
		mMemoryCache=new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};
	}
	/**
	 * 获取ImageLoader的实例
	 * @return
	 */
	public static ImagerLoader getInstance(){
		if (mImagerLoader == null) {
			mImagerLoader=new ImagerLoader();
		}
		return mImagerLoader;
	}
	/**
	 * 将一张图片存储到LruCache中
	 * @param key
	 * @param bitmap
	 */
	public void addBitmapToMemoryCache(String key,Bitmap bitmap){
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}
	/**
	 * 从LruCache中获取一张图片
	 * @param key
	 * @return
	 */
	public Bitmap getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth){
		// 源图片的宽度
		final int width=options.outWidth;
		int inSampleSize=1;
		if (width>reqWidth) {
			// 计算出实际宽度和目标宽度的比例
			final int widthRatio=Math.round((float)width/(float)reqWidth);
			inSampleSize=widthRatio;
		}
		return inSampleSize;
	}
	
	public static Bitmap decodeSampleBitmapFormResource(String pathName,int reqWidth){
		//第一次解析将inJustDecodeBounds设置true，获取图片大小
		final BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(pathName,options);
		// 调用上面自定义的方法计算inSampleSize值
		options.inSampleSize=calculateInSampleSize(options, reqWidth);
		options.inJustDecodeBounds=false;
		Bitmap bitmap=BitmapFactory.decodeFile(pathName, options);
		return bitmap;
	}
	
	
}
