package com.rhw.learning.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rhw.learning.R;
import com.rhw.learning.utils.LogUtil;

/**
 * Author:renhongwei
 * Date:2017/11/29 on 0:46
 * function: 全屏显示视频界面
 */
public class VideoFullDialog extends Dialog  implements CustomVideoView.VideoPlayerListener {

    private static final String TAG = VideoFullDialog.class.getSimpleName();
    private CustomVideoView mVideoView;

    private Context mContext;
    private RelativeLayout mRootView;
    private ViewGroup mParentView;
    private ImageView mBackButton;


    private int mPosition;

    private boolean isFirst = true;

    private FullToSmallListener mListener;
    public interface FullToSmallListener {
        void getCurrentPlayPosition(int position);
        void playComplete();//全屏播放结束时回调
    }
    public void setListener(FullToSmallListener listener) {
        this.mListener = listener;
    }
    public VideoFullDialog(@NonNull Context context,CustomVideoView mraidView,  int position) {
        super(context, R.style.dialog_full_screen);
        this.mContext = context;
        this.mPosition = position;
        this.mVideoView = mraidView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xadsdk_dialog_video_layout);
        initVideoView();
    }

    private void initVideoView() {
        mParentView = (RelativeLayout) findViewById(R.id.content_layout);
        mBackButton = (ImageView) findViewById(R.id.xadsdk_player_close_btn);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBackBtn();
            }
        });
        mRootView = (RelativeLayout) findViewById(R.id.root_view);
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickVideo();
            }
        });
        mVideoView.setListener(this);
        mVideoView.mute(false);
        mParentView.addView(mVideoView);
    }

    @Override
    public void onBufferUpdate(int time) {

    }

    @Override
    public void onClickFullScreenBtn() {

    }

    @Override
    public void onClickVideo() {

    }

    @Override
    public void onClickBackBtn() {
        if (mListener != null) {
            mListener.getCurrentPlayPosition(this.mVideoView.getCurrentPosition());
        }
    }

    @Override
    public void onClickPlay() {

    }

    @Override
    public void onVideoLoadSuccess() {
        if (mVideoView != null) {
            mVideoView.resume();
        }
    }

    @Override
    public void onVideoLoadFailed() {

    }

    @Override
    public void onVideoLoadComplete() {
        try {
            //int position = mVideoView.getDuration() / Constant.MILLION_UNIT;

        } catch (Exception e) {
            e.printStackTrace();
        }

        dismiss();
        if (mListener != null) {
            mListener.playComplete();
        }

    }

    @Override
    public void dismiss() {
        LogUtil.e(TAG, "dismiss");
        this.mParentView.removeView(mVideoView);
        super.dismiss();
    }

    @Override
    public void onBackPressed() {
        onClickBackBtn();
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        LogUtil.i(TAG, "onWindowFocusChanged");
        mVideoView.isShowFullBtn(false); //防止第一次，有些手机仍显示全屏按钮
        if (!hasFocus) {
            mPosition = mVideoView.getCurrentPosition();
            mVideoView.pauseForFullScreen();
        } else {
            if (isFirst) { //为了适配某些手机不执行seekandresume中的播放方法
                mVideoView.seekAndResume(mPosition);
            } else {
                mVideoView.resume();
            }
        }
        isFirst = false;
    }

}
