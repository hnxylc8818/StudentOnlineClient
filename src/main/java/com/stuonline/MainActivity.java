package com.stuonline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.http.ResponseInfo;
import com.stuonline.entity.Result;
import com.stuonline.entity.Tab;
import com.stuonline.fragments.ItemFragment;
import com.stuonline.https.MyCallBack;
import com.stuonline.https.XUtils;
import com.stuonline.utils.DialogUtil;
import com.stuonline.utils.JsonUtil;
import com.stuonline.views.SplashView;
import com.stuonline.views.TitleView;
import com.stuonline.vpi.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private FrameLayout frameLayout;
    private SplashView splashView;
    private TitleView titleView;
    private View mainView;
    private ImageView addTab;
    private TabPageIndicatorAdapter adapter;
    private TabPageIndicator indicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        init();
    }

    private void init() {

        // 帧布局
        frameLayout = new FrameLayout(this);
        if (mainView == null || MyApp.isMainChange) {
            MyApp.isMainChange = false;
            // 主布局
            mainView = getLayoutInflater().inflate(R.layout.activity_main, null);
            titleView = (TitleView) mainView.findViewById(R.id.main_title);
            //ViewPager的adapter
            adapter = new TabPageIndicatorAdapter(getSupportFragmentManager(), null);
            ViewPager pager = (ViewPager) mainView.findViewById(R.id.vp);
            pager.setAdapter(adapter);
            pager.setCurrentItem(1);

            //实例化TabPageIndicator然后设置ViewPager与之关联
            indicator= (TabPageIndicator) mainView.findViewById(R.id.indicator);
            indicator.setViewPager(pager);

            //如果我们要对ViewPager设置监听，用indicator设置就行了
            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int arg0) {

                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {

                }
            });
            if (MyApp.user != null) {
                titleView.setTitleRightImgSrc(XUtils.BURL + MyApp.user.getPhotoUrl());
            }

            titleView.setOnRightImgclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyApp.user == null) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        startIntentAnim();
                    } else {
                        Intent intent = new Intent(MainActivity.this, PersonalCenterActivity.class);
                        startActivity(intent);
                        startIntentAnim();
                    }
                }
            });
        }
        addTab = (ImageView) mainView.findViewById(R.id.add_tab);
        addTab.setOnClickListener(l);

        ViewGroup parent = (ViewGroup) mainView.getParent();
        if (null != parent) {
            parent.removeView(mainView);
        }
        // 先添加主布局
        frameLayout.addView(mainView);
        loadTab();
        // 如果是首次进入App界面
        if (MyApp.isWelcome) {
            // 再添加SplashView
            splashView = new SplashView(this);
            frameLayout.addView(splashView);
            setContentView(frameLayout);
            // 开启Splash动画 --- 模拟后台加载数据
            startLoad();
            // 更改属性值为false，表示下次进入不是第一次，不显示欢迎动画
            MyApp.isWelcome = false;
        } else {
            setContentView(frameLayout);
        }
    }

    private View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_tab:
                    XUtils.showToast("添加标签");
                    break;
            }
        }
    };

    private Handler handler = new Handler();

    private void startLoad() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 数据加载完毕执行后面的动画 -- 让ContentView显示
                splashView.splashAndDisapper();
            }
        }, 3000);
    }

    private void loadTab() {
        httpHandler = XUtils.send(XUtils.QUERYTABS, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (null != responseInfo) {
                    JsonUtil<Result<List<Tab>>> jsonUtil = new JsonUtil<Result<List<Tab>>>(new TypeReference<Result<List<Tab>>>() {
                    });
                    Result<List<Tab>> result = jsonUtil.parse(responseInfo.result);
                    if (result.state == Result.STATE_SUC) {
                        adapter.clear();
                        adapter.addAll(result.data);
                        indicator.notifyDataSetChanged();
                    }
                } else {
                    XUtils.showToast("发生错误");
                }
            }
        });
    }

    /**
     * ViewPager适配器
     *
     * @author len
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {

        private List<Tab> tabs;

        public TabPageIndicatorAdapter(FragmentManager fm, List<Tab> tabs) {
            super(fm);
            if (tabs == null) {
                this.tabs = new ArrayList<>();
            } else {
                this.tabs = tabs;
            }
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递参数
            Fragment fragment = new ItemFragment();
            Bundle args = new Bundle();
            args.putString("tid", String.valueOf(tabs.get(position).getTid()));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            return TITLE[position % TITLE.length];
            return tabs.get(position % tabs.size()).getTab();
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        public void addAll(List<Tab> tabs) {
            if (null != tabs) {
                this.tabs.addAll(tabs);
                notifyDataSetChanged();
            }
        }
        public void clear(){
            this.tabs.clear();
            notifyDataSetChanged();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 当此Activity销毁时说明已经退出程序，所有更改 MyApp.isWelcome = true;表示下次进入为第一次，显示欢迎动画
        MyApp.isWelcome = true;
    }
}
