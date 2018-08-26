package com.daleyzou.zhbj.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不允许滑动的viewpager
 */
public class NoScollViewPager extends ViewPager {
    public NoScollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public NoScollViewPager(@NonNull Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //重写此方法，触摸时什么也不做，从而实现对滑动事件的禁用
        return true;
    }

    // 事件拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;//不拦截子控件的事件
    }
}
