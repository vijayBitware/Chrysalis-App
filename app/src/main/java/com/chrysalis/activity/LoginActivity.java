package com.chrysalis.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chrysalis.R;
import com.chrysalis.domain.AppPreference;
import com.chrysalis.domain.Config;
import com.chrysalis.domain.ConnectionDetector;
import com.chrysalis.domain.api.APIRequest;
import com.chrysalis.model.signInData.LoginResponse;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chrysalis.domain.Config.BASE_URL;
import static com.chrysalis.domain.Config.EMAIL_REGEX;

/**
 * Created by Bitware Marketing on 22-05-2017.
 */

public class LoginActivity extends Activity {
    @BindView(R.id.etLoginPass)
    EditText etLoginPass;
    @BindView(R.id.etLoginEmail)
    EditText etLoginEmail;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.tvForgotPass)
    TextView tvForgotPass;
    @BindView(R.id.tvRegister)
    TextView tvRegister;

    String email = "", pass = "";
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;

    LoginButton login_button;
    Button fbLogin;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AppPreference appPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sharedPreferences = this.getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        appPreference = new AppPreference(LoginActivity.this);

        login_button = findViewById(R.id.login_button);
        fbLogin = findViewById(R.id.fbLogin);

        // etLoginEmail.setText("dipakkadamdpk@gmail.com");
        // etLoginPass.setText("dipak1234");

        checkFBKeyHashesh();

        etLoginPass.setTransformationMethod(new PasswordTransformationMethod());
        facebookLogin(); //Called after clickOn Faceebook Signup
    }

    private void checkFBKeyHashesh() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.chrysalis",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    @OnClick(R.id.btnLogin)
    public void LoginClick() {
        email = etLoginEmail.getText().toString().trim();
        pass = etLoginPass.getText().toString().trim();
        if (!email.equals("")) {
            if (email.matches(EMAIL_REGEX)) {
                if (!pass.equals("")) {
                    if (isInternetPresent) {
                        String loginURL = BASE_URL + "login";
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("email", email);
                            jsonObject.put("password", pass);
                            //callLoginAPI(loginURL,jsonObject);
                            new APIRequest(LoginActivity.this, jsonObject, loginURL, Config.API_LOGIN, Config.POST);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    etLoginPass.requestFocus();
                    etLoginPass.setError("Please enter password");
                }
            } else {
                etLoginEmail.requestFocus();
                etLoginEmail.setError("Please enter valid email");
            }
        } else {
            etLoginEmail.requestFocus();
            etLoginEmail.setError("Please enter email");
        }
    }

    @Subscribe
    public void getLoginData(LoginResponse loginResponse) {

        System.out.println("*****res******" + loginResponse.toString());
        if (loginResponse.getResult().equalsIgnoreCase("success")) {
            Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
            editor.putString("isLogin", "Yes");
            editor.putString("userId", loginResponse.getData().get(0).getUser().getId());
            appPreference.setUserId(loginResponse.getData().get(0).getUser().getId());
            editor.putString("email", loginResponse.getData().get(0).getUser().getEmail());
            editor.putString("firstName", loginResponse.getData().get(0).getUser().getFirstName());
            editor.putString("lastName", loginResponse.getData().get(0).getUser().getLastName());
            editor.putString("userName", loginResponse.getData().get(0).getUser().getUsername());
            editor.putString("status", loginResponse.getData().get(0).getUser().getActive().toString());
            editor.putString("KEY", "empty");
            editor.commit();

            Intent i = new Intent(LoginActivity.this, ProductListingScreen.class);
            startActivity(i);
            finish();

        } else {
            Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    /*    private void callLoginAPI(String loginURL, JSONObject jsonObject) {
           progressDialog = new ProgressDialog(this);
          //  String  REQUEST_TAG = "com.example.bitwarepc.vollydemo.volleyStringRequest";
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(loginURL, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Log.d(TAG, response.toString());
                            progressDialog.hide();
                            Gson gson = new Gson(); // Or use new GsonBuilder().create();
                            LoginResponse loginResponse = gson.fromJson(response.toString(),LoginResponse.class);

                            if(loginResponse.getResult().equalsIgnoreCase("success")){
                                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                editor.putString("isLogin","Yes");
                                editor.putString("userId",loginResponse.getData().get(0).getUser().getId());
                                appPreference.setUserId(loginResponse.getData().get(0).getUser().getId());
                                editor.putString("email",loginResponse.getData().get(0).getUser().getEmail());
                                editor.putString("firstName",loginResponse.getData().get(0).getUser().getFirstName());
                                editor.putString("lastName",loginResponse.getData().get(0).getUser().getLastName());
                                editor.putString("displayName",loginResponse.getData().get(0).getUser().getLastName());
                                editor.commit();

                                Intent i = new Intent(LoginActivity.this,ProductListingScreen.class);
                                startActivity(i);
                                finish();

                            }else{
                                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.hide();
                  //  VolleyLog.d(TAG, "Error: " + error.getMessage());

                }
            });

            // Adding String request to request queue
            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq);
        }*/

    @OnClick(R.id.tvRegister)
    public void registerClick() {
        Intent i = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.tvForgotPass)
    public void ForgetPassClick() {
        Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(i);
        finish();
    }

    private void facebookLogin() {
        login_button.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        callbackManager = CallbackManager.Factory.create();

        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject fbObject, GraphResponse response) {
                                LoginManager.getInstance().logOut();
                                String strFbId = "", faceBookEmail = "", password = "";
                                try {
                                    strFbId = fbObject.getString("id");

                                    if (fbObject.has("email")) {
                                        if (!fbObject.getString("email").equals("") || !fbObject.getString("email").equals("null")) {
                                            faceBookEmail = fbObject.getString("email");
                                            callFacebookLogin(strFbId, faceBookEmail, password);
                                        }
                                    } else {
                                        callFacebookLogin(strFbId, faceBookEmail, password);
                                    }

                                    //  callFacebookSignUpAPI(strFbId,fbName,first_name,lst_name,faceBookEmail);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,last_name,first_name,email");
                request.setParameters(parameters);
                request.executeAsync();

                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                               AccessToken currentAccessToken) {
                        if (currentAccessToken == null) {
                            //tv_profile_name.setText("");

                        }
                    }
                };
            }

            @Override
            public void onCancel() {
                System.out.println("*****cancel***");

            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("****error***" + error.toString());

            }

        });
    }

    private void callFacebookLogin(String strFbId, String faceBookEmail, String password) {
        String loginURL = BASE_URL + "login";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fbid", strFbId);
            jsonObject.put("email", faceBookEmail);
            jsonObject.put("password", password);

            //callLoginAPI(loginURL,jsonObject);
            new APIRequest(LoginActivity.this, jsonObject, loginURL, Config.API_LOGIN, Config.POST);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void FacebookLoginClick(View v) {
        if (v == fbLogin) {
            if (isInternetPresent) {
                login_button.performClick();
            } else {
                Toast.makeText(LoginActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
            }

        }
    }

}




