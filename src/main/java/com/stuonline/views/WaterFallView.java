package com.stuonline.views;

import java.io.File;

import com.stuonline.AddTabActivity;
import com.stuonline.MainActivity;
import com.stuonline.MyApp;
import com.stuonline.PersonalCenterActivity;
import com.stuonline.R;
import com.stuonline.utils.HttpHelper;
import com.stuonline.utils.ImagerLoader;
import com.stuonline.utils.Images;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class WaterFallView extends ScrollView {

    // 定义每页加载多少图片
    public static final int PAGE_COUNT = 10;
    // 记录当前已经加载到第几页了
    private int page;
    // 当前第一列的高度
    private int firstColumnHeight;
    // 当前第二列的高度
    private int secondColumnHeight;
    // 当前第三列的高度
    private int thirdColumnHeight;
    // 每一列的宽度
    private int columnWidth;
    // 记录是否第一次加载
    private boolean isOnceLoaded;
    // 第一列的布局
    private LinearLayout firstColumn;
    // 第二列的布局
    private LinearLayout secondColumn;
    // 第三列布局
    private LinearLayout thirdColumn;
    // MyScrollView的子布局 -- 总线性布局
    private static View scrollLayout;
    // MyScrollView的高度
    private static int scrollViewHeight;
    // 记录当前上滑出去的距离
    private static int lastScrollY = -1;
    private WaterFallView myScrollView;
    private ImagerLoader imagerLoader;

    private GestureDetector detector;

    public WaterFallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        myScrollView = this;
        if (imagerLoader == null) {
            imagerLoader = ImagerLoader.getInstance();
        }
        detector = new GestureDetector(context, cestureLis);
    }

    // 分发触摸事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private GestureDetector.OnGestureListener cestureLis = new GestureDetector.OnGestureListener() {

        // 按下时
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        // 短按时
        @Override
        public void onShowPress(MotionEvent e) {
        }

        // 抬起时
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        //滑动中持续调用 参数1 一次滑动前事件，2 一次滑动后事件，3 x滑动距离，4 y滑动距离
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        //长按时
        @Override
        public void onLongPress(MotionEvent e) {
        }

        //滑动结束后 参数1一次滑动前事件，2一次滑动后事件，3x滑动速度，4y滑动速度
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float y1 = e1.getY();
            float y2 = e2.getY();
            float yDriver = y1 - y2;
            if (yDriver > 300) {
                page++;
                loadMoreImage();
            }
            return false;
        }
    };

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // 初始化
        if (changed && !isOnceLoaded) {
            scrollViewHeight = getHeight();
            scrollLayout = getChildAt(0);
            firstColumn = (LinearLayout) scrollLayout
                    .findViewById(R.id.first_colum);
            secondColumn = (LinearLayout) scrollLayout
                    .findViewById(R.id.second_colum);
            thirdColumn = (LinearLayout) scrollLayout
                    .findViewById(R.id.third_colum);
            isOnceLoaded = true;
            columnWidth = firstColumn.getWidth();

            // 加载第一页的图片
            loadMoreImage();
        }
    }

    private void loadMoreImage() {
        // 加载图片
        // 分页加载，从第几个图片开始请求
        int startIndex = page * PAGE_COUNT;
        int endIndex = (page + 1) * PAGE_COUNT;
        if (startIndex < Images.imageUrls.length) {
            if (endIndex > Images.imageUrls.length) {
                endIndex = Images.imageUrls.length;
            }
            for (int i = startIndex; i < endIndex; i++) {
                // 循环加载图片
                String url = Images.imageUrls[i];
                // 加载
                ImagerLoadTask task = new ImagerLoadTask();
                task.execute(url);
            }
        }
    }

    class ImagerLoadTask extends AsyncTask<String, Void, Bitmap> {

        private String mImageUrl;

        @Override
        protected Bitmap doInBackground(String... params) {
            // 子线程中加载图片
            mImageUrl = params[0];
            return loadImage(mImageUrl);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            // 显示图片

            if (null != bitmap) {
                // 得到图片经过压缩后的图片宽高
                double ratio = bitmap.getWidth() / (columnWidth * 1.0); // 压缩比例
                int scaledHeight = (int) (bitmap.getHeight() / ratio);
                addImage(bitmap, columnWidth, scaledHeight);
            }
        }

    }

    private Bitmap loadImage(String imageUrl) {
        // 1.内存有没有图片
        Bitmap bitmap = imagerLoader.getBitmapFromMemoryCache(imageUrl);
        if (bitmap != null) {
            return bitmap;
        }
        // 2.SD卡里面有没有图片
        File imageFile = new File(HttpHelper.getImagePath(imageUrl));
        // 3.如果SD卡都没有图片，去网络加载
        if (!imageFile.exists()) {
            HttpHelper.downloadImage(imageUrl, columnWidth);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 去缓存里面获取图片
        if (null != imageUrl) {

            bitmap = ImagerLoader.decodeSampleBitmapFormResource(
                    imageFile.getPath(), columnWidth);
            if (null != bitmap) {
                return bitmap;
            } else {
                bitmap = ImagerLoader.decodeSampleBitmapFormResource(
                        imageFile.getPath(), columnWidth);
                return bitmap;
            }

        }
        return null;
    }

    public void addImage(Bitmap bitmap, int width, int scaledHeight) {
        // 添加图片 -- 显示
        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(bitmap);
        iv.setScaleType(ScaleType.FIT_XY); // 填充整个ImageView
        iv.setPadding(5, 5, 5, 5);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                scaledHeight);
        iv.setLayoutParams(params);
        // 1.找到某个线性布局 -- 高度最小的
        LinearLayout ll = findMinHeightLayout(scaledHeight);
        // 2.将图片添加进去 addView(iv)
        ll.addView(iv);
    }

    /**
     * 找到高度最小的布局
     *
     * @param
     * @param scaledHeight
     * @return
     */
    private LinearLayout findMinHeightLayout(int scaledHeight) {
        // 求三个数当中的最小的数
        // Math.min(Math.min(a, b), c);
        if (firstColumnHeight <= secondColumnHeight) {
            if (firstColumnHeight <= thirdColumnHeight) {
                // first最小
                firstColumnHeight += scaledHeight;
                return firstColumn;
            } else {
                // third最小
                thirdColumnHeight += scaledHeight;
                return thirdColumn;
            }
        } else {
            if (secondColumnHeight <= thirdColumnHeight) {
                // second最小
                secondColumnHeight += scaledHeight;
                return secondColumn;
            } else {
                // third最小
                thirdColumnHeight += scaledHeight;
                return thirdColumn;
            }
        }
    }
}
