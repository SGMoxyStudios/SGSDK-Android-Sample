package com.example.sgsdktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sgstudios.rd.*;

public class MainActivity extends AppCompatActivity
{
    private final String TAG = "MainActivity";
    private final String GameKey = "d7e73390-d7fd-11e6-9074-21c247f06802";
    private final String AppSecret = "d7d7c810-d7ee-11e6-9e86-db4f79aeee86";

    private TextView _txtLog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _txtLog = (TextView) findViewById(R.id.txtLog);

        setSDKCallback();

        Button btnAPICall = (Button) findViewById(R.id.btnAPICall);
        btnAPICall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                UILog("[Action] Init SDK...");
                SgSDK.getInstance().init(MainActivity.this,GameKey,AppSecret);
            }
        });

        Button btnWebView = (Button) findViewById(R.id.btnWebView);
        btnWebView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                UILog("[Action] Launch Login WebView...");
                SgSDK.getInstance().login();
            }
        });

    }

        @Override
        public void onBackPressed()
        {
            if(SgSDK.getInstance().onBackPressed())
            {
                UILog("[Action] BackPressed is handled by SDK...");
            }
            else
            {
                super.onBackPressed();
            }
        }

    //--------------------------------------------------------------------------------------
    private void setSDKCallback()
    {
        UILog("[Action] Set SDK Listener begin...");

        SgSDK.getInstance().setListener(new SgSDKListener()
        {
            @Override
            public void onCallBack(SgSDKResult result)
            {
                UILog("[CallBackMsg]: "+ result.toString());

                switch (result.getCode())
                {
                    case 101: // init OK
                        UILog("[Action] Init OK --> do something.");
                        break;

                    case 102: // init Fail
                        UILog("[Action] Init Failed --> do something.");
                        break;

                    case 201: // login OK
                        UILog("[Action] Login OK --> do something.");
                        UILog("[Action] OpenId: "+SgSDK.getInstance().getOpenId());
                        UILog("[Action] SessionId: "+SgSDK.getInstance().getSessionId());
                        UILog("[Action] Token: "+SgSDK.getInstance().getToken());
                        break;

                    case 202: // login Fail
                    case 203: // login Fail
                    case 204: // login Fail
                    case 205: // login Fail
                        UILog("[Action] Login Failed --> do something.");
                        break;

                    //..............................
                }
            }
        });

        UILog("[Action] Set SDK Listener done.");
    }

    private void UILog(String msg)
    {
        _txtLog.append(msg+"\n");
    }

}
