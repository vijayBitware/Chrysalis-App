package com.chrysalis.domain;

import android.app.Application;
import android.os.StrictMode;

import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ChrysalisApplication extends Application implements IAdobeAuthClientCredentials {

    /* Be sure to fill in the two strings below. */
    private static final String CREATIVE_SDK_CLIENT_ID = "ca057635dce745ae84bc8ca567baaa0d";
    private static final String CREATIVE_SDK_CLIENT_SECRET = "d3f08062-a693-41b3-b0fa-96f6b9937b17";
    private static final String CREATIVE_SDK_REDIRECT_URI = "ams+0962a45ce18cc600b92ab1d223d07dca7f73ed3f://adobeid/ca057635dce745ae84bc8ca567baaa0d";
    private static final String[] CREATIVE_SDK_SCOPES = {"email", "profile", "address"};

    public String Mode;

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());

    }

    /* @Override
     protected void attachBaseContext(Context base) {
         super.attachBaseContext(base);
         MultiDex.install(ChrysalisApplication.this);
     }*/
    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    @Override
    public String getClientID() {
        return CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_CLIENT_SECRET;
    }

    @Override
    public String[] getAdditionalScopesList() {
        return CREATIVE_SDK_SCOPES;
    }

    @Override
    public String getRedirectURI() {
        return CREATIVE_SDK_REDIRECT_URI;
    }

}


