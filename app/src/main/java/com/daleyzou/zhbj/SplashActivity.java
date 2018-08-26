package com.daleyzou.zhbj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.daleyzou.zhbj.utils.PrefUtils;

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout rlRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        rlRoot = (RelativeLayout)findViewById(R.id.rl_root);
        // 旋转动画
        RotateAnimation animRotate = new RotateAnimation(0,360,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animRotate.setDuration(1000);//旋转时间
        animRotate.setFillAfter(true);// 保持动画结束状态

        //缩放动画
        ScaleAnimation animScale = new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animScale.setDuration(1000);

        // 渐变动画
        AlphaAnimation animAlpha = new AlphaAnimation(0,1);
        animAlpha.setDuration(2000);
        animAlpha.setFillAfter(true);

        // 动画集合
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animRotate);
        set.addAnimation(animScale);
        set.addAnimation(animAlpha);

        // 启动动画
        rlRoot.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 动画结束，跳转页面
                // 如果是第一次进入，跳新手引导
                // 否则跳主页面

                Intent intent;
                boolean isFirstEnter = PrefUtils.getBoolean(SplashActivity.this,"is_first_enter",true);
                if (isFirstEnter){
                    //新手引导
                    intent = new Intent(getApplicationContext(), GuideActivity.class);
                }else {
                    // 主页面
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }
                startActivity(intent);
                finish();//结束当前页面
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
