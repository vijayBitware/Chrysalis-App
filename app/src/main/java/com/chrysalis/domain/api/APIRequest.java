package com.chrysalis.domain.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chrysalis.domain.AppPreference;
import com.chrysalis.domain.Config;
import com.chrysalis.model.ResponseCategoryList;
import com.chrysalis.model.charitylist.CharityResponse;
import com.chrysalis.model.deleteimage.DeleteImageResponse;
import com.chrysalis.model.editcostume.EditCostumeResult;
import com.chrysalis.model.forgetpass.ForgetPassword;
import com.chrysalis.model.signInData.LoginResponse;
import com.chrysalis.model.signupResponse.SignupResponse;
import com.chrysalis.model.update_costume.UpdateCostumeResponse;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by bitwarepc on 04-Jul-17.
 */

public class APIRequest extends AppCompatActivity {

    ProgressDialog progressDialog;
    BaseResponse baseResponse;
    private JSONObject mJsonObject;
    private String mUrl;
    // private ResponseHandler responseHandler;
    private int API_NAME;
    private Context mContext;

    public APIRequest(Context context, JSONObject jsonObject, String url, int api, String methodName) {
        //  this.responseHandler = responseHandler1;
        this.API_NAME = api;
        this.mUrl = url;
        this.mJsonObject = jsonObject;
        this.mContext = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (methodName.equals(Config.GET)) {
            apiGetRequest();
        } else {
            apiPostRequest();
        }
    }

    private void apiPostRequest() {
        String REQUEST_TAG = String.valueOf(API_NAME);
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(mUrl, mJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(" >>> Response for " + mUrl + "-- " + response);
                        setResponseToBody(response);
                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
            }
        });

        jsonObjectReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(mContext).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }

    private void apiGetRequest() {
        String REQUEST_TAG = String.valueOf(API_NAME);
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(mUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(">> response is " + response);
                        setResponseToBody(response);
                        progressDialog.hide();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
            }
        });

        jsonObjectReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(mContext).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }

    private void setResponseToBody(JSONObject response) {
        Gson gson = new Gson();
        switch (API_NAME) {
            case Config.API_SIGNUP:
                System.out.println(">> SignUp response :  " + response.toString());
                SignupResponse signupResrponse = gson.fromJson(response.toString(), SignupResponse.class);
                EventBus.getDefault().post(signupResrponse);
                break;

            case Config.API_LOGIN:
                LoginResponse loginResponse = gson.fromJson(response.toString(), LoginResponse.class);
                EventBus.getDefault().post(loginResponse);
                break;

            case Config.API_CATEGORY_LIST:
                AppPreference appPreference = AppPreference.getInstance(mContext);
                appPreference.setCategoryJson(response.toString());

                ResponseCategoryList responseCategoryList = gson.fromJson(response.toString(), ResponseCategoryList.class);
                EventBus.getDefault().post(responseCategoryList);
                break;

            case Config.API_EDIT_COSTUME:
                EditCostumeResult editCostumeResult = gson.fromJson(response.toString(), EditCostumeResult.class);
                EventBus.getDefault().post(editCostumeResult);
                break;

            case Config.API_CHARITY_LIST:
                CharityResponse charityResponse = gson.fromJson(response.toString(), CharityResponse.class);
                EventBus.getDefault().post(charityResponse);
                break;

            case Config.API_UPDATE_COSTUME:
                UpdateCostumeResponse updateCostume = gson.fromJson(response.toString(), UpdateCostumeResponse.class);
                EventBus.getDefault().post(updateCostume);
                break;

            case Config.API_DELETE_IMAGE:
                DeleteImageResponse deleteImage = gson.fromJson(response.toString(), DeleteImageResponse.class);
                EventBus.getDefault().post(deleteImage);
                break;

            case Config.API_FORGET_PASS:
                ForgetPassword forgetPassword = gson.fromJson(response.toString(), ForgetPassword.class);
                EventBus.getDefault().post(forgetPassword);
                break;

        }

    }

    public interface ResponseHandler {
        void onSuccess(BaseResponse response);

        void onFailure(BaseResponse response);

    }
}