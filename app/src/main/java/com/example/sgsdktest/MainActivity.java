package com.example.sgsdktest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.sgstudios.sdk.*;

import java.util.UUID;

public class MainActivity extends AppCompatActivity
{
    private final String TAG = "MainActivity";
    private final String GameKey = "d7e73390-d7fd-11e6-9074-21c247f06802";
    private final String AppSecret = "d7d7c810-d7ee-11e6-9e86-db4f79aeee86";

    private TextView _txtLog;
    private String _cmd="";

    private SGSDK _sdk = SGSDK.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _txtLog = (TextView) findViewById(R.id.txtLog);

        //_sdk.setHost("http://192.168.201.12:12000");
        setSDKCallback();
        //_sdk.init(MainActivity.this,GameKey,AppSecret);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.api_methods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                _cmd = ""+parent.getItemAtPosition(position);
                runCommand();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                _cmd="";
            }
        });

        findViewById(R.id.btnGo).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view){ runCommand(); }
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

    private void runCommand()
    {
        if(_cmd.contains("[API] Init"))
        {
            _sdk.init(MainActivity.this,GameKey,AppSecret);
        }
        else if(_cmd.contains("[WebView] Login"))
        {
            _sdk.login();
        }
        else if(_cmd.contains("[WebView] Signup"))
        {
            _sdk.signUp();
        }
        else if(_cmd.contains("[WebView] Forget Password"))
        {
            _sdk.forgetPassword();
        }
        else if(_cmd.contains("[WebView] Change Password"))
        {
            _sdk.changePassword();
        }
        else if(_cmd.contains("[WebView] My Account"))
        {
            _sdk.myAccount();
        }
        else if(_cmd.contains("[WebView] My Kid"))
        {
            _sdk.myKid();
        }
        else if(_cmd.contains("[WebView] Select Kid"))
        {
            _sdk.selectKid();
        }
        else if(_cmd.contains("[WebView] Parental Lock"))
        {
            _sdk.parentalLock();
        }

        else if(_cmd.contains("Login By Token"))
        {
            _sdk.loginByToken(_sdk.getToken());
        }
        else if(_cmd.contains("Verify Session"))
        {
            _sdk.verifySession(GameKey,  _sdk.getSessionID(), _sdk.getOpenID(), "SIGN");
        }
        else if(_cmd.contains("OpenID"))
        {
            _sdk.openId();
        }
        else if(_cmd.contains("Game Start"))
        {
            _sdk.gameStart();
        }
        else if(_cmd.contains("Game Stop"))
        {
            _sdk.gameStop();
        }
        else if(_cmd.contains("Logout"))
        {
            _sdk.logout();
        }

        else if(_cmd.contains("Google Pay"))
        {
            _sdk.pay(initPayReq("com.sgstudios.paytest1", "managed"));
        }
        else if(_cmd.contains("Google Subscribe"))
        {
            _sdk.pay(initPayReq("com.sgstudions.subscribetest1","subscribe"));
        }
        else if(_cmd.contains("Google ConsumePurchasedItems"))
        {
            _sdk.consumePurchasedItems();
        }

        else if(_cmd.contains("WeChat Pay"))
        {
            String order = "wx_"+ UUID.randomUUID().toString();
            SGPayRequest req = initWeChatOrder(order.substring(0,10), "元宝", 10);
            _sdk.pay(req);
        }
        else if(_cmd.contains("Get Order"))
        {
            _sdk.getOrder("ID", GameKey, _sdk.getOpenID(), "SIGN");
        }
        else if(_cmd.contains("[Widget] Show"))
        {
            _sdk.showWidget(SGSDK.EWidgetLocation.Left);
        }
        else if(_cmd.contains("[Widget] Hide"))
        {
            _sdk.hideWidget();
        }
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

        _sdk.setListener(new SGListener()
        {
            @Override
            public void onCallBack(SGListenerResult result)
            {
                UILog(result.toString());
                if(result.Data!=null) UILog("   -- Result Data: " + result.Data.toString());

                switch (result.Code)
                {
                    case 101: // init OK
                        UILog("getChannelId(): "+_sdk.getChannelID());
                        break;

                    case 201: // login OK
                        UILog("getOpenId(): "+_sdk.getOpenID());
                        UILog("getSessionId(): "+_sdk.getSessionID());
                        UILog("getToken(): "+_sdk.getToken());
                        UILog("isLogin(): "+_sdk.isLogin());
                        break;

                    case 251: // openId result
                        break;

                    case 1101: // pay OK
                        SGPayResponse resp = (SGPayResponse)result.Data;
                        if(resp!=null)
                        {
                            UILog("SGPayResponse: "+resp.rawJson);
                        }
                        break;

                    case 1102: // pay fail
                    case 1103: // pay fail
                    case 1104: // pay fail
                    case 1105: // pay fail
                    case 1106: // pay fail
                    case 1107: // pay fail
                    case 1108: // pay fail
                        SGPayResponse resp2 = (SGPayResponse)result.Data;
                        if(resp2!=null) { UILog("SGPayResponse: "+resp2.rawJson); }
                        break;
                    case 1120: // wechat pay result
                        SGPayResponse resp3 = (SGPayResponse)result.Data;
                        if(resp3!=null) { UILog("SGPayResponse: "+resp3.receipt); }
                        break;
                    case 1151: // google pay init fail
                    case 1161: // google subscribe ok
                    case 1162: // google subscribe ok
                    case 1171: // google pay ok
                    case 1172: // google pay fail
                    case 1182: // google consume fail

                    case 9900: //SDK GetMetaData Error
                    case 9910: //SDK WebAPI Call Error
                    case 9920: //SDK WebAPI Response Data Error

                }
            }
        });


    }

    private SGPayRequest initPayReq(String pid, String type)
    {
        SGPayRequest req = new SGPayRequest();
        req.productId = pid;
        req.productName = "My Product";
        req.paymentMethod = type; //"managed" or "subscribe"
        req.paymentChannel = "GooglePay"; //GooglePay, WeChat,.........
        req.productDesc = "Product Description......";
        req.price = 1.25f;

        return req;
    }

    private SGPayRequest initWeChatOrder(String orderID, String pName, float price)
    {
        SGPayRequest req = new SGPayRequest();
        req.orderId = orderID;
        req.productName = pName;
        req.price = price;

        req.productId = "wx_diamond";
        req.paymentChannel = "WeChat";
        req.trade_type = "APP";
        req.productDesc = "Product Description......";
        req.spbill_create_ip = "14.23.150.211";
        req.notify_url = "http://gameapi.smartgamesltd.com/wechat-pay";
        req.u8NotifyUrl = "http://www.sgstudios.cn:8080/pay/sg/payCallback/1706";
        String random_str = UUID.randomUUID().toString().replace("-", "");
        req.nonce_str = random_str.substring(0,10); //random number

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
