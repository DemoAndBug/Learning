package com.rhw.learning.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.rhw.learning.R;

/**
 * Author:renhongwei
 * Date:2017/11/25 on 16:23
 * Function:初始化univerImageloader，并用来加载图片
 */
public class ImageLoaderManager {

    //配置默认参数值
    public  static final int  THREAD_COUNT = 4;
    public  static final int  PRIORITY  = 2;
    public  static final int  CONNECTION_TIME_OUT = 5*1000;
    public  static final int  READ_TIME_OUT  = 30*1000;
    public  static final int  DISK_CACHE_SIZE  = 50*1024;

    private  static ImageLoader mImageloader = null;
    private  static ImageLoaderManager mInstance = null;



    public static  ImageLoaderManager   getInstance(Context context){
        if(mInstance == null){
            synchronized (ImageLoaderManager.class){
                if(mInstance == null){
                     mInstance = new ImageLoaderManager(context);
                }
            }
        }
        return  mInstance;
    }

    //单利模式私有构造方法
    private  ImageLoaderManager (Context context){
        ImageLoaderConfiguration imageLoaderConfiguration = new  ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(THREAD_COUNT)//配置图片下载最大线程数量
                .threadPriority(PRIORITY)//配置下载线程的优先级
                .denyCacheImageMultipleSizesInMemory()//防止缓存多套尺寸图片到我们的内存中
                .memoryCache(new WeakMemoryCache())//使用弱引用内存缓存
                .diskCacheSize(DISK_CACHE_SIZE)//分配硬盘缓存大小
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//使用MD5命名文件
                .tasksProcessingOrder(QueueProcessingType.FIFO)//图片下载顺序设置
                .defaultDisplayImageOptions(getDefaultOptions())//默认图片加载options
                .imageDownloader(new BaseImageDownloader(context, CONNECTION_TIME_OUT, READ_TIME_OUT))//设置图片下载器
                .writeDebugLogs()//是否要写日志，debug环境模式下输出log
                .build();
        ImageLoader.getInstance().init(imageLoaderConfiguration);
        mImageloader =ImageLoader.getInstance();
    }

    /**
     *  默认的图片显示Options,可设置图片的缓存策略，编解码方式等，非常重要
     * @return
     */
    private DisplayImageOptions getDefaultOptions() {

        DisplayImageOptions options = new
                DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.default_user_avatar)
                .showImageOnFail(R.mipmap.default_user_avatar)
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中, 重要，否则图片不会缓存到内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中, 重要，否则图片不会缓存到硬盘中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .decodingOptions(new BitmapFactory.Options())//设置图片的解码配置
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .build();
        return options;
    }

    public void displayImage(ImageView imageView, String path,DisplayImageOptions options, ImageLoadingListener listener) {
        if (mImageloader != null) {
            mImageloader.displayImage(path, imageView,options, listener);
        }
    }

    public void displayImage(ImageView imageView, String path,ImageLoadingListener listener) {
        displayImage(imageView, path, null,listener);
    }

    public void displayImage(ImageView imageView, String path) {
        displayImage(imageView, path, null);
    }

}



