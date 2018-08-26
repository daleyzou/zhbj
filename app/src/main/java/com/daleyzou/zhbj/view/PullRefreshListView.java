package com.daleyzou.zhbj.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daleyzou.zhbj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PullRefreshListView extends ListView implements AbsListView.OnScrollListener{
    private static final int STATE_PULL_TO_REFRESH = 1;
    private static final int STATE_RELEASE_TO_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;

    private int mCurrentState = STATE_PULL_TO_REFRESH;
    private  View mHeaderView;
    private int mHeaderViewHeight;
    private int startY = -1;
    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private ProgressBar pbProgress;
    private View mFooterView;
    private int mFooterViewHeight;

    public PullRefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public PullRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public PullRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PullRefreshListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView(){
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh_header,null);
        this.addHeaderView(mHeaderView);

        tvTitle = (TextView)mHeaderView.findViewById(R.id.tv_refresh_title);
        tvTime = (TextView)mHeaderView.findViewById(R.id.tv_refresh_time);
        ivArrow = (ImageView)mHeaderView.findViewById(R.id.iv_arrow);
        pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);

        // 隐藏头布局
        mHeaderView.measure(0,0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);

        initAnimation();
        setCurrentTime();
        this.setOnScrollListener(this); // 滑动监听
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1){ // 当用户按住头条新闻的viewpager进行下拉时，ACTION_DOWN会被viewpager消费掉
                    startY = (int) ev.getY(); // 需要重新赋值
                }

                if (mCurrentState == STATE_REFRESHING){
                    // 如果是正在刷新，就跳出循环
                    break;
                }
                int endY = (int) ev.getY();
                int dy = endY - startY;
                int firstVisiblePosition = getFirstVisiblePosition();// 当前显示的第一个item的位置
                if (dy > 0 && firstVisiblePosition == 0){// 必须下拉，并且当前显示的是第一个item
                    int padding = dy - mHeaderViewHeight;// 计算当前下拉控件的padding的值
                    mHeaderView.setPadding(0,padding,0,0);

                    if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH){
                        // 改为松开刷新
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    }else if(padding < 0 && mCurrentState != STATE_PULL_TO_REFRESH){
                        // 改为下拉刷新
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }

                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrentState == STATE_RELEASE_TO_REFRESH){
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
                    // 完整展示头布局
                    mHeaderView.setPadding(0,0,0,0);
                    // 4.进行回调
                    if (mListener != null){
                        mListener.onRefersh();
                    }
                }else if (mCurrentState == STATE_PULL_TO_REFRESH){
                    mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
                    // 隐藏头布局

                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 初始化箭头动画
     */
    private void initAnimation(){
        animUp = new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        animDown = new RotateAnimation(-180,0, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);
    }

    /**
     * 根据当前状态刷新界面
     */
    private void refreshState() {
        switch (mCurrentState){
            case STATE_PULL_TO_REFRESH:
                tvTitle.setText("下拉刷新");
                pbProgress.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                ivArrow.startAnimation(animDown);
                break;
            case STATE_RELEASE_TO_REFRESH:
                tvTitle.setText("松开刷新");
                pbProgress.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                ivArrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新...");
                ivArrow.clearAnimation();// 清除箭头动画，否则无法隐藏
                pbProgress.setVisibility(View.VISIBLE);
                ivArrow.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    // 3.定义成员变量，接收监听对象
    private OnRefreshListener mListener;

    /**
     * 2.暴露接口，设置监听
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener){
        mListener = listener;
    }

    private boolean isLoadMore; // 标记是否正在加载更多
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 滑动状态发生变化
        if (scrollState == SCROLL_STATE_IDLE){// 空闲状态
            int lastVisiblePostition = getLastVisiblePosition();
            if (lastVisiblePostition == getCount() - 1 && !isLoadMore){
                System.out.println("到底了");
                isLoadMore = true;
                mFooterView.setPadding(0,0,0,0); // 显示加载更多的布局
                setSelection(getCount() - 1);// 将listview显示在最后一个item上，从而加载更多会直接显示出来，无需手动滑动

                // 通知主界面加载下一页
                if (mListener != null){
                    mListener.onLoadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 滑动过程回调
    }

    /**
     * 1.下拉刷新的回调接口
     */
    public interface OnRefreshListener{
        public void onRefersh();
        // 下拉加载更多
        public void onLoadMore();
    }

    /**
     * 刷新结束，收起控件
     */
    public void onRefreshComplete(boolean success){
        if (!isLoadMore){
            if (success){ // 只有刷新成功才更新时间
                setCurrentTime();
            }
            mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
            mCurrentState = STATE_PULL_TO_REFRESH;
            tvTitle.setText("下拉刷新");
            pbProgress.setVisibility(View.INVISIBLE);
            ivArrow.setVisibility(View.VISIBLE);
        }else {
            mFooterView.setPadding(0,-mFooterViewHeight,0,0);
            isLoadMore = false;
        }
    }

    /**
     * 设置刷新时间
     */
    private void setCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        tvTime.setText(time);
    }

    /**
     * 初始化脚布局
     */
    private void initFooterView(){
        mFooterView = View.inflate(getContext(), R.layout.pull_to_refresh_footer,null);
        this.addFooterView(mFooterView);
        mFooterView.measure(0,0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();

        mFooterView.setPadding(0,-mFooterViewHeight,0,0);
    }
}
