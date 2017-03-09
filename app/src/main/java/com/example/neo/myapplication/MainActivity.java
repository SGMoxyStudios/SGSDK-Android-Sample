package com.example.neo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.CookieManager;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String Host = "http://gameapi.smartgamesltd.com/";
    String URL_Init = "init";
    String GameKey = "d7e73390-d7fd-11e6-9074-21c247f06802";
    String AppSecret = "d7d7c810-d7ee-11e6-9e86-db4f79aeee86";
    String PrivateKey = "azlxEUasGMOf1VoygA9Jvta76pG8QnoC";
//    String Host = "http://192.168.201.229:12000/";
//    String URL_Init = "init";
//    String GameKey = "0e0c3020-dd2f-11e6-ae80-6ffc5013c85a";
//    String AppSecret = "f84b3010-dd2e-11e6-ae80-6ffc5013c85a";
//    String PrivateKey = "KjUQMi2FCMeddTeDqc5gkUYbn97xAuKQ";
    WebView mWebView = null;
    Map<String, String> webViewCookie = new HashMap<String, String>();
    String cookie;
    CookieManager cookieManager;

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

        cookieManager = CookieManager.getInstance();
        Init(GameKey, AppSecret, PrivateKey);
    }

    public boolean IsInit = false;
    public int Kind = 0; //1.init 2.login 3.openid
    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d("URL", url);
            if (url.contains("login")) {
                view.evaluateJavascript("ResultCode()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d("Login code is ", value);
                    }
                });

                view.evaluateJavascript("GetOpenID()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d("Login openid is ", value);
                    }
                });

                view.evaluateJavascript("GetToken()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d("Login token is ", value);
                    }
                });

                view.evaluateJavascript("GetSessionID()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d("Login session id is ", value);
                    }
                });
            }
        }
    };

    //1.
    public void Init(String gamekey, String appsecret, String privatekey) {
        String url = String.format("%s?gamekey=%s&appsecret=%s&privatekey=%s&channel=googleplay", Host + URL_Init, gamekey, appsecret, privatekey);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Response is ", response);
                        Login();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response error ", error.toString());
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.headers.containsKey("Set-Cookie")) {
                    cookie = response.headers.get("Set-Cookie");
                }

                return super.parseNetworkResponse(response);
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    //2.
    public void Login() {
        cookieManager.setCookie(Host + "login", cookie);
        mWebView.loadUrl(Host + "login");
    }
}