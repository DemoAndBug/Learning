package com.rhw.learning.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rhw.learning.R;

/**
 * Date:2017/11/23 on 20:37
 * @author Simon
 */
public class MessageFragment extends BaseFragment {

    /**
     * UI
     */
    private View mContentView;
    private TextView mMessageView;
    private TextView mZanView;

    public MessageFragment (){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_message_layout,null,false);
        initView();
        return mContentView;
    }

    private void initView() {
        mMessageView = (TextView) mContentView.findViewById(R.id.tip_message_view);
        mZanView = (TextView) mContentView.findViewById(R.id.zan_message_info_view);
    }
}
