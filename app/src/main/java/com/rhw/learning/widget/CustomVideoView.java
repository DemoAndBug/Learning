package com.rhw.learning.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rhw.learning.R;
import com.rhw.learning.constant.Constant;
import com.rhw.learning.utils.LogUtil;
import com.rhw.learning.utils.Utils;

/**
 * Author:renhongwei
 * Date:2017/11/28 on 21:54
 * funtion：视频播放view 控制视频的播放 暂停 事件触发
 */
public class CustomVideoView extends RelativeLayout implements View.OnClickListener,MediaPlayer.OnPreparedListener,
        MediaPlayer.OnInfoListener,MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener ,MediaPlayer.OnBufferingUpdateListener ,TextureView.SurfaceTextureListener{

    /**
     * Constant
     */
    private static final String TAG = "CustomVideoView";
    private static final int TIME_MSG = 0x01;
    private static final int TIME_INVAL = 1000;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSING = 2;
    /**
     *加载失败情况下会尝试加载三次
     */
    private static final int LOAD_TOTAL_COUNT = 3;

    /**
     * UI
     */
    private ViewGroup mParentContainer;
    private RelativeLayout mPlayerView;
    private TextureView mVideoView;
    private Button mMiniPlayBtn;
    private ImageView mFullBtn;
    private ImageView mLoadingBar;
    private ImageView mFrameView;

    /**
     * 音量控制
     */
    private AudioManager audioManager;
    /**
     *
     */
    private Surface videoSurface;

    /**
     * Data
     */
    private String mUrl;
    private String mFrameURI;
    private boolean isMute;
    private int mScreenWidth, mDestationHeight;

    /**
     * Status状态保护
     */
    private boolean canPlay = true;
    private boolean mIsRealPause;
    private boolean mIsComplete;
    private int mCurrentCount;
    private int playerState = STATE_IDLE;//默认处于空闲状态


    /**
     * 监听屏幕是否锁屏
     */
    private ScreenEventReceiver mScreenReceiver;

    private MediaPlayer mMediaPlayer;
    /**
     * 事件监听回调，为外界提供接口
     */
    private VideoPlayerListener mListener;

    private FrameImageLoadListener mFrameLoadListener;

    public void setListener(VideoPlayerListener listener) {
        this.mListener = listener;
    }

    public void setFrameLoadListener(FrameImageLoadListener frameLoadListener) {
        this.mFrameLoadListener = frameLoadListener;
    }

    /**
     * 供调用实现具体点击逻辑,
     * 如果对UI的点击没有具体监测的话可以不回调
     */
    public interface VideoPlayerListener {
        /**
         * 视频播放进度
         * @param time
         */
        public void onBufferUpdate(int time);

        /**
         * 跳转到全频的事件监听
         */
        public void onClickFullScreenBtn();


        public void onClickVideo();

        public void onClickBackBtn();

        public void onClickPlay();

        public void onVideoLoadSuccess();

        public void onVideoLoadFailed();

        public void onVideoLoadComplete();
    }

    /**
     * 加载定帧图监听器
     */
    public interface FrameImageLoadListener {

        void onStartFrameLoad(String url, ImageLoaderListener listener);
    }

    public interface ImageLoaderListener {
        /**
         * 如果图片下载不成功，传null
         * @param loadedImage
         */
        void onLoadingComplete(Bitmap loadedImage);
    }
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MSG:
                    if (isPlaying()) {
                        //可以在这里更新progressbar
                        LogUtil.i(TAG, "TIME_MSG");
                        //mListener.onBufferUpdate(getCurrentPosition());
                        sendEmptyMessageDelayed(TIME_MSG, TIME_INVAL);
                    }
                    break;
            }
        }
    };

    public CustomVideoView(Context context, ViewGroup parentContainer) {
        super(context);
        mParentContainer = parentContainer;
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        initData();
        initView();
        registerBroadcastReceiver();
    }

    private void initData() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mDestationHeight = (int) (mScreenWidth * Constant.VIDEO_HEIGHT_PERCENT);
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        mPlayerView = (RelativeLayout) inflater.inflate(R.layout.video_player_view, this);
        mVideoView = (TextureView) mPlayerView.findViewById(R.id.xadsdk_player_video_textureView);
        mVideoView.setOnClickListener(this);
        mVideoView.setKeepScreenOn(true);
        mVideoView.setSurfaceTextureListener(this);
        initSmallLayoutMode(); //init the small mode
    }
    // 小模式状态
    private void initSmallLayoutMode() {
        LayoutParams params = new LayoutParams(mScreenWidth, mDestationHeight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mPlayerView.setLayoutParams(params);

        mMiniPlayBtn = (Button) mPlayerView.findViewById(R.id.xadsdk_small_play_btn);
        mFullBtn = (ImageView) mPlayerView.findViewById(R.id.xadsdk_to_full_view);
        mLoadingBar = (ImageView) mPlayerView.findViewById(R.id.loading_bar);
        mFrameView = (ImageView) mPlayerView.findViewById(R.id.framing_view);
        mMiniPlayBtn.setOnClickListener(this);
        mFullBtn.setOnClickListener(this);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE && playerState == STATE_PAUSING) {
            if (isPauseBtnClicked() || isComplete()) {
                pause();
            } else {
                decideCanPlay();
            }
        } else {
            pause();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
    @Override
    public void onPrepared(MediaPlayer mp) {

        showPlayView();
        mMediaPlayer = mp;
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mCurrentCount = 0;
            if (mListener != null) {
                mListener.onVideoLoadSuccess();
            }
            //满足自动播放条件，则直接播放
            setCurrentPlayState(STATE_PAUSING);
            resume();
        }

    }

    /**
     * 流媒体 播放完毕后回调的方法
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mListener != null) {
            mListener.onVideoLoadComplete();
        }
        playBack();
        setIsComplete(true);
        setIsRealPause(true);
    }

    /**
     * 异步操作中出现错误时回调该方法
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        LogUtil.e(TAG, "do error:" + what);
        this.playerState = STATE_ERROR;
        mMediaPlayer = mp;
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
        if (mCurrentCount >= LOAD_TOTAL_COUNT) {
            showPauseView(false);
            if (this.mListener != null) {
                mListener.onVideoLoadFailed();
            }
        }
        this.stop();//去重新load
        return true;
    }

    /**
     * 媒体播放时出现信息或者警告时回调该方法
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return true;
    }

    /**
     * 流媒体缓冲状态发生改变的时候, 标明该状态
     * @param mp
     * @param percent
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }



    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        LogUtil.i(TAG, "onSurfaceTextureAvailable");
        videoSurface = new Surface(surface);
        checkMediaPlayer();
        mMediaPlayer.setSurface(videoSurface);
        load();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        LogUtil.i(TAG, "onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    /**
     * 点击事件由自己处理
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
    @Override
    public void onClick(View v) {
        if (v == this.mMiniPlayBtn) {

        } else if (v == this.mFullBtn) {
            this.mListener.onClickFullScreenBtn();
        } else if (v == mVideoView) {
            this.mListener.onClickVideo();
        }

    }
    /**
     * 监听锁屏事件的广播接收器
     */
    private class ScreenEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //主动锁屏时 pause, 主动解锁屏幕时，resume
            switch (intent.getAction()) {
                case Intent.ACTION_USER_PRESENT:

                    break;
                case Intent.ACTION_SCREEN_OFF:

                    break;
            }
        }
    }

    /**
     * 注册亮灭屏广播监听
     */
    private void registerBroadcastReceiver() {
        if (mScreenReceiver == null) {
            mScreenReceiver = new ScreenEventReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            getContext().registerReceiver(mScreenReceiver, filter);
        }
    }

    /**
     * 取消亮灭屏广播监听
     */
    private void unRegisterBroadcastReceiver() {
        if (mScreenReceiver != null) {
            getContext().unregisterReceiver(mScreenReceiver);
        }
    }

    public void destroy() {
        LogUtil.d(TAG, " do destroy");
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setOnSeekCompleteListener(null);
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        setCurrentPlayState(STATE_IDLE);
        mCurrentCount = 0;
        setIsComplete(false);
        setIsRealPause(false);
        unRegisterBroadcastReceiver();
        mHandler.removeCallbacksAndMessages(null); //release all message and runnable
        showPauseView(false); //除了播放和loading外其余任何状态都显示pause
    }


    private MediaPlayer createMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.reset();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (videoSurface != null && videoSurface.isValid()) {
            mMediaPlayer.setSurface(videoSurface);
        } else {
            stop();
        }
        return mMediaPlayer;
    }

    public void load() {
        if (this.playerState != STATE_IDLE) {
            return;
        }
        LogUtil.d(TAG, "do play url = " + this.mUrl);
        showLoadingView();
        try {
            setCurrentPlayState(STATE_IDLE);
            checkMediaPlayer();
            mute(true);
            mMediaPlayer.setDataSource(this.mUrl);
            mMediaPlayer.prepareAsync(); //开始异步加载
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            stop(); //error以后重新调用stop加载
        }
    }

    public void stop() {
        LogUtil.d(TAG, " do stop");
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.setOnSeekCompleteListener(null);
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        mHandler.removeCallbacksAndMessages(null);
        setCurrentPlayState(STATE_IDLE);
        if (mCurrentCount < LOAD_TOTAL_COUNT) { //满足重新加载的条件
            mCurrentCount += 1;
            load();
        } else {
            showPauseView(false); //显示暂停状态
        }
    }

    /**
     * 暂停视频播放
     */
    public void pause() {
        if (this.playerState != STATE_PLAYING) {
            return;
        }
        LogUtil.d(TAG, "do pause");
        setCurrentPlayState(STATE_PAUSING);
        if (isPlaying()) {
            mMediaPlayer.pause();
            if (!this.canPlay) {
                this.mMediaPlayer.seekTo(0);
            }
        }
        this.showPauseView(false);
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 恢复视频播放
     */
    public void resume() {
        if (this.playerState != STATE_PAUSING) {
            return;
        }
        LogUtil.d(TAG, "do resume");
        if (!isPlaying()) {
            entryResumeState();
            mMediaPlayer.setOnSeekCompleteListener(null);
            mMediaPlayer.start();
            mHandler.sendEmptyMessage(TIME_MSG);
            showPauseView(true);
        } else {
            showPauseView(false);
        }
    }
    private void decideCanPlay() {
        if (Utils.getVisiblePercent(mParentContainer) > Constant.VIDEO_SCREEN_PERCENT)
            //来回切换页面时，只有 >50,且满足自动播放条件才自动播放
            resume();
        else
            pause();
    }

    /**
     *播放完后重置播放状态到初始值，并保存为Pause状态 避免重复加载
     */
    public void playBack() {
        LogUtil.d(TAG, " do playBack");
        setCurrentPlayState(STATE_PAUSING);
        mHandler.removeCallbacksAndMessages(null);
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnSeekCompleteListener(null);
            mMediaPlayer.seekTo(0);
            //设置为Pause状态 避免用户再次播放重复加载
            mMediaPlayer.pause();
        }
        this.showPauseView(false);
    }


    //跳到指定点播放视频
    public void seekAndResume(int position) {
        if (mMediaPlayer != null) {
            showPauseView(true);
            entryResumeState();
            mMediaPlayer.seekTo(position);
            mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    LogUtil.d(TAG, "do seek and resume");
                    mMediaPlayer.start();
                    mHandler.sendEmptyMessage(TIME_MSG);
                }
            });
        }
    }
    //跳到指定点暂停视频
    public void seekAndPause(int position) {
        if (this.playerState != STATE_PLAYING) {
            return;
        }
        showPauseView(false);
        setCurrentPlayState(STATE_PAUSING);
        if (isPlaying()) {
            mMediaPlayer.seekTo(position);
            mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    LogUtil.d(TAG, "do seek and pause");
                    mMediaPlayer.pause();
                    mHandler.removeCallbacksAndMessages(null);
                }
            });
        }
    }

    /**
     * 进入播放状态时的状态更新
     */
    private void entryResumeState() {
        canPlay = true;
        setCurrentPlayState(STATE_PLAYING);
        setIsRealPause(false);
        setIsComplete(false);
    }

    public boolean isRealPause() {
        return mIsRealPause;
    }

    /**
     * 检测mMediaPlayer 是否为空
     */
    private synchronized void checkMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = createMediaPlayer();
        }
    }

    /**
     * 设置当前所处状态
     * @param state
     */
    private void setCurrentPlayState(int state) {
        playerState = state;
    }

    public boolean isPauseBtnClicked() {
        return mIsRealPause;
    }

    public boolean isComplete() {
        return mIsComplete;
    }
    /**
     * 设置是否静音
     * @param mute
     */
    public void mute(boolean mute) {
        LogUtil.d(TAG, "mute");
        isMute = mute;
        if (mMediaPlayer != null && this.audioManager != null) {
            float volume = isMute ? 0.0f : 1.0f;
            mMediaPlayer.setVolume(volume, volume);
        }
    }


    public boolean isPlaying() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    public void setIsRealPause(boolean isRealPause) {
        this.mIsRealPause = isRealPause;
    }

    public boolean isFrameHidden() {
        return mFrameView.getVisibility() != View.VISIBLE;
    }

    public void
    setIsComplete(boolean isComplete) {
        mIsComplete = isComplete;
    }

    private void showPauseView(boolean show) {
        mFullBtn.setVisibility(show ? View.VISIBLE : View.GONE);
        mMiniPlayBtn.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoadingBar.clearAnimation();
        mLoadingBar.setVisibility(View.GONE);
        if (!show) {
            mFrameView.setVisibility(View.VISIBLE);
            loadFrameImage();
        } else {
            mFrameView.setVisibility(View.GONE);
        }
    }

    private void showLoadingView() {
        mFullBtn.setVisibility(View.GONE);
        mLoadingBar.setVisibility(View.VISIBLE);
        AnimationDrawable anim = (AnimationDrawable) mLoadingBar.getBackground();
        anim.start();
        mMiniPlayBtn.setVisibility(View.GONE);
        mFrameView.setVisibility(View.GONE);
        loadFrameImage();
    }

    private void showPlayView() {
        mLoadingBar.clearAnimation();
        mLoadingBar.setVisibility(View.GONE);
        mMiniPlayBtn.setVisibility(View.GONE);
        mFrameView.setVisibility(View.GONE);
    }

    /**
     * 获取当前所处的位置
     * @return
     */
    public int getCurrentPosition() {
        if (this.mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * 异步加载定帧图
     */
    private void loadFrameImage() {
        if (mFrameLoadListener != null) {
            mFrameLoadListener.onStartFrameLoad(mFrameURI, new ImageLoaderListener() {
                @Override
                public void onLoadingComplete(Bitmap loadedImage) {
                    if (loadedImage != null) {
                        mFrameView.setScaleType(ImageView.ScaleType.FIT_XY);
                        mFrameView.setImageBitmap(loadedImage);
                    } else {
                        mFrameView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        mFrameView.setImageResource(R.mipmap.xadsdk_img_error);
                    }
                }
            });
        }
    }

    private void setVideoBuffer() {
        if (Build.VERSION.SDK_INT >= 15) {
            SurfaceTexture texture = mVideoView.getSurfaceTexture();
            if (texture != null) {
                texture.setDefaultBufferSize(mMediaPlayer.getVideoWidth(),
                        mMediaPlayer.getVideoHeight());
            }
        }
    }

    public void setDataSource(String url) {
        this.mUrl = url;
    }

    public void setFrameURI(String url) {
        mFrameURI = url;
    }

    /**
     * 全屏播放模式下暂停播放
     */
    public void pauseForFullScreen() {
        if (playerState != STATE_PLAYING) {
            return;
        }
        LogUtil.d(TAG, "do full pause");
        setCurrentPlayState(STATE_PAUSING);
        if (isPlaying()) {
            mMediaPlayer.pause();
            if (!this.canPlay) {
                mMediaPlayer.seekTo(0);
            }
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    public int getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }
    public void isShowFullBtn(boolean isShow) {
        mFullBtn.setImageResource(isShow ? R.mipmap.xadsdk_ad_mini : R.mipmap.xadsdk_ad_mini_null);
        mFullBtn.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
