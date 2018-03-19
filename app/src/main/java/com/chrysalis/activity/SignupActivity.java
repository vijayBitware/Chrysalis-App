package com.chrysalis.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chrysalis.R;
import com.chrysalis.domain.Config;
import com.chrysalis.domain.ConnectionDetector;
import com.chrysalis.domain.api.APIRequest;
import com.chrysalis.model.signupResponse.SignupResponse;
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

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chrysalis.domain.Config.BASE_URL;
import static com.chrysalis.domain.Config.EMAIL_REGEX;

/**
 * Created by Bitware Marketing on 23-05-2017.
 */

public class SignupActivity extends Activity {

    public int screenWidth, screenHeight;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.ivSignupBack)
    ImageView ivSignupBack;
    @BindView(R.id.btnSignp)
    Button btnSignp;
    String strFirstName = "", strLastName = "", strUserName = "", strEmail = "", strPassword = "";
    AccessTokenTracker accessTokenTracker;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LoginButton login_button;
    Button fbRegister;
    CallbackManager callbackManager;
    String faceBookEmail = "", strUserEMail = "";
    Dialog dialogService2;
    TextView tvOkEmailDialog, txtChkBox;
    EditText etUserMobileNo;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    CheckBox chkBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        inIt();

    }

    private void inIt() {
        sharedPreferences = this.getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        etPassword.setTransformationMethod(new PasswordTransformationMethod()); //For password hide/show
        login_button = findViewById(R.id.login_button);
        fbRegister = findViewById(R.id.fbRegister);
        chkBox = findViewById(R.id.chkBox);
        txtChkBox = findViewById(R.id.txtChkBox);

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String filtered = "";
                for (int i = start; i < end; i++) {
                    char character = source.charAt(i);
                    if (!Character.isWhitespace(character)) {
                        filtered += character;
                    }
                }

                return filtered;
            }

        };

        etEmail.setFilters(new InputFilter[]{filter});
        etUserName.setFilters(new InputFilter[]{filter});
        etPassword.setFilters(new InputFilter[]{filter});

        SpannableString ss = new SpannableString("I accept the Terms Of Use and Privacy Policy");
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // do some thing
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.chrysaliscostumes.com/pages/terms-of-use"));
                startActivity(browserIntent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.adobe_csdk_blue_color));
            }
        };

        ClickableSpan span2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // do another thing
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.chrysaliscostumes.com/pages/privacy-policy"));
                startActivity(browserIntent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.adobe_csdk_blue_color));
            }

        };

        ss.setSpan(span1, 13, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span2, 30, 44, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtChkBox.setText(ss);
        txtChkBox.setMovementMethod(LinkMovementMethod.getInstance());
        txtChkBox.setHighlightColor(Color.TRANSPARENT);

        if (isInternetPresent) {
            facebookSignup(); //Called after clickOn Faceebook Signup
        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
        }

    }

    private void facebookSignup() {
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
                                try {
                                    String strFbId = fbObject.getString("id");
                                    String fbName = fbObject.getString("name");
                                    String first_name = fbObject.getString("first_name");
                                    String lst_name = fbObject.getString("last_name");

                                    if (fbObject.has("email")) {
                                        if (!fbObject.getString("email").equals("") || !fbObject.getString("email").equals("null")) {
                                            faceBookEmail = fbObject.getString("email");
                                            callFacebookSignUpAPI(strFbId, fbName, first_name, lst_name, faceBookEmail);
                                        }
                                    } else {
                                        askEmailDialogFromUser(strFbId, fbName, first_name, lst_name);
                                    }

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

            }

            @Override
            public void onError(FacebookException error) {

            }

        });

    }

    private void askEmailDialogFromUser(final String strFbId, final String fbName, final String first_name, final String lst_name) {

        dialogService2 = new Dialog(SignupActivity.this);
        dialogService2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogService2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogService2.setContentView(R.layout.ask_mobile_from_user);
        dialogService2.setCanceledOnTouchOutside(false);

        //tvCancelPhoneDialog = (TextView) dialogService2.findViewById(R.id.tvCancelPhoneDialog);
        tvOkEmailDialog = dialogService2.findViewById(R.id.tvOkEmailDialog);
        etUserMobileNo = dialogService2.findViewById(R.id.etUserEmailId);
        tvOkEmailDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strUserEMail = etUserMobileNo.getText().toString();
                if (!strUserEMail.equals("")) {
                    if (strUserEMail.matches(EMAIL_REGEX)) {
                        dialogService2.dismiss();
                        String signupURL = BASE_URL + "registration";
                        JSONObject jsonObject = new JSONObject();
                        try {
                            System.out.println(">>> FB DATA at Login Email not exist :");
                            System.out.println(">>>fbid  :" + strFbId);
                            System.out.println(">>> first_name :" + first_name);
                            System.out.println(">>> lst_name :" + lst_name);
                            System.out.println(">>> fbName :" + fbName);
                            System.out.println(">>> strPassword :" + strPassword);
                            System.out.println(">>> strUserEMail :" + strUserEMail);

                            jsonObject.put("fbid", strFbId);
                            jsonObject.put("first_name", first_name);
                            jsonObject.put("last_name", lst_name);
                            jsonObject.put("username", fbName);
                            jsonObject.put("password", strPassword);
                            jsonObject.put("email", strUserEMail);

                            if (isInternetPresent) {
                                callSignupAPI(signupURL, jsonObject);
                            } else {
                                Toast.makeText(SignupActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        etUserMobileNo.requestFocus();
                        Toast.makeText(SignupActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    etUserMobileNo.requestFocus();
                    Toast.makeText(SignupActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        WindowManager.LayoutParams wmlp = dialogService2.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_HORIZONTAL;
        dialogService2.show();
        dialogService2.getWindow().setLayout((int) ((screenWidth / 5) * 4.2), (screenHeight / 15) * 5);
    }

    private void callSignupAPI(String signupURL, JSONObject jsonObject) {
        if (isInternetPresent) {
            System.out.println(">>> SignUp Params : " + signupURL + " -- " + jsonObject);
            new APIRequest(SignupActivity.this, jsonObject, signupURL, Config.API_SIGNUP, Config.POST);
        } else {
            Toast.makeText(SignupActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
        }
    }

    private void callFacebookSignUpAPI(String strFbId, String fbName, String first_name, String lst_name, String faceBookEmail) {
        String signupURL = BASE_URL + "registration";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fbid", strFbId);
            jsonObject.put("first_name", first_name);
            jsonObject.put("last_name", lst_name);
            jsonObject.put("username", fbName);
            jsonObject.put("password", strPassword);
            jsonObject.put("email", faceBookEmail);

            if (isInternetPresent) {
                callSignupAPI(signupURL, jsonObject);
            } else {
                Toast.makeText(SignupActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void FacebookSignupClick(View v) {
        if (v == fbRegister) {
            if (isInternetPresent) {
                login_button.performClick();
            } else {
                Toast.makeText(SignupActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @OnClick(R.id.btnSignp)
    public void onClickSignup() {
        // strFirstName = "",strLastName = "",strUserName = "",strEmail = "",strPassword = "";
        strFirstName = etFirstName.getText().toString().trim();
        strLastName = etLastName.getText().toString().trim();
        strEmail = etEmail.getText().toString().trim();
        strPassword = etPassword.getText().toString().trim();
        strUserName = etUserName.getText().toString().trim();

        if (!strFirstName.equals("")) {
            boolean boolSpecialChar = Config.isContainSpecialChar(strFirstName);
            boolean boolSpace = strFirstName.contains(" ");
            if (boolSpecialChar == false && boolSpace == false) {
                if (!strLastName.equals("")) {
                    boolean boolCharLastName = Config.isContainSpecialChar(strLastName);
                    boolean boolSpaceLastName = strLastName.contains(" ");
                    if (boolCharLastName == false && boolSpaceLastName == false) {
                        if (!strUserName.equals("")) {
                            if (!strEmail.equals("")) {
                                if (strEmail.matches(EMAIL_REGEX)) {
                                    if (!strPassword.equals("")) {
                                        if (strPassword.length() >= 6) {
                                            if (chkBox.isChecked()) {
                                                String signupURL = BASE_URL + "registration";
                                                JSONObject jsonObject = new JSONObject();
                                                try {
                                                    jsonObject.put("first_name", strFirstName);
                                                    jsonObject.put("last_name", strLastName);
                                                    jsonObject.put("username", strUserName);
                                                    jsonObject.put("password", strPassword);
                                                    jsonObject.put("email", strEmail);

                                                    if (isInternetPresent) {
                                                        callSignupAPI(signupURL, jsonObject);
                                                    } else {
                                                        Toast.makeText(SignupActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                Toast.makeText(SignupActivity.this, "Please accept terms & privacy policy", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            etPassword.requestFocus();
                                            etPassword.setError("Password should be minimum 6 characters");
                                        }
                                    } else {
                                        etPassword.requestFocus();
                                        etPassword.setError("Please enter password");
                                    }
                                } else {
                                    etEmail.requestFocus();
                                    etEmail.setError("Please enter valid email");
                                }
                            } else {
                                etEmail.requestFocus();
                                etEmail.setError("Please enter email");
                            }
                        } else {
                            etUserName.requestFocus();
                            etUserName.setError("Please enter username");
                        }
                    } else {
                        etLastName.requestFocus();
                        etLastName.setError("Special characters & space are not allowed.");
                    }

                } else {
                    etLastName.requestFocus();
                    etLastName.setError("Please enter last name");
                }
            } else {
                etFirstName.requestFocus();
                etFirstName.setError("Special characters & space are not allowed");
            }
        } else {
            etFirstName.requestFocus();
            etFirstName.setError("Please enter first name");
        }
    }

    @Subscribe
    public void getSignupData(SignupResponse signupResponse) {
        System.out.println(">> SignUp Data in response : " + signupResponse);
        if (signupResponse.getResult().equalsIgnoreCase("success")) {
            Toast.makeText(SignupActivity.this, signupResponse.getMessage(), Toast.LENGTH_SHORT).show();

            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(SignupActivity.this, signupResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }





/*    private void callSignupAPI(String signupURL, JSONObject jsonObject) {
        progressDialog = new ProgressDialog(this);
        //  String  REQUEST_TAG = "com.example.bitwarepc.vollydemo.volleyStringRequest";
        progressDialog.setMessage("In Progress..");
        progressDialog.show();

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(signupURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(">>> SignUp Response :"+response.toString());
                        progressDialog.hide();

                         Gson gson = new Gson(); // Or use new GsonBuilder().create();
                         SignupResponse signupResponse = gson.fromJson(response.toString(),SignupResponse.class);

                        if(signupResponse.getResult().equalsIgnoreCase("success")){
                            Toast.makeText(SignupActivity.this, signupResponse.getMessage(), Toast.LENGTH_SHORT).show();

                            editor.putString("isLogin","Yes");
                            editor.putString("userId",signupResponse.getData().get(0).getUser().getId());
                            editor.putString("email",signupResponse.getData().get(0).getUser().getEmail());
                            editor.putString("firstName",signupResponse.getData().get(0).getUser().getFirstName());
                            editor.putString("lastName",signupResponse.getData().get(0).getUser().getLastName());
                            editor.putString("displayName",signupResponse.getData().get(0).getUser().getLastName());
                            editor.commit();

                            Intent i = new Intent(SignupActivity.this,ProductListingScreen.class);
                            startActivity(i);
                            finish();
                        }else{
                            progressDialog.hide();
                            Toast.makeText(SignupActivity.this, signupResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                System.out.println(">>> SignUp Error :"+error);
            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq);
    }*/

    @OnClick(R.id.ivSignupBack)
    public void BackClick() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
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

}
