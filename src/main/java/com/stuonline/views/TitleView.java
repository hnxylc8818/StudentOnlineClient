package com.stuonline.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stuonline.R;

/**
 * 孙卫星
 * 自定义头部控件
 * Created by Administrator on 2015/9/14.
 */
public class TitleView extends RelativeLayout {


    private  ImageView mLeftimg;
    private TextView mCententtext;
    private TextView mRighttext;
    private  ImageView mRightimg;


    public TitleView(Context context) {
        super(context);
        init(null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    public void init(AttributeSet attrs){
        LayoutInflater.from(getContext()).inflate(R.layout.layout_title, this);
        mLeftimg = (ImageView) findViewById(R.id.title_left);
        mCententtext = (TextView) findViewById(R.id.title_center);
        mRighttext= (TextView) findViewById(R.id.title_right);
        mRightimg= (ImageView) findViewById(R.id.title_right_src);
        if (attrs == null) {
            return;
        }
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int index = array.getIndex(i);
            switch (index){
                case R.styleable.TitleView_left_src:
                    Drawable drawable = array.getDrawable(index);
                    mLeftimg.setImageDrawable(drawable);
                    break;
                case R.styleable.TitleView_centent_text:
                    String str = array.getString(index);
                    mCententtext.setText(str);
                    break;
                case R.styleable.TitleView_centent_text_Color:
                    int color = array.getColor(index, Color.RED);
                    mCententtext.setTextColor(color);
                    break;
                case R.styleable.TitleView_centent_text_Size:
                    int size = array.getDimensionPixelSize(index, 20);
                    mCententtext.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
                    break;
                case R.styleable.TitleView_right_text:
                    String rightstr = array.getString(index);
                    mRighttext.setText(rightstr);
                    break;
                case R.styleable.TitleView_title_right_src:
                    Drawable rightdrawable = array.getDrawable(index);
                    mRightimg.setImageDrawable(rightdrawable);
                    break;
                case R.styleable.TitleView_right_text_Color:
                    int rightcolor = array.getColor(index, Color.RED);
                    mRighttext.setTextColor(rightcolor);
                    break;
                case R.styleable.TitleView_right_text_Size:
                    int rightsize = array.getDimensionPixelSize(index, 20);
                    mRighttext.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightsize);
                    break;
                case R.styleable.TitleView_title_right_src_visibility:
                    int vis = array.getInt(index, 0);
                    if (vis == 1) {
                        mRightimg.setVisibility(View.VISIBLE);
                    } else if (vis == 0) {
                        mRightimg.setVisibility(View.GONE);
                    }
                    break;
                case R.styleable.TitleView_title_right_text_visibility:
                    int rightvis = array.getInt(index, 0);
                    if (rightvis == 1) {
                        mRighttext.setVisibility(View.VISIBLE);
                    } else if (rightvis == 0) {
                        mRighttext.setVisibility(View.GONE);
                    }
                    break;
                case R.styleable.TitleView_left_visibility:
                    int leftvis = array.getInt(index, 0);
                    if (leftvis == 1) {
                        mLeftimg.setVisibility(View.VISIBLE);
                    } else if (leftvis == 0) {
                        mLeftimg.setVisibility(View.GONE);
                    }
                    break;

            }
        }
    }


}
