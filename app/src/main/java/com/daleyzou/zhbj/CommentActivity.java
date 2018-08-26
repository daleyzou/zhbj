package com.daleyzou.zhbj;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.daleyzou.zhbj.base.impl.menu.TabDetailPager;
import com.daleyzou.zhbj.domain.NewsTabBean;
import com.daleyzou.zhbj.fragment.ContentFragment;
import com.daleyzou.zhbj.utils.PrefUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 主页面
 */
public class CommentActivity extends AppCompatActivity {

    @ViewInject(R.id.am_lv_comments)
    ListView mAmLvComments;
    @ViewInject(R.id.ll_control)
    private LinearLayout llControl;
    @ViewInject(R.id.btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.btn_textsize)
    private ImageButton btnTextSize;
    @ViewInject(R.id.btn_share)
    private ImageButton btnShare;
    @ViewInject(R.id.btn_menu)
    private ImageButton btnMenu;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    List<String> coments = new ArrayList<String>();
    String[] name = {"颖雅","梦丽","林惠","琛柔","锦格","雪克","鸿璇","初诗","静玉","娅格","彩菡","春涵","洁岚","彩彦","蓓锦","洲彬","彦帆","芝馨","楠雪","馨月"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comment);
        ViewUtils.inject(this);

        tvTitle.setText("查看评论");
        llControl.setVisibility(View.GONE);
        btnBack.setVisibility(View.VISIBLE);
        btnTextSize.setVisibility(View.GONE);
        btnMenu.setVisibility(View.GONE);

        Bundle b=this.getIntent().getExtras();
        coments = b.getStringArrayList("coments");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAmLvComments.setAdapter(new CommentsAdapter());

    }

    class CommentsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return coments.size();
        }

        @Override
        public Object getItem(int position) {
            return coments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                convertView = View.inflate(getApplicationContext(),R.layout.item_comment,null);
                holder = new ViewHolder();
                holder.tvName = (TextView) convertView.findViewById(R.id.iam_tv_replyed);
                holder.tvContent = (TextView)convertView.findViewById(R.id.iam_tv_content);
                holder.tvDate = (TextView)convertView.findViewById(R.id.iam_tv_time);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            String  comment = (String) getItem(position);
            holder.tvContent.setText(comment);
            Random random = new Random();
            holder.tvName.setText(name[random.nextInt(name.length)]);
            holder.tvDate.setText(new Date().toString());

            holder.tvContent.setTextColor(Color.BLACK);

            return convertView;
        }
    }
    static class ViewHolder{
        public TextView tvName;
        public TextView tvContent;
        public TextView tvDate;
    }
}
