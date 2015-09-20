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

import com.stuonline.fragments.ItemFragment;
import com.stuonline.https.XUtils;
import com.stuonline.views.SplashView;
import com.stuonline.views.TitleView;
import com.stuonline.vpi.TabPageIndicator;

public class MainActivity extends BaseActivity {

    private FrameLayout frameLayout;
    private SplashView splashView;
    private TitleView titleView;
    private View mainView;
    private ImageView addTab;

    /**
     * Tab标题
     */
    private static final String[] TITLE = new String[]{"头条", "房产", "另一面", "女人",
            "财经", "数码", "情感", "科技",
            "财经", "数码", "情感", "科技"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {

        // 帧布局
        frameLayout = new FrameLayout(this);
        if (mainView == null || MyApp.isMainChange) {
            MyApp.isMainChange=false;
            // 主布局
            mainView = getLayoutInflater().inflate(R.layout.activity_main, null);
            titleView = (TitleView) mainView.findViewById(R.id.main_title);
            //ViewPager的adapter
            FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
            ViewPager pager = (ViewPager) mainView.findViewById(R.id.vp);
            pager.setAdapter(adapter);

            //实例化TabPageIndicator然后设置ViewPager与之关联
            TabPageIndicator indicator = (TabPageIndicator) mainView.findViewById(R.id.indicator);
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
        addTab= (ImageView) mainView.findViewById(R.id.add_tab);
        addTab.setOnClickListener(l);

        ViewGroup parent = (ViewGroup) mainView.getParent();
        if (null != parent) {
            parent.removeView(mainView);
        }
        // 先添加主布局
        frameLayout.addView(mainView);
        // 如果是首次进入App界面
        if (MyApp.isWelcome) {
            // 更改属性值为false，表示下次进入不是第一次，不显示欢迎动画
            MyApp.isWelcome = false;
            // 再添加SplashView
            splashView = new SplashView(this);
            frameLayout.addView(splashView);
            setContentView(frameLayout);
            // 开启Splash动画 --- 模拟后台加载数据
            startLoad();
        } else {
            setContentView(frameLayout);
        }
    }

    private View.OnClickListener l =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
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

    /**
     * ViewPager适配器
     *
     * @author len
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递参数
            Fragment fragment = new ItemFragment();
            Bundle args = new Bundle();
            args.putString("arg", TITLE[position]);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position % TITLE.length];
        }

        @Override
        public int getCount() {
            return TITLE.length;
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
