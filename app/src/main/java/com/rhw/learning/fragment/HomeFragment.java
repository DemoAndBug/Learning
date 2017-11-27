package com.rhw.learning.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rhw.learning.R;
import com.rhw.learning.adapter.HomeAdapter;
import com.rhw.learning.module.recommand.BaseRecommandModel;
import com.rhw.learning.okhttp.listener.DisposeDataListener;
import com.rhw.learning.utils.RequestCenter;


/**
 * Author:renhongwei
 * Date:2017/11/23 on 20:37
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    private static final int REQUEST_QRCODE = 0x01;
    /**
     * UI
     */
    private View mContentView;
    private ListView mListView;
    private TextView mQRCodeView;
    private TextView mCategoryView;
    private TextView mSearchView;
    private ImageView mLoadingView;

    /**
     * data
     */
    private HomeAdapter mAdapter;
    private BaseRecommandModel mRecommandData;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"into create");
        requestRecommandData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_home_layout, container, false);
        initView();
        return mContentView;
    }

    public void  initView(){
        mQRCodeView = (TextView) mContentView.findViewById(R.id.qrcode_view);
        mQRCodeView.setOnClickListener(this);
        mCategoryView = (TextView) mContentView.findViewById(R.id.category_view);
        mCategoryView.setOnClickListener(this);
        mSearchView = (TextView) mContentView.findViewById(R.id.search_view);
        mSearchView.setOnClickListener(this);
        mListView = (ListView) mContentView.findViewById(R.id.list_view);
        mLoadingView = (ImageView) mContentView.findViewById(R.id.loading_view);
        AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
        anim.start();
    }

    private void requestRecommandData() {
        Log.i(TAG,"into requestRecommandData");
        RequestCenter.requestRecommandData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.i(TAG,"==" +responseObj.toString());
                //mRecommandData = (BaseRecommandModel) responseObj;
                //更新UI
                //showSuccessView();
            }

            @Override
            public void onFailure(Object reasonObj) {
                    Log.i(TAG,"onFailure" + reasonObj.toString());
                //显示请求失败View
                showErrorView();
            }
        });
    }

    private void showSuccessView() {
        if (mRecommandData.data.list != null && mRecommandData.data.list.size() > 0) {
            mLoadingView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mAdapter = new HomeAdapter(mContext, mRecommandData.data.list);
            mListView.setAdapter(mAdapter);
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        } else {
            showErrorView();
        }
    }

    private void showErrorView(){

    }

    @Override
    public void onClick(View v) {

    }
}
