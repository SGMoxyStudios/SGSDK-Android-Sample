package com.example.sgsdktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.sgstudios.rd.*;

public class MainActivity extends AppCompatActivity
{
    private final String TAG = "MainActivity";
    private final String GameKey = "d7e73390-d7fd-11e6-9074-21c247f06802";
    private final String AppSecret = "d7d7c810-d7ee-11e6-9e86-db4f79aeee86";

    private TextView _txtLog;
    private boolean isWidgetShown=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _txtLog = (TextView) findViewById(R.id.txtLog);

        setSDKCallback();
        SgSDK.getInstance().init(MainActivity.this,GameKey,AppSecret);

        findViewById(R.id.btnAPICall).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                UILog("Init SDK...");
                SgSDK.getInstance().init(MainActivity.this,GameKey,AppSecret);
            }
        });

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                UILog("Launch Login WebView...");
                SgSDK.getInstance().login();
            }
        });

        findViewById(R.id.btnBuy).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //UILog("SDK buy...");
                //SgSDK.getInstance().pay("com.sgstudios.paytest1");

                SgSDKPayRequest req = new SgSDKPayRequest();
                req.setProductId("com.sgstudios.paytest1");
                req.setProductName("My Product");
                req.setPaymentMethod("managed");
                req.setPaymentChannel("WeChat");
                req.setProductDesc("Product Description......");
                req.setPrice(1.25f);
                req.setServerId("My ServerId");
                req.setServerName("My Server Name");
                req.setRoleId("My Role Id");
                req.setRoleName("My Role Name");
                req.setRoleLevel(1);
                req.setPayNotifyUrl("PAY_NOTIFY_URL");

                SgSDK.getInstance().pay(req);
            }
        });

        findViewById(R.id.btnWidget).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(isWidgetShown)
                {
                    SgSDK.getInstance().hideWidget();
                    isWidgetShown = false;
                }
                else
                {
                    SgSDK.getInstance().showWidget("ML");
                    isWidgetShown = true;
                }
            }
        });

        findViewById(R.id.btnSignup).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SgSDK.getInstance().signUp();
            }
        });

        findViewById(R.id.btnForgetPass).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SgSDK.getInstance().forgetPassword();
            }
        });
        findViewById(R.id.btnChangePass).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SgSDK.getInstance().changePassword();
            }
        });
        findViewById(R.id.btnMyAccount).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SgSDK.getInstance().myAccount();
            }
        });
        findViewById(R.id.btnParentalLock).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SgSDK.getInstance().parentalLock();
            }
        });
        findViewById(R.id.btnChildData).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SgSDK.getInstance().myKid();
            }
        });

    }

    private void animateView(final ImageView imgView, final float finalX, final float finalY)
    {
        TranslateAnimation animation = new TranslateAnimation(0, finalX-imgView.getX(), 0, finalY-imgView.getY());
        animation.setDuration(500);  // animation duration
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation)
            {
                imgView.setX(finalX);
                imgView.setY(finalY);
                imgView.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation){}
        });

        imgView.startAnimation(animation);
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

    private void setSDKCallback()
    {
        UILog("Prepare SDK Listener...");

        SgSDK.getInstance().setListener(new SgSDKListener()
        {
            @Override
            public void onCallBack(SgSDKResult result)
            {
                UILog(result.toString());

                switch (result.getCode())
                {
                    case 101: // init OK
                        break;

                    case 102: // init Fail
                        break;

                    case 201: // login OK
                        UILog("OpenId: "+SgSDK.getInstance().getOpenId());
                        UILog("SessionId: "+SgSDK.getInstance().getSessionId());
                        UILog("Token: "+SgSDK.getInstance().getToken());
                        UILog("IsLogin: "+SgSDK.getInstance().isLogin());
                        break;

                    case 202: // login Fail
                    case 203: // login Fail
                    case 204: // login Fail
                    case 205: // login Fail
                        break;

                    case 1101: // pay OK
                        SgSDKPayResponse resp = (SgSDKPayResponse)result.getData();
                        UILog(resp.getRawJson());
                        break;


                    //..............................
                }
            }
        });
    }

    private void UILog(String msg)
    {
        _txtLog.append(msg+"\n");
    }

}
