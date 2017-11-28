package com.rhw.learning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rhw.learning.R;
import com.rhw.learning.module.recommand.RecommandBodyValue;
import com.rhw.learning.utils.ImageLoaderManager;
import com.rhw.learning.utils.LogUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author:renhongwei
 * Date:2017/11/26 on 16:21
 */
public class HomeAdapter extends BaseAdapter {

    private static final String TAG = "HomeAdapter";
    /**
     * 定义四种类型的Item
     */
    private static final int CARD_COUNT = 3;
    private static final int VIDOE_TYPE = 0x00;
    private static final int CARD_TYPE_ONE = 0x01;
    private static final int CARD_TYPE_TWO = 0x02;

    private LayoutInflater mInflate;
    private Context mContext;
    private ArrayList<RecommandBodyValue> mData;
    private ViewHolder mViewHolder;
    private ImageLoaderManager mImagerLoader;



    public HomeAdapter (Context context, ArrayList<RecommandBodyValue> data  ){
        this.mContext = context;
        this.mData = data;
        mInflate = LayoutInflater.from(mContext);
        mImagerLoader = ImageLoaderManager.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return CARD_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        RecommandBodyValue value = (RecommandBodyValue) getItem(position);
        return value.type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        final RecommandBodyValue value = (RecommandBodyValue) getItem(position);
        //无tag时
        if (convertView == null) {
            switch (type) {
                case CARD_TYPE_ONE:
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_product_card_one_layout, parent, false);
                    mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
                    mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    mViewHolder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);
                    mViewHolder.mProductOneView = (ImageView) convertView.findViewById(R.id.product_view_one);
                    mViewHolder.mProductTwoView = (ImageView) convertView.findViewById(R.id.product_view_two);
                    mViewHolder.mProductThreeView = (ImageView) convertView.findViewById(R.id.product_view_three);
                    break;
                case CARD_TYPE_TWO:
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_product_card_two_layout, parent, false);
                    mViewHolder.mProductView = (ImageView) convertView.findViewById(R.id.product_photo_view);
                    mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
                    mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    mViewHolder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);
                    break;
                case VIDOE_TYPE:
                    //显示video卡片
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_video_layout, parent, false);
                    mViewHolder.mVieoContentLayout = (RelativeLayout)
                            convertView.findViewById(R.id.video_ad_layout);
                    mViewHolder.mShareView = (ImageView) convertView.findViewById(R.id.item_share_view);
                    break;
            }
            mViewHolder.mLogoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
            mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
            mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
            mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
            convertView.setTag(mViewHolder);
        }//有tag时
        else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        //填充item的数据
        LogUtil.i(TAG,value.logo);
        mImagerLoader.displayImage(mViewHolder.mLogoView, value.logo);
        mViewHolder.mTitleView.setText(value.title);
        mViewHolder.mInfoView.setText(value.info.concat(mContext.getString(R.string.tian_qian)));
        mViewHolder.mFooterView.setText(value.text);
        switch (type) {
            case CARD_TYPE_ONE:
                mViewHolder.mPriceView.setText(value.price);
                mViewHolder.mFromView.setText(value.from);
                mViewHolder.mZanView.setText(mContext.getString(R.string.dian_zan).concat(value.zan));
                //为类型1的三个imageview加载远程图片。
                mImagerLoader.displayImage(mViewHolder.mProductOneView, value.url.get(0));
                mImagerLoader.displayImage(mViewHolder.mProductTwoView, value.url.get(1));
                mImagerLoader.displayImage(mViewHolder.mProductThreeView, value.url.get(2));
                break;
            case CARD_TYPE_TWO:
                mViewHolder.mPriceView.setText(value.price);
                mViewHolder.mFromView.setText(value.from);
                mViewHolder.mZanView.setText(mContext.getString(R.string.dian_zan).concat(value.zan));
                //为单个ImageView加载远程图片
                mImagerLoader.displayImage(mViewHolder.mProductView, value.url.get(0));
                break;
            case VIDOE_TYPE:
                break;
        }
        return convertView;
    }
    private static class ViewHolder {
        //所有Card共有属性
        private CircleImageView mLogoView;
        private TextView mTitleView;
        private TextView mInfoView;
        private TextView mFooterView;
        //Video Card特有属性
        private RelativeLayout mVieoContentLayout;
        private ImageView mShareView;

        //Video Card外所有Card具有属性
        private TextView mPriceView;
        private TextView mFromView;
        private TextView mZanView;
        //Card One特有属性
        private ImageView mProductOneView;
        private ImageView mProductTwoView;
        private ImageView mProductThreeView;
        //Card Two特有属性
        private ImageView mProductView;
    }
}
