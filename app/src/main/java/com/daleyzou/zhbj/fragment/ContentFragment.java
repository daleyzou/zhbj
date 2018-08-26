package com.daleyzou.zhbj.fragment;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.daleyzou.zhbj.MainActivity;
import com.daleyzou.zhbj.R;
import com.daleyzou.zhbj.base.BasePager;
import com.daleyzou.zhbj.base.impl.GovAffairsPager;
import com.daleyzou.zhbj.base.impl.HomePager;
import com.daleyzou.zhbj.base.impl.NewsCenterPager;
import com.daleyzou.zhbj.base.impl.SettingPager;
import com.daleyzou.zhbj.base.impl.SmartServicePager;
import com.daleyzou.zhbj.view.NoScollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * 主页面Fragment
 */
public class ContentFragment extends BaseFragment {
    private NoScollViewPager mViewPager;
    private ArrayList<BasePager> mPagers; // 五个标签页的集合
    private RadioGroup rgGroup;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mViewPager = (NoScollViewPager)view.findViewById(R.id.vp_content);
        rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<BasePager>();

        //添加五个标签页
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));


        mViewPager.setAdapter(new ContentAdaper());
        //底栏标签切换监听
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home:
                        mViewPager.setCurrentItem(0);//参2：是否平滑滑动
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.rb_gov:
                        mViewPager.setCurrentItem(3);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4);
                        break;
                    default:
                        break;
                }
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager pager = mPagers.get(position);
                pager.initData();

                if (position == 0 || position == mPagers.size() - 1){
                    //首页和设置禁用侧边栏
                    setSlidingMenuEnable(false);
                }else {
                    //其他页面开启侧边栏
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPagers.get(0).initData();//手动加载第一页的数据
        setSlidingMenuEnable(false);//首页禁用侧边栏
    }

    /**
     * 开启或禁用侧边栏
     * @param enable
     */
    private void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }


    class ContentAdaper extends PagerAdapter{

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            BasePager pager = mPagers.get(position);
            View view = pager.mRootView;//获取当前页面对象的布局
            //pager.initData();//初始化数据，viewpager会默认加载下一个页面
            container.addView(view);


            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 获取新闻中心页面
     */
    public NewsCenterPager getNewsCenterPager(){
        NewsCenterPager pager = (NewsCenterPager) mPagers.get(1);
        return pager;
    }
}
