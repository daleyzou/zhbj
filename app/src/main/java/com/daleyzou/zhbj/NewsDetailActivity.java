package com.daleyzou.zhbj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daleyzou.zhbj.domain.Comment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mob.MobSDK;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * 新闻详情页面
 */
public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener {

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
    @ViewInject(R.id.wv_news_detail)
    private WebView mWebView;
    @ViewInject(R.id.pb_detail_loading)
    private ProgressBar pbLoading;
    private String mUrl;
    private String mNewsId;

    @ViewInject(R.id.am_tv_zan)
    TextView mAmTvZan;
    @ViewInject(R.id.am_tv_comment)
    TextView mAmTvComment;
    //添加listview头
    View mView;
    //列表数据
    List<Comment> mCommentList = new ArrayList<Comment>();
    @ViewInject(R.id.am_et_msg)
    EditText mAmEtMsg;
    @ViewInject(R.id.am_b_save)
    Button mAmBSave;
    @ViewInject(R.id.am_ll_liuyan)
    LinearLayout mAmLlLiuyan;
    @ViewInject(R.id.am_ll_info)
    LinearLayout mAmLlInfo;


    //回复的内容
    String info = "";
    //标记位，是评论还是回复。默true认评论
    boolean isComment=true;

    List<String> coments = new ArrayList<String>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_news_detail);
        ViewUtils.inject(this);
        llControl.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        btnTextSize.setVisibility(View.VISIBLE);
        btnMenu.setVisibility(View.GONE);


        btnBack.setOnClickListener(this);
        btnTextSize.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        mAmTvZan.setOnClickListener(this);
        mAmTvComment.setOnClickListener(this);
        mAmBSave.setOnClickListener(this);



//        mWebView.loadUrl("http://10.0.2.2:8080/zhbj/10007/724D6A55496A11726628.html");
        WebSettings settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(true);// 显示缩放按钮
        settings.setUseWideViewPort(true); // 支持双击缩放
//        settings.setJavaScriptEnabled(true);// 支持js功能
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setDefaultFontSize(600);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mUrl = getIntent().getStringExtra("url");
        mNewsId = getIntent().getStringExtra("newsId");
//        mWebView.loadUrl("https://www.baidu.com");
        mWebView.loadUrl(mUrl);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {// 开始加载网页
                super.onPageStarted(view, url, favicon);
                pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {// 网页加载结束
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("跳转链接："+ url);
                view.loadUrl(url);// 强制在当前webview中加载
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();// 接受所有网站的证书
            }
        });
//        mWebView.goBack();// 跳到上一页面
//        mWebView.goForward();// 跳到下一页面
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {// 进度发送变化
                System.out.println("进度：" + newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {// 网页标题
                System.out.println("网页标题:" + title);
                super.onReceivedTitle(view, title);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_textsize:
                showChooseDialog();
                break;
            case R.id.btn_share:
                showShare();
                break;
            case R.id.am_tv_comment:
                //点击评论按钮
                System.out.println("****************************************************");
                System.out.println("已点击评论按钮");
                System.out.println("****************************************************");
                comment(true);
                break;
            case R.id.am_b_save:
                // 跳到查看评论详情页面
                System.out.println("****************************************************");
                System.out.println("已在保存数据");
                System.out.println("****************************************************");
                comment(false);
                saveComment();
                break;
            case R.id.am_tv_zan:
                // 跳到查看评论详情页面
                getCommentsList();
                break;
            default:
                break;
        }
    }

    private int mTempWhich;// 记录临时选择的字体大小（点击确定之前）
    private int mCurrentWhich; // 记录当前选中的字体大小（点击确定之后）
    /**
     * 展示选择字体大小的弹窗
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        String[] items = new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};

        builder.setSingleChoiceItems(items, mCurrentWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTempWhich = which;
            }
        });
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = mWebView.getSettings();
                // 根据被选择的字体来修改网页字体大小
                switch (mTempWhich){
                    case 0:
                        // 超大字体
//                        settings.setDefaultFontSize(800);
                         settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        // 大号字体
//                        settings.setDefaultFontSize(700);
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        // 正常字体
//                        settings.setDefaultFontSize(600);
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        // 小号字体
//                        settings.setDefaultFontSize(500);
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        // 超小号字体
//                        settings.setDefaultFontSize(400);
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                    default:
                        break;
                }
                mCurrentWhich = mTempWhich;
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }
    private void showShare() {
        MobSDK.init(this);
        OnekeyShare oks = new OnekeyShare();
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(getString(R.string.share));
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
    }
    // 保存评论
    private void saveComment() {
        if (!TextUtils.isEmpty(mAmEtMsg.getText())) {
            info = mAmEtMsg.getText().toString();
            System.out.println("****************************************************");
            System.out.println("内容："+info);
            System.out.println("****************************************************");
            /**
             * 将数据插入到我本地的数据库
             */
            ContentResolver cr = NewsDetailActivity.this.getContentResolver();
            Uri uri = Uri.parse("content://com.daleyzou.zhbj/news_table");
            ContentValues values = new ContentValues();
            values.put("news_title",mNewsId);
            values.put("news_content",info);

            Uri result = cr.insert(uri,values);
            if (ContentUris.parseId(result) > 0){
                Toast.makeText(NewsDetailActivity.this,"数据增加成功！",Toast.LENGTH_SHORT).show();
            }
            //还原
            comment(false);
        } else {
            Toast.makeText(NewsDetailActivity.this, "请输入内容后在留言", Toast.LENGTH_SHORT).show();
        }
    }
    private void comment(boolean flag) {
        if(flag){
            mAmLlInfo.setVisibility(View.GONE);
            mAmLlLiuyan.setVisibility(View.VISIBLE);
            onFocusChange(flag);
        }else{
            mAmLlInfo.setVisibility(View.VISIBLE);
            mAmLlLiuyan.setVisibility(View.GONE);
            onFocusChange(flag);
        }


    }

    /**
     * 显示或隐藏输入法
     */
    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        NewsDetailActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    mAmEtMsg.setFocusable(true);
                    mAmEtMsg.requestFocus();
                } else {
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(mAmEtMsg.getWindowToken(), 0);
                }
            }
        }, 100);
    }

    /**
     * 从数据库中取出当前newsId对应的评论信息，并传递到CommentActivity中去
     */
    private void getCommentsList(){
        // com.daleyzou.zhbj
        ContentResolver cr = NewsDetailActivity.this.getContentResolver();
        Uri uri = Uri.parse("content://com.daleyzou.zhbj/news_table");

        Cursor c = cr.query(uri,null,null,null,null);
        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            System.out.println("评论newsId："+c.getString(1)+", 评论内容"+c.getString(2));
            if (c.getString(1).equals(mNewsId)){
                coments.add(c.getString(2));
            }

        }

        Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
        Bundle b=new Bundle();
        b.putStringArrayList("coments", (ArrayList<String>) coments);
        intent.putExtras(b);
        NewsDetailActivity.this.startActivity(intent);
    }
}

