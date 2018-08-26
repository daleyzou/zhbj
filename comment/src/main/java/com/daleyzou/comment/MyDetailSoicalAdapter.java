package com.daleyzou.comment;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wgyscsf on 2016/5/26.
 * 邮箱：wgyscsf@163.com
 * 博客：http://blog.csdn.net/wgyscsf
 */
public class MyDetailSoicalAdapter extends BaseAdapter {

    ViewHolder viewHolder = null;
    private LayoutInflater mLayoutInflater;
    private List<Comment> mCommentList;
    private Context mContext;

    public MyDetailSoicalAdapter(Context context, List<Comment> mCommentList) {
        this.mContext = context;
        this.mCommentList = mCommentList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mCommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_activity_main, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //开始设置参数
        Comment comment = mCommentList.get(position);
        viewHolder.mIamTvReply.setText(comment.getNickName());
        viewHolder.mIamTvLocal.setText(comment.getLocal());
        viewHolder.mIamTvContent.setText(comment.getContent());
        viewHolder.mIamTvTime.setText(comment.getTime());

        //设置男女
        if (comment.getSex() == 0) {
            viewHolder.mIamIvSex.setImageResource(R.drawable.icon_point_explain);
        } else if (comment.getSex() == 1) {
            viewHolder.mIamIvSex.setImageResource(R.drawable.icon_sender_man);

        } else if (comment.getSex() == 2) {
            viewHolder.mIamIvSex.setImageResource(R.drawable.icon_sender_women);

        }
        //是否显示回复俩字以及回复人
        if (comment.getReplyed() != null) {
            viewHolder.iam_tv_huifu.setText("回复");
            viewHolder.mIamTvReplyed.setText(comment.getReplyed());
        }
        viewHolder.mIamSdvImg.setImageURI(Uri.parse(comment.getImgUrl()));
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iam_sdv_img)
        SimpleDraweeView mIamSdvImg;
        @Bind(R.id.iam_tv_reply)
        TextView mIamTvReply;
        @Bind(R.id.iam_tv_huifu)
        TextView iam_tv_huifu;
        @Bind(R.id.iam_tv_replyed)
        TextView mIamTvReplyed;
        @Bind(R.id.iam_iv_sex)
        ImageView mIamIvSex;
        @Bind(R.id.iam_tv_local)
        TextView mIamTvLocal;
        @Bind(R.id.iam_tv_content)
        TextView mIamTvContent;
        @Bind(R.id.iam_tv_time)
        TextView mIamTvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
