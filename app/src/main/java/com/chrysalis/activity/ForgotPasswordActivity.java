package com.chrysalis.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chrysalis.R;
import com.chrysalis.domain.Config;
import com.chrysalis.domain.ConnectionDetector;
import com.chrysalis.domain.api.APIRequest;
import com.chrysalis.model.forgetpass.ForgetPassword;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import static com.chrysalis.domain.Config.BASE_URL;
import static com.chrysalis.domain.Config.EMAIL_REGEX;

/**
 * Created by Bitware Marketing on 23-05-2017.
 */

public class ForgotPasswordActivity extends Activity {
    ImageView ivForgotPassBack;
    Button btnForgetPass;
    EditText etForgetEmail;
    String strForgetEmail = "";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        inIt();
    }

    private void inIt() {
        ivForgotPassBack = findViewById(R.id.ivForgotPassBack);
        btnForgetPass = findViewById(R.id.btnForgetPass);
        etForgetEmail = findViewById(R.id.etForgetEmail);

        sharedPreferences = this.getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        btnForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strForgetEmail = etForgetEmail.getText().toString().trim();

                if (!strForgetEmail.equals("")) {
                    if (strForgetEmail.matches(EMAIL_REGEX)) {
                        if (isInternetPresent) {
                            String forgetPassURL = BASE_URL + "forgot_password";
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("email", strForgetEmail);
                                callForgotPassAPI(forgetPassURL, jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        etForgetEmail.requestFocus();
                        etForgetEmail.setError("Please enter valid email");
                    }
                } else {
                    etForgetEmail.requestFocus();
                    etForgetEmail.setError("Please enter valid email");
                }
            }
        });
        ivForgotPassBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void callForgotPassAPI(String forgetPassURL, JSONObject jsonObject) {
        if (isInternetPresent) {
            System.out.println(">>> ForgetPass Params : " + forgetPassURL + " -- " + jsonObject);
            new APIRequest(ForgotPasswordActivity.this, jsonObject, forgetPassURL, Config.API_FORGET_PASS, Config.POST);
        } else {
            Toast.makeText(ForgotPasswordActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void getForgetPassData(ForgetPassword forgetPassword) {
        if (forgetPassword.getResult().equalsIgnoreCase("success")) {
            Toast.makeText(ForgotPasswordActivity.this, forgetPassword.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ForgotPasswordActivity.this, forgetPassword.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
