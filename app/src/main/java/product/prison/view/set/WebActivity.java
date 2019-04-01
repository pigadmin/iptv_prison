package product.prison.view.set;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import product.prison.BaseActivity;
import product.prison.R;

/**
 * Created by zhu on 2017/9/26.
 */

public class WebActivity extends BaseActivity {

    private WebView fram_web;
    private String Home = "https://www.baidu.com/";


    @Override
    public void onBackPressed() {
        if (fram_web.canGoBack()) {
            fram_web.goBack();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        fram_web = f(R.id.fram_web);
    }

    @Override
    public void loadData() {
        try {
            Home = getIntent().getStringExtra("key");
            WebSettings websettings = fram_web.getSettings();
            websettings.setJavaScriptEnabled(true);
            websettings.setBuiltInZoomControls(true);
            fram_web.loadUrl(Home);
            fram_web.setBackgroundColor(Color.TRANSPARENT);
            fram_web.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public int getContentId() {
        return R.layout.activity_web;
    }
}
