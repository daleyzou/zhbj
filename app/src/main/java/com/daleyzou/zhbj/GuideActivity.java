package com.daleyzou.zhbj;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daleyzou.zhbj.utils.PrefUtils;

import java.util.ArrayList;

/**
 * 新手引导界面
 */
public class GuideActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    // 引导页图片id数组
    private int[] mImageIds = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};

    private ArrayList<ImageView> mImageViewList;// imageView集合
    private LinearLayout llContainer;
    private ImageView ivRedPoint;
    private  int mPointDis;
    private Button btnStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();// 去掉标题栏
        setContentView(R.layout.activity_guide);

        mViewPager = findViewById(R.id.vp_guide);
        llContainer = (LinearLayout)findViewById(R.id.ll_content);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
        btnStart = (Button) findViewById(R.id.btn_start);


        initeData();
        mViewPager.setAdapter(new GuideAdapter());

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当页面滑动过程中回调
                //更新小红点距离
                int leftMargin = (int)(mPointDis * positionOffset + position * mPointDis);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                layoutParams.leftMargin = leftMargin;//修改左边距

                ivRedPoint.setLayoutParams(layoutParams);//重新设置布局参数
            }

            @Override
            public void onPageSelected(int position) {
                //某个页面被选中
                if (position == mImageViewList.size() - 1){//最后一个页面显示开始体验的按钮
                    btnStart.setVisibility(View.VISIBLE);
                }else {
                    btnStart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 页面状态发生变化的回调
            }
        });
        //计算两个圆点的距离
        // 移动距离=第二个圆点left值 - 第一个圆点left值

        //监听layout方法结束的事件，位置确定好之后再获取圆点间距
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 移除监听，避免重复调用
                ivRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //layout方法执行结束的回调
                mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
                System.out.println("圆点之间的距离："+mPointDis);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新sp
                PrefUtils.setBoolen(getApplication(),"is_first_enter",false);
                //跳转到主页面
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }

    /**
     * 初始化数据
     */
   private void initeData(){
       mImageViewList = new ArrayList<ImageView>();
       for (int i = 0; i < mImageIds.length; i++){
           ImageView  view = new ImageView(this);
           view.setBackgroundResource(mImageIds[i]);// 通过设置背景，可以让宽高填充布局
           mImageViewList.add(view);
           // 初始化布局参数，宽高包裹内容，父控件是谁，就是谁声明的布局参数
           LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                   LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
           if(i > 0){
               // 设置左边距
               params.leftMargin = 10;
           }

           //初始化小圆点
           ImageView point = new ImageView(this);
           point.setImageResource(R.drawable.shape_point_gray);// 设置形状
           point.setLayoutParams(params);
           llContainer.addView(point);
       }
   }

    class GuideAdapter extends PagerAdapter{

        //item的个数
        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        //初始化item布局
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        // 销毁item
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
