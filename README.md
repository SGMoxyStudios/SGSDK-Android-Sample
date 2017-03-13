SgSDK Quick Usage Guide

1. Reference

Add SgSDK.jar as a library in your project.

2. Preparation

Permission

Add the following permissions to AndroidManifest.xml file.

<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

Register a Listener

Before invoking any methods of the SDK, register a SgSDKListener to monitor onCallBack method, which return a SgSDKResult object.


SgSDK.getInstance().setListener(new SgSDKListener()
{
   @Override
   public void onCallBack(SgSDKResult result)
   {
       Log.i("[CallBackMsg]: "+ result.toString());

       switch (result.getCode())
       {
           case 101: // init OK
               Log.i("[Action] Init OK --> do something.");
               break;

           case 102: // init Fail
               Log.i("[Action] Init Failed --> do something.");
               break;

           case 201: // login OK
               Log.i("[Action] Login OK --> do something.");
               Log.i("[Action] OpenId: "+SgSDK.getInstance().getOpenId());
               Log.i("[Action] SessionId: "+SgSDK.getInstance().getSessionId());
               Log.i("[Action] Token: "+SgSDK.getInstance().getToken());
               break;

           case 202: // login Fail
           case 203: // login Fail
           case 204: // login Fail
           case 205: // login Fail
               Log.i("[Action] Login Failed --> do something.");
               break;
       }
   }
});

Override Events

Override onBackPressed event (for now) of the Activity, and invoke SDK’s onBackPressed event.  Mainly because of the internal WebView component overlaying behavior.

@Override
public void onBackPressed()
{
   if(SgSDK.getInstance().onBackPressed())
   {
       Log.i("[Action] BackPressed is handled by SDK...");
   }
   else
   {
       super.onBackPressed();
   }
}




3. Initialization


Initialize SDK with your GameKey, AppSecrect, as well as Activity Context object.

SgSDK.getInstance().init(MainActivity.this, GameKey, AppSecret);


4. Login

(NOTE: you can only invoke the method after initialization is successful.)

SgSDK.getInstance().login();

Login method will launch a WebView component overlaying the main activity.
You can obtain Open Id, Token, and Session Id from SDK callback after successful login.
(please refer the sample codes above)
