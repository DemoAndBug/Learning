package com.rhw.learning.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rhw.learning.R;
import com.rhw.learning.constant.Constant;
import com.rhw.learning.manager.SharedPreferenceManager;
import com.rhw.learning.video.VideoSettings;

/**
 * Author:renhongwei
 * Date:2017/12/4 on 13:45
 * Function:视屏自动播放设置
 */
public class SettingActivity extends BaseActivity  implements  View.OnClickListener{

    /**
     * UI
     */
    private RelativeLayout mWifiLayout;
    private RelativeLayout mAlwayLayout;
    private RelativeLayout mNeverLayout;
    private CheckBox mWifiBox, mAlwayBox, mNeverBox;
    private ImageView mBackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);

        initView();
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.back_view);
        mWifiLayout = (RelativeLayout) findViewById(R.id.wifi_layout);
        mWifiBox = (CheckBox) findViewById(R.id.wifi_check_box);
        mAlwayLayout = (RelativeLayout) findViewById(R.id.alway_layout);
        mAlwayBox = (CheckBox) findViewById(R.id.alway_check_box);
        mNeverLayout = (RelativeLayout) findViewById(R.id.close_layout);
        mNeverBox = (CheckBox) findViewById(R.id.close_check_box);

        mBackView.setOnClickListener(this);
        mWifiLayout.setOnClickListener(this);
        mAlwayLayout.setOnClickListener(this);
        mNeverLayout.setOnClickListener(this);

        int currentSetting = SharedPreferenceManager.getInstance().getInt(SharedPreferenceManager.VIDEO_PLAY_SETTING, 1);
        switch (currentSetting) {
            case 0:
                mAlwayBox.setBackgroundResource(R.mipmap.setting_selected);
                mWifiBox.setBackgroundResource(0);
                mNeverBox.setBackgroundResource(0);
                break;
            case 1:
                mAlwayBox.setBackgroundResource(0);
                mWifiBox.setBackgroundResource(R.mipmap.setting_selected);
                mNeverBox.setBackgroundResource(0);
                break;
            case 2:
                mAlwayBox.setBackgroundResource(0);
                mWifiBox.setBackgroundResource(0);
                mNeverBox.setBackgroundResource(R.mipmap.setting_selected);
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.alway_layout:
                SharedPreferenceManager.getInstance().putInt(SharedPreferenceManager.VIDEO_PLAY_SETTING, 0);
                VideoSettings.setCurrentSetting(Constant.AutoPlaySetting.AUTO_PLAY_3G_4G_WIFI);
                mAlwayBox.setBackgroundResource(R.mipmap.setting_selected);
                mWifiBox.setBackgroundResource(0);
                mNeverBox.setBackgroundResource(0);
                break;
            case R.id.close_layout:
                SharedPreferenceManager.getInstance().putInt(SharedPreferenceManager.VIDEO_PLAY_SETTING, 2);
                VideoSettings.setCurrentSetting(Constant.AutoPlaySetting.AUTO_PLAY_NEVER);
                mAlwayBox.setBackgroundResource(0);
                mWifiBox.setBackgroundResource(0);
                mNeverBox.setBackgroundResource(R.mipmap.setting_selected);
                break;
            case R.id.wifi_layout:
                SharedPreferenceManager.getInstance().putInt(SharedPreferenceManager.VIDEO_PLAY_SETTING, 1);
                VideoSettings.setCurrentSetting(Constant.AutoPlaySetting.AUTO_PLAY_ONLY_WIFI);
                mAlwayBox.setBackgroundResource(0);
                mWifiBox.setBackgroundResource(R.mipmap.setting_selected);
                mNeverBox.setBackgroundResource(0);
                break;
            case R.id.back_view:
                finish();
                break;

        }
    }
}
