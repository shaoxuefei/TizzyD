package com.shanlin.sxf;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.shanlin.sxf.softkeybord.KeySoftBordNormalView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyWebViewActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.tv_reload)
    TextView tv_reload;
    @BindView(R.id.tv_message)
    TextView tv_message;
    @BindView(R.id.softInputKey)
    KeySoftBordNormalView keySoftBordNormalViewl;

    String[] strings = new String[]{"2018-10-26", "2018-10-19", "2018-10-12"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web_view);
        ButterKnife.bind(this);
        initWebView(webView);
        //http://tg.zhaohaogu.cn/teacher.html
        webView.loadUrl("http://fanyi.youdao.com/");

        tv_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = strings[(int) (Math.random() * 2)];
                webView.loadUrl("http://fanyi.youdao.com/");
                tv_reload.setText(date);
            }
        });


        keySoftBordNormalViewl.setBaseActivity(this);
        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keySoftBordNormalViewl.showSoftInputView(true, null);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    keySoftBordNormalViewl.dismissSoftView();
                }
            });
        }
    }

    private void initWebView(WebView mWebView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        WebSettings settings = mWebView.getSettings();
//        String ua = settings.getUserAgentString() + "GoGoaler-invest/" + BuildConfig.VERSION_NAME;
//        settings.setUserAgentString(ua);
        // 开启DOM缓存。
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        // 设置显示缩放按钮
        settings.setBuiltInZoomControls(false);
        //支持缩放
        settings.setSupportZoom(false);
        //适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView.setWebViewClient(new MyWebClient());

    }

    class MyWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            String urls = url;
        }

        //重定向的Url
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //重定向Url-->讲原先的Url-封装转换后显示真正的Url
            //但是对于一般的Url返回值可能没啥影响，但是如果H5链接内部有重定向功能的话那么返回值就会有影响了
            return super.shouldOverrideUrlLoading(view, url);
        }

        //正常重写这个shouldOverrideUrlLoading(WebView view, String request)方法也行
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                String url = request.getUrl().toString();
//            } else {
//                String url = request.toString();
//            }
//            return super.shouldOverrideUrlLoading(view, request);
//        }


        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            //拦截Url的请求
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String urle = url;
        }
    }

    class WebChromeClient extends android.webkit.WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    }

}
