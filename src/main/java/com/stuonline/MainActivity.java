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
import com.stuonline.entity.MeTab;
import com.stuonline.entity.Result;
import com.stuonline.entity.Tab;
import com.stuonline.fragments.ImageFragment;
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

import cn.jpush.android.api.JPushInterface;

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

        if (mainView == null) {
            MyApp.isMainChange = false;
            // 主布局
            mainView = getLayoutInflater().inflate(R.layout.activity_main, null);
            titleView = (TitleView) mainView.findViewById(R.id.main_title);
            //ViewPager的adapter
            adapter = new TabPageIndicatorAdapter(getSupportFragmentManager(),conversionTab(MyApp.getMeTabs()));
            ViewPager pager = (ViewPager) mainView.findViewById(R.id.vp);
            pager.setAdapter(adapter);

            //实例化TabPageIndicator然后设置ViewPager与之关联
            indicator = (TabPageIndicator) mainView.findViewById(R.id.indicator);
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
        if (MyApp.user != null) {
            titleView.setTitleRightImgSrc(XUtils.BURL + MyApp.user.getPhotoUrl());
        }else{
            titleView.setTitleRightImgSrc(R.drawable.default_photo);
        }
        indicator.notifyDataSetChanged();
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
                    startActivity(new Intent(MainActivity.this, AddTabActivity.class));
                    startIntentAnim();
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
        httpHandler = XUtils.send(XUtils.QUERYTABS, null, new MyCallBack<Result<List<Tab>>>(new TypeReference<Result<List<Tab>>>(){}) {

            @Override
            public void success(Result<List<Tab>> result) {
                if (result.state == Result.STATE_SUC) {
                    adapter.clear();
                    if (MyApp.getMeTabs() == null || MyApp.getMeTabs().size() == 0) {
                        adapter.addAll(result.data);
                    } else {
                        adapter.addAll(conversionTab(MyApp.getMeTabs()));
                    }
                    indicator.notifyDataSetChanged();
                }
            }
        },false);
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
            if (tabs.get(position).getTid() == 9){
                Fragment imgFrag=new ImageFragment();
                return imgFrag;
            }
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

        public void clear() {
            this.tabs.clear();
            notifyDataSetChanged();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 当此Activity销毁时说明已经退出程序，所有更改 MyApp.isWelcome = true;表示下次进入为第一次，显示欢迎动画
        MyApp.isWelcome = true;
    }

    public List<Tab> conversionTab(List<MeTab> meTabs) {
        List<Tab> tabs = new ArrayList<>();
        Tab tab = null;
        if (null != meTabs && meTabs.size() > 0) {
            for (MeTab meTab : meTabs) {
                tab = new Tab();
                tab.setTid(meTab.getT_id());
                tab.setTab(meTab.getT_tabname());
                tabs.add(tab);
            }
        }
        return tabs;
    }
}
