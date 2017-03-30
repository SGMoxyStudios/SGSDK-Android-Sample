package com.example.sgsdktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import android.content.Intent;
import android.widget.ScrollView;

import com.sgstudios.rd.SgSDK;
import com.sgstudios.rd.SgSDKListener;
import com.sgstudios.rd.SgSDKPayRequest;
import com.sgstudios.rd.SgSDKPayResponse;
import com.sgstudios.rd.SgSDKResult;

public class MainActivity extends AppCompatActivity
{
    private final String TAG = "MainActivity";
    private final String GameKey = "d7e73390-d7fd-11e6-9074-21c247f06802";
    private final String AppSecret = "d7d7c810-d7ee-11e6-9e86-db4f79aeee86";

    private TextView _txtLog;
    private ScrollView _scroller;
    private boolean isWidgetShown=false;

    private SgSDK _sdk = SgSDK.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _txtLog = (TextView) findViewById(R.id.txtLog);
        _scroller = (ScrollView) findViewById(R.id.scroller);

        setSDKCallback();
        //_sdk.init(MainActivity.this,GameKey,AppSecret);

        findViewById(R.id.btnInit).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.init(MainActivity.this,GameKey,AppSecret);
            }
        });

        findViewById(R.id.btnAPICall).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                UILog("Restore Purchase...");
                _sdk.restorePurchasedItems();
            }
        });

        findViewById(R.id.btnSubscribe).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.pay(initPayReq("com.sgstudions.subscribetest1","subscribe"));
            }
        });

        findViewById(R.id.btnBuy).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.pay(initPayReq("com.sgstudios.paytest1","managed"));
            }
        });


        findViewById(R.id.btnWidget).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(isWidgetShown)
                {
                    _sdk.hideWidget();
                    isWidgetShown = false;
                }
                else
                {
                    _sdk.showWidget("ML");
                    isWidgetShown = true;
                }
            }
        });

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.login();
            }
        });

        findViewById(R.id.btnSignup).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.signUp();
            }
        });

        findViewById(R.id.btnForgetPass).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.forgetPassword();
            }
        });
        findViewById(R.id.btnChangePass).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.changePassword();
            }
        });
        findViewById(R.id.btnMyAccount).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.myAccount();
            }
        });
        findViewById(R.id.btnParentalLock).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.parentalLock();
            }
        });
        findViewById(R.id.btnChildData).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.myKid();
            }
        });

        findViewById(R.id.btnVerifySession).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.verifySession(GameKey,  _sdk.getSessionId(), _sdk.getOpenId(), "signature");
            }
        });
        findViewById(R.id.btnVerifyToken).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.verifyToken(_sdk.getToken());
            }
        });
        findViewById(R.id.btnOpenId).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.openId();
            }
        });

        findViewById(R.id.btnGameStart).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.gameStart();
            }
        });
        findViewById(R.id.btnGameStop).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.gameStop();
            }
        });
        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _sdk.logout();
            }
        });
        findViewById(R.id.btnClr).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                clearUILog();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        _sdk.onResume();
    }

    @Override
    public void onBackPressed()
    {
        if(_sdk.onBackPressed())
        {
            UILog("[Action] BackPressed is handled by SDK...");
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        _sdk.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy()
    {
        _sdk.onDestroy();
        super.onDestroy();
    }

    //--------------------------------------------------------------------------------------
    private void setSDKCallback()
    {
        UILog("Prepare SDK Listener...");

        _sdk.setListener(new SgSDKListener()
        {
            @Override
            public void onCallBack(SgSDKResult result)
            {
                UILog(result.toString());
                if(result.getData()!=null) UILog("[Result Obj]: " + result.getData().toString());

                switch (result.getCode())
                {
                    case 101: // init OK
                        UILog("ChannelId: "+_sdk.getChannelId());
                        break;

                    case 201: // login OK
                        UILog("OpenId: "+_sdk.getOpenId());
                        UILog("SessionId: "+_sdk.getSessionId());
                        UILog("Token: "+_sdk.getToken());
                        UILog("IsLogin: "+_sdk.isLogin());
                        break;

                    case 251: // openId result
                        break;

                    case 1101: // pay OK
                    case 1102: // pay fail
                    case 1103: // pay fail
                    case 1104: // pay fail
                    case 1105: // pay fail
                    case 1106: // pay fail
                    case 1107: // pay fail
                    case 1108: // pay fail
                        SgSDKPayResponse resp = (SgSDKPayResponse)result.getData();
                        if(resp!=null)
                        {
                            UILog(resp.getRawJson());
                        }
                        break;

                    case 1151: // google pay init fail
                    case 1161: // google subscribe ok
                    case 1162: // google subscribe ok
                    case 1171: // google pay ok
                    case 1172: // google pay fail
                    case 1182: // google consume fail

                    case 9997: //SDK GetMetaData Error
                    case 9998: //SDK JSONException
                }
            }
        });


    }
    private SgSDKPayRequest initPayReq(String pid, String type)
    {
        SgSDKPayRequest req = new SgSDKPayRequest();
        req.setProductId(pid);
        req.setProductName("My Product");
        req.setPaymentMethod(type); //"managed" or "subscribe"
        req.setPaymentChannel("GooglePay"); //GooglePay, WeChat,.........
        req.setProductDesc("Product Description......");
        req.setPrice(1.25f);
        req.setServerId("My ServerId");
        req.setServerName("My Server Name");
        req.setRoleId("My Role Id");
        req.setRoleName("My Role Name");
        req.setRoleLevel(1);
        req.setPayNotifyUrl("PAY_NOTIFY_URL");
        return req;
    }

    private void UILog(String msg)
    {
        _txtLog.append(msg+"\n");

    }
    private void clearUILog()
    {
        _txtLog.setText("");
    }


}