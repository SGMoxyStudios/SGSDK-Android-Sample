package com.example.neo.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceRequest;
import android.net.http.*;
import android.util.Log;
import android.webkit.ValueCallback;
import java.net.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import com.android.volley.*;
import com.android.volley.toolbox.*;

public class MainActivity extends AppCompatActivity {
    //String URL_Init = "http://gameapi.smartgamesltd.com/init";
    String URL_Init = "http://192.168.201.247:12000/init";
    //String GameKey = "d7e73390-d7fd-11e6-9074-21c247f06802";
    String GameKey = "0e0c3020-dd2f-11e6-ae80-6ffc5013c85a";
    //String AppSecret = "d7d7c810-d7ee-11e6-9e86-db4f79aeee86";
    String AppSecret = "f84b3010-dd2e-11e6-ae80-6ffc5013c85a";
    //String PrivateKey = "azlxEUasGMOf1VoygA9Jvta76pG8QnoC";
    String PrivateKey = "KjUQMi2FCMeddTeDqc5gkUYbn97xAuKQ";
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

            if (url.contains("init")) {
                Kind = 1;

            } else
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
        /*
        String url = String.format("%s?gamekey=%s&appsecret=%s&privatekey=%s&channel=googleplay", URL_Init, gamekey, appsecret, privatekey);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Response is ", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response error ", error.toString());
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
*/
        /*
        try {
            URL url = null;
            String response = null;
            String parameters = String.format("gamekey=%s&appsecret=%s&privatekey=%s", gamekey, appsecret, privatekey);
            url = new URL(URL_Init);
            //create the connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //set the request method to GET
            connection.setRequestMethod("GET");

            //get the output stream from the connection you created
            OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());
            //write your data to the ouputstream
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            //create your inputsream
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            //read in the data from input stream, this can be done a variety of ways
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            //get the string version of the response data
            response = sb.toString();
            //do what you want with the data now

            //always remember to close your input and output streams
            isr.close();
            reader.close();
            Log.d("req : ", response);
        } catch (IOException e) {
            Log.e("HTTP GET:", e.toString());
        }*/
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
