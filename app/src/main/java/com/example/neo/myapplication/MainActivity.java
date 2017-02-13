package com.example.neo.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceRequest;
import android.util.Log;
import android.webkit.ValueCallback;
import java.io.Console;

public class MainActivity extends AppCompatActivity {
    String URL_Init = "http://gameapi.smartgamesltd.com/init";
    String GameKey = "d7e73390-d7fd-11e6-9074-21c247f06802";
    String AppSecret = "d7d7c810-d7ee-11e6-9e86-db4f79aeee86";
    String PrivateKey = "azlxEUasGMOf1VoygA9Jvta76pG8QnoC";
    WebView mWebView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mWebView = (WebView)findViewById(R.id.webView);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setInitialScale(1);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setJavaScriptEnabled(true);

        Init(GameKey, AppSecret, PrivateKey);
    }

    public boolean IsInit = false;
    public int Kind = 0; //1.init 2.login 3.openid
    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d("URL", url);
            Kind = 0;
            String js = "ResultCode()";

            if (url.contains("init"))
                Kind = 1;
            else
            if (url.contains("login"))
                Kind = 2;
            else
            if (url.contains("openid")) {
                Kind = 3;
                js = "GetOpenID()";
            }

            view.evaluateJavascript(js, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.d("value", value);

                    switch (Kind) {
                        case 1:
                            if (!IsInit && value.equals("1")) {
                                IsInit = true;
                                Login();
                            }

                            break;
                        case 2:
                            if (value.equals("1"))
                                GetOpenID();

                            break;
                        case 3:
                            Log.d("Open ID is ", value);

                            break;
                    }
                }
            });
        }
    };

    //1.
    public void Init(String gamekey, String appsecret, String privatekey) {
        mWebView.loadUrl(String.format("%s?gamekey=%s&appsecret=%s&privatekey=%s", URL_Init, gamekey, appsecret, privatekey));
    }

    //2.
    public void Login() {
        mWebView.loadUrl("http://gameapi.smartgamesltd.com/login");
    }

    //3.
    public void GetOpenID() {
        mWebView.loadUrl("http://gameapi.smartgamesltd.com/openid");
    }
}
