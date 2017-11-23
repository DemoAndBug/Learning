package com.rhw.learning.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rhw.learning.R;

public class MainActivity extends BaseActivity {


    private Button button;
    private RelativeLayout mHomeActivity;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiView();
    }

    private void intiView() {
    }


}
