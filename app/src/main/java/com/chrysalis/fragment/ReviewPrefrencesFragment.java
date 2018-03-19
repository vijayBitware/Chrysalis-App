package com.chrysalis.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.chrysalis.R;
import com.chrysalis.activity.ConfirmationActivity;
import com.chrysalis.activity.ProductListingScreen;
import com.chrysalis.adapter.AdapterPricingShipping;
import com.chrysalis.domain.AndroidMultiPartEntity;
import com.chrysalis.domain.AppPreference;
import com.chrysalis.domain.Config;
import com.chrysalis.domain.ConnectionDetector;
import com.chrysalis.domain.RealmController;
import com.chrysalis.domain.Utility;
import com.chrysalis.domain.api.APIRequest;
import com.chrysalis.model.ModelAddImages;
import com.chrysalis.model.PricingShippingModel;
import com.chrysalis.model.charitylist.CharityResponse;
import com.chrysalis.model.charitylist.Datum;
import com.chrysalis.model.editcostume.EditCostumeResult;
import com.chrysalis.model.update_costume.UpdateCostumeResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.client.ClientProtocolException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by samsung on 28/7/17.
 */

public class ReviewPrefrencesFragment extends BaseFragment {
    @BindView(R.id.btnFinishedReview)
    Button btnFinishedReview;
    @BindView(R.id.ivBackReview)
    ImageView ivBackReview;
    @BindView(R.id.ivCharOne)
    ImageView ivCharOne;
    @BindView(R.id.ivCharTwo)
    ImageView ivCharTwo;
    @BindView(R.id.ivCharThree)
    ImageView ivCharThree;
    @BindView(R.id.ivCharFour)
    ImageView ivCharFour;
    @BindView(R.id.ivCharFive)
    ImageView ivCharFive;
    @BindView(R.id.ivCharSix)
    ImageView ivCharSix;
    /*@BindView(R.id.etItemLocation)
    EditText etItemLocation;*/
    @BindView(R.id.tvHandlingTime)
    TextView tvHandlingTime;
    @BindView(R.id.tvReturnAccepted)
    TextView tvReturnAccepted;
    @BindView(R.id.tvNoReturn)
    TextView tvNoReturn;
    @BindView(R.id.tvMinus)
    TextView tvMinus;
    @BindView(R.id.tvPercentageVal)
    TextView tvPercentageVal;
    @BindView(R.id.tvPlus)
    TextView tvPlus;
    @BindView(R.id.etSuggestedCharityOrg)
    EditText etSuggestedCharityOrg;
    @BindView(R.id.tvDonatoinAmt)
    TextView tvDonatoinAmt;

    @BindView(R.id.llHandlingtime)
    LinearLayout llHandlingtime;

    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int screenWidth, screenHeight;
    AQuery aQuery;
    ProgressDialog p;
    Dialog dialogService2;
    ImageView ivClose;
    TextView tvDialogCostumeHeader;
    ListView lvMainCategory;
    ProgressDialog progressDialog;
    AppPreference appPreference;
    List<Datum> arrReviewImages;
    String strReturnPolicy = "", strCharityImgIdR = "", strDonationAmount = "", strHandlingTime = "", strSuggestedCharity = "";
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.activity_review_pref, container, false);
            ButterKnife.bind(this, view);
            inIt();

            String strUploadedImages = appPreference.getPhotoModel();
            Gson gson = new Gson();
            Type type = new TypeToken<List<ModelAddImages>>() {
            }.getType();
            List<ModelAddImages> images = gson.fromJson(strUploadedImages, type);
            if (images != null) {
                if (images.size() > 0) {
                    System.out.println(">>> Received images arr size from JSON : " + images.size());
                    System.out.println("---------------------------");
                    for (int i = 0; i < images.size(); i++) {
                        System.out.println(images.get(i).getImgName() + "  -->  " + images.get(i).getImgPath());
                    }
                }
            }

        }

        return view;
    }

    private void inIt() {
        screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        sharedPreferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);

        editor = sharedPreferences.edit();
        aQuery = new AQuery(getActivity());
        appPreference = new AppPreference(getActivity());
        strCharityImgIdR = "";
        appPreference.setCharityId(strCharityImgIdR);
        if (Utility.isNetworkAvailable(getActivity())) {
            try {
                JSONObject jsonObject = new JSONObject();
                String CategoryURL = Config.BASE_URL + "charities";

                new APIRequest(getActivity(), jsonObject, CategoryURL, Config.API_CHARITY_LIST, Config.POST);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setEditView() {
        //EditCostumeResult editCostumeResult = RealmController.with(getActivity()).getEditResponse(appPreference.getCostumeId());

        EditCostumeResult editCostumeResult = RealmController.with(getActivity()).getEditResponse(appPreference.getCostumeId());
        tvHandlingTime.setText(editCostumeResult.getData().getCostumedescScreen().getHandlingTime());
        tvDonatoinAmt.setText("$ " + editCostumeResult.getData().getCostumedescScreen().getDonationAmount());

        // etSuggestedCharityOrg.setText(editCostumeResult.getData().getCostumedescScreen().getC);
        String strReturnPolicy = editCostumeResult.getData().getCostumedescScreen().getReturn_policy();
        ////////////////////////////////////////////
        // strReturnPolicy = "Return Accepted";
        System.out.println("**************" + strReturnPolicy);
        if (strReturnPolicy.equalsIgnoreCase("Returns Accepted")) {
            onClickTvReturnAccepted();
        } else {
            onClickTvReturnNotAccepted();
        }
        strCharityImgIdR = editCostumeResult.getData().getCostumedescScreen().getCharityId();
        System.out.println("************" + strCharityImgIdR);
        if (strCharityImgIdR.equalsIgnoreCase("6")) {
            //onClickfirstImg();
            strCharityImgIdR = arrReviewImages.get(0).getId();
            appPreference.setCharityId(strCharityImgIdR);
            ivCharOne.setBackground(getResources().getDrawable(R.drawable.orgframe));

            ivCharTwo.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharThree.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharFour.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharFive.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharSix.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));

        } else if (strCharityImgIdR.equalsIgnoreCase("7")) {
            //onClickSecondmg();
            strCharityImgIdR = arrReviewImages.get(1).getId();
            appPreference.setCharityId(strCharityImgIdR);
            ivCharTwo.setBackground(getResources().getDrawable(R.drawable.orgframe));

            ivCharOne.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharThree.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharFour.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharFive.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharSix.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        } else if (strCharityImgIdR.equalsIgnoreCase("10")) {
            //onClickthirdImg();
            strCharityImgIdR = arrReviewImages.get(2).getId();
            appPreference.setCharityId(strCharityImgIdR);
            ivCharThree.setBackground(getResources().getDrawable(R.drawable.orgframe));

            ivCharOne.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharTwo.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharFour.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharFive.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharSix.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        } else if (strCharityImgIdR.equalsIgnoreCase("11")) {
            //onClickFour();
            strCharityImgIdR = arrReviewImages.get(3).getId();
            appPreference.setCharityId(strCharityImgIdR);
            ivCharFour.setBackground(getResources().getDrawable(R.drawable.orgframe));

            ivCharOne.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharTwo.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharThree.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharFive.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharSix.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        } else if (strCharityImgIdR.equalsIgnoreCase("20")) {
            //onClickFive();
            strCharityImgIdR = arrReviewImages.get(4).getId();
            appPreference.setCharityId(strCharityImgIdR);
            ivCharFive.setBackground(getResources().getDrawable(R.drawable.orgframe));

            ivCharOne.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharTwo.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharThree.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharFour.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharSix.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        } else if (strCharityImgIdR.equalsIgnoreCase("21")) {
            //onClickSix();
            strCharityImgIdR = arrReviewImages.get(5).getId();
            appPreference.setCharityId(strCharityImgIdR);
            ivCharSix.setBackground(getResources().getDrawable(R.drawable.orgframe));

            ivCharOne.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharTwo.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharThree.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharFour.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharFive.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        } else {
            //onClickfirstImg();
           /* strCharityImgIdR = arrReviewImages.get(0).getId();
            appPreference.setCharityId(strCharityImgIdR);
            ivCharOne.setBackground(getResources().getDrawable(R.drawable.orgframe));

            ivCharTwo.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharThree.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharFour.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharFive.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
            ivCharSix.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));*/
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void getUpdateCostumeResult(UpdateCostumeResponse updateCostumeResponse) {
        UpdateCostumeResponse mUpdateCostumeResponse = updateCostumeResponse;
        if (mUpdateCostumeResponse.getResult().equalsIgnoreCase("success")) {
            Toast.makeText(getActivity(), mUpdateCostumeResponse.getMessage(), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity(), ProductListingScreen.class);
            startActivity(i);
        } else {
            Toast.makeText(getActivity(), mUpdateCostumeResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void getCharityResponse(CharityResponse charityResponse) {

        CharityResponse mCharityResponse = charityResponse;
        if (mCharityResponse.getResult().equalsIgnoreCase("success")) {
            arrReviewImages = mCharityResponse.getData();
            Glide.with(getActivity()).load(arrReviewImages.get(0).getImage()).into(ivCharOne);
            Glide.with(getActivity()).load(arrReviewImages.get(1).getImage()).into(ivCharTwo);
            Glide.with(getActivity()).load(arrReviewImages.get(2).getImage()).into(ivCharThree);
            Glide.with(getActivity()).load(arrReviewImages.get(3).getImage()).into(ivCharFour);
            Glide.with(getActivity()).load(arrReviewImages.get(4).getImage()).into(ivCharFive);
            Glide.with(getActivity()).load(arrReviewImages.get(5).getImage()).into(ivCharSix);

            System.out.println("## getMode() at Review  : " + Config.MODE);
            if (Config.MODE.equals(Config.EDIT_MODE)) {
                setEditView();
            } else {
                onClickTvReturnNotAccepted();
            }

        } else {
            Toast.makeText(getActivity(), "Somthing went erog, please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // ((AddEditCostumeActivity)getActivity()).getSupportActionBar().setTitle("Review");
    }

    @OnClick(R.id.btnFinishedReview)
    public void onFinishButtonClick() {
        sharedPreferences.edit().putString("KEY", "empty").commit();
        strDonationAmount = tvDonatoinAmt.getText().toString().replace("$", "").trim();
        appPreference.setDonationAmt(strDonationAmount);
        strHandlingTime = tvHandlingTime.getText().toString().trim();
        appPreference.setHandalingTime(strHandlingTime);
        strSuggestedCharity = etSuggestedCharityOrg.getText().toString().trim();
        appPreference.setSuggestedChrity(strSuggestedCharity);

        System.out.println("## getMode() at Review : " + Config.MODE);

        if (!strHandlingTime.equals("") && !strHandlingTime.equalsIgnoreCase("Select Time")) {
            if (!strReturnPolicy.equals("")) {
                if (!strDonationAmount.equals("")) {
                    //if(!strCharityImgIdR.equals("")){
                    if (Config.MODE.equalsIgnoreCase(Config.ADD_MODE)) {

                        System.out.println(">> costume_name :" + appPreference.getCostumeName());
                        System.out.println(">> category :" + appPreference.getCategoryName());
                        System.out.println(">> category ID:" + appPreference.getCategoryId());
                        System.out.println(">> gender :" + appPreference.getGender());
                        System.out.println(">> size :" + appPreference.getSize());
                        System.out.println(">> theme :" + appPreference.getThemeName() + " id " + appPreference.getThemeId());

                        System.out.println(">> condition :" + appPreference.getCondition());
                        System.out.println(">> keywords :" + appPreference.getKeyword());
                        System.out.println(">> make_costume :" + appPreference.getMakeCostume());
                        System.out.println(">> costume_time :" + appPreference.getCostumeTime());
                        System.out.println(">> costume_desc :" + appPreference.getCostumeDescription());

                        System.out.println(">> faq :" + appPreference.getFaq());
                        System.out.println(">> user_id :" + appPreference.getUserId());
                        System.out.println(">>> price " + appPreference.getShippingPrice());
                        System.out.println(">>> shipping_option " + appPreference.getShippingOptions());
                        System.out.println(">>> length " + appPreference.getLenght());

                        System.out.println(">>> width " + appPreference.getWidth());
                        System.out.println(">>> height " + appPreference.getHeight());
                        System.out.println(">> weight_ounces :" + appPreference.getOunces());
                        System.out.println(">> weight_pounds :" + appPreference.getPounds());
                        System.out.println(">> return_policy :" + appPreference.getReturnPolicy());

                        System.out.println(">> charity_id :" + appPreference.getCharityId());
                        System.out.println(">> handling_time :" + appPreference.getHandalingTime());
                        System.out.println(">> donation_amount :" + appPreference.getDonationAmt());

                        if (Utility.isNetworkAvailable(getActivity())) {
                            new createCostumeAPI().execute("");
                        } else {
                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        if (Utility.isNetworkAvailable(getActivity())) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                String updateCostumeURL = Config.BASE_URL + "updateCostumeDetails";

                                jsonObject.put("wash_type", appPreference.getCostumeWashType());
                                jsonObject.put("production_name", appPreference.getProductionName());
                                jsonObject.put("costume_id", appPreference.getCostumeId());
                                jsonObject.put("costume_name", appPreference.getCostumeName());
                                jsonObject.put("category", appPreference.getCategoryId());
                                jsonObject.put("gender", appPreference.getGender());
                                jsonObject.put("size", appPreference.getSize());

                                jsonObject.put("theme", appPreference.getThemeId());
                                jsonObject.put("condition", appPreference.getCondition());
                                jsonObject.put("keywords", appPreference.getKeyword());
                                jsonObject.put("make_costume", appPreference.getMakeCostume());
                                jsonObject.put("costume_time", appPreference.getCostumeTime());

                                jsonObject.put("costume_desc", appPreference.getCostumeDescription());
                                jsonObject.put("faq", appPreference.getFaq());
                                jsonObject.put("price", appPreference.getShippingPrice());
                                jsonObject.put("shipping_option", appPreference.getShippingOptions());
                                jsonObject.put("length-in", appPreference.getLenght());

                                jsonObject.put("width-in", appPreference.getWidth());
                                jsonObject.put("height-in", appPreference.getHeight());
                                jsonObject.put("weight_pounds", appPreference.getPounds());
                                jsonObject.put("weight_ounces", appPreference.getOunces());
                                jsonObject.put("donation_amount", appPreference.getDonationAmt());

                                jsonObject.put("charity_id", appPreference.getCharityId());
                                jsonObject.put("handling_time", appPreference.getHandalingTime());
                                jsonObject.put("return_policy", appPreference.getReturnPolicy());
                                jsonObject.put("fun_fact", appPreference.getFunFact());
                                jsonObject.put("height-ft", appPreference.getBDHeightfoot());

                                jsonObject.put("height-in-body", appPreference.getBDHeightIn());
                                jsonObject.put("weight-lbs", appPreference.getWeghtLbs());
                                jsonObject.put("chest-in", appPreference.getChestIn());
                                jsonObject.put("waist-lbs", appPreference.getWaistLbs());
                                jsonObject.put("quantity", appPreference.getQuantity()); //########
                                jsonObject.put("fimquality", appPreference.getQuestomQuality());

                                jsonObject.put("suggested_organization", appPreference.getSuggestedChrity()); //########
                                jsonObject.put("user_id", appPreference.getUserId());

                                System.out.println(">>> Update URL :" + updateCostumeURL);
                                System.out.println(">>> Update PARAMS :" + jsonObject);

                                new APIRequest(getActivity(), jsonObject, updateCostumeURL, Config.API_UPDATE_COSTUME, Config.POST);

                            } catch (Exception e) {
                                System.out.println("*****************exception**********" + e);
                                e.printStackTrace();
                            }

                        } else {

                        }

                    }

                    /*}else{
                        Toast.makeText(getActivity(),"Please select charity organization.",Toast.LENGTH_SHORT).show();
                    }*/

                } else {
                    Toast.makeText(getActivity(), "Enter donation amount", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Select return policy", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getActivity(), "Select handling time", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.ivBackReview)
    public void backClick() {
        getFragmentManager().popBackStackImmediate();
    }

    @OnClick(R.id.tvReturnAccepted)
    public void onClickTvReturnAccepted() {
        strReturnPolicy = "Returns Accepted";
        appPreference.setReturnPolicy(strReturnPolicy);
        tvReturnAccepted.setTextColor(getResources().getColor(R.color.white));
        tvReturnAccepted.setBackgroundColor(getResources().getColor(R.color.green));

        tvNoReturn.setBackgroundColor(getResources().getColor(R.color.white));
        tvNoReturn.setTextColor(getResources().getColor(R.color.green));

    }

    @OnClick(R.id.llHandlingtime)
    public void handlingTime() {
        showDilogForHandlingTime("Handling Time", tvHandlingTime, PricingShippingFragment.arrFourNew);
    }
   /* @OnClick()
    public void onHandlingTimeClick(){
        showDilogForHandlingTime("Handling Time",tvHandlingTime, PricingShippingFragment.arrFourNew);
    }*/

    @OnClick(R.id.tvNoReturn)
    public void onClickTvReturnNotAccepted() {
        strReturnPolicy = "Returns Not Accepted";
        appPreference.setReturnPolicy(strReturnPolicy);
        tvNoReturn.setTextColor(getResources().getColor(R.color.white));
        tvNoReturn.setBackgroundColor(getResources().getColor(R.color.green));

        tvReturnAccepted.setBackgroundColor(getResources().getColor(R.color.white));
        tvReturnAccepted.setTextColor(getResources().getColor(R.color.green));
    }

  /*  @OnClick(R.id.tvHandlingTime)
    public void onClicktvHandlingTime(){
        showDilogForHandlingTime("Handling Time",tvHandlingTime, PricingShippingFragment.arrFourNew);
    }*/

    @OnClick(R.id.tvMinus)
    public void onMinusClick() {
        int percentage = Integer.parseInt(tvPercentageVal.getText().toString().trim());

        if (percentage != 0) {
            int res = percentage - 1;
            tvPercentageVal.setText(String.valueOf(res));

            Double actPrice = Double.parseDouble(appPreference.getShippingPrice());
            double val = getFormatedval(actPrice * (res / 100.0f));
            tvDonatoinAmt.setText("$ " + String.valueOf(val));
        }
    }

    @OnClick(R.id.tvPlus)
    public void onPlusClick() {
        int percentage = Integer.parseInt(tvPercentageVal.getText().toString().trim());
        int res = percentage + 1;
        tvPercentageVal.setText(String.valueOf(res));

        Double actPrice = Double.parseDouble(appPreference.getShippingPrice());
        double val = getFormatedval(actPrice * (res / 100.0f));
        tvDonatoinAmt.setText("$ " + String.valueOf(val));
    }

    @OnClick(R.id.ivCharOne)
    public void onClickfirstImg() {
        strCharityImgIdR = arrReviewImages.get(0).getId();
        appPreference.setCharityId(strCharityImgIdR);
        ivCharOne.setBackground(getResources().getDrawable(R.drawable.orgframe));

        ivCharTwo.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharThree.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharFour.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharFive.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharSix.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));

    }

    @OnClick(R.id.ivCharTwo)
    public void onClickSecondmg() {
        strCharityImgIdR = arrReviewImages.get(1).getId();
        appPreference.setCharityId(strCharityImgIdR);
        ivCharTwo.setBackground(getResources().getDrawable(R.drawable.orgframe));

        ivCharOne.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharThree.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharFour.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharFive.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharSix.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
    }

    @OnClick(R.id.ivCharThree)
    public void onClickthirdImg() {
        strCharityImgIdR = arrReviewImages.get(2).getId();
        appPreference.setCharityId(strCharityImgIdR);
        ivCharThree.setBackground(getResources().getDrawable(R.drawable.orgframe));

        ivCharOne.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharTwo.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharFour.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharFive.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharSix.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
    }

    @OnClick(R.id.ivCharFour)
    public void onClickFour() {
        strCharityImgIdR = arrReviewImages.get(3).getId();
        appPreference.setCharityId(strCharityImgIdR);
        ivCharFour.setBackground(getResources().getDrawable(R.drawable.orgframe));

        ivCharOne.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharTwo.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharThree.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharFive.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharSix.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
    }

    @OnClick(R.id.ivCharFive)
    public void onClickFive() {
        strCharityImgIdR = arrReviewImages.get(4).getId();
        appPreference.setCharityId(strCharityImgIdR);
        ivCharFive.setBackground(getResources().getDrawable(R.drawable.orgframe));

        ivCharOne.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharTwo.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharThree.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharFour.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharSix.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
    }

    @OnClick(R.id.ivCharSix)
    public void onClickSix() {
        strCharityImgIdR = arrReviewImages.get(5).getId();
        appPreference.setCharityId(strCharityImgIdR);
        ivCharSix.setBackground(getResources().getDrawable(R.drawable.orgframe));

        ivCharOne.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharTwo.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharThree.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharFour.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
        ivCharFive.setBackground(getResources().getDrawable(R.drawable.orgemptyframe));
    }

    private void showDilogForHandlingTime(String strHandlingTime, final TextView tvHandlingTime, final ArrayList<PricingShippingModel> arrFourNew) {
        dialogService2 = new Dialog(getActivity());
        dialogService2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogService2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogService2.setContentView(R.layout.dialog_cat_list);
        dialogService2.setCanceledOnTouchOutside(false);

        ivClose = dialogService2.findViewById(R.id.ivCloseNew);
        tvDialogCostumeHeader = dialogService2.findViewById(R.id.tvDialogCostumeHeader);
        lvMainCategory = dialogService2.findViewById(R.id.lvMainCategory);
        tvDialogCostumeHeader.setText(strHandlingTime);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogService2.dismiss();
            }
        });
        AdapterPricingShipping adapterPricingShipping = new AdapterPricingShipping(getActivity(), R.layout.row_costume_category_dialog, arrFourNew);
        lvMainCategory.setAdapter(adapterPricingShipping);

        lvMainCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvHandlingTime.setText(String.valueOf(arrFourNew.get(position).getVal()));
                dialogService2.dismiss();

            }
        });

        WindowManager.LayoutParams wmlp = dialogService2.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_HORIZONTAL;
        dialogService2.show();
        dialogService2.getWindow().setLayout((int) ((screenWidth / 5.5) * 4.2), (screenHeight / 12) * 5);
    }

    private void clearPrefrences() {
        editor.remove("photoModel");
        editor.remove("costume_name");
        editor.remove("category");
        editor.remove("gender");
        editor.remove("size");
        editor.remove("theme");
        editor.remove("keywords");
        editor.remove("myCostume_time");
        editor.remove("faq");
        editor.remove("price");
        editor.remove("donation_amount");
        editor.remove("charity_id");
        editor.remove("shipping_option");
        editor.remove("length");
        editor.remove("width");
        editor.remove("height");
        editor.remove("handling_time");
        editor.remove("return_policy");
        editor.remove("strOunces");
        editor.remove("strPounds");
        editor.apply();
    }

    private double getFormatedval(double currentLat) {
        double doubleVal = 0.0;
        String last2Chars;
        if (String.valueOf(currentLat).contains(".")) {
            String[] arr = String.valueOf(currentLat).split("\\.");
            long[] intArr = new long[2];
            intArr[0] = Long.parseLong(arr[0]); // 1
            intArr[1] = Long.parseLong(arr[1]); //
            String mainDigits = String.valueOf(intArr[0]);
            String strLenght = String.valueOf(intArr[1]);
            if (strLenght.length() > 2) {
                last2Chars = strLenght.substring(0, 2);
            } else {
                last2Chars = strLenght;
            }
            String strFinal = mainDigits + "." + last2Chars;
            System.out.println(">>>> Final output is  strFinal --- :" + strFinal);
            doubleVal = Double.parseDouble(strFinal);
            System.out.println(">>>> Double return val is --- :" + doubleVal);
        } else {
            doubleVal = currentLat;
        }
        return doubleVal;
    }

    private List<ModelAddImages> checkIsImagesPresentInGSON() {
        String strSavedImages = appPreference.getPhotoModel();
        Gson gson = new Gson();
        Type type = new TypeToken<List<ModelAddImages>>() {
        }.getType();
        List<ModelAddImages> arrSavedList = gson.fromJson(strSavedImages, type);
        return arrSavedList;
    }

    class createCostumeAPI extends AsyncTask<String, Void, String> {
        ProgressDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(getActivity());
            p.setMessage("In Progress..");
            p.setCanceledOnTouchOutside(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseString = null;
            org.apache.http.client.HttpClient httpclient = new org.apache.http.impl.client.DefaultHttpClient();
            org.apache.http.client.methods.HttpPost httppost = new org.apache.http.client.methods.HttpPost(Config.BASE_URL + "add_costume_data");
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                //publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                // Adding file data to http body
                // Extra parameters if you want to pass to server

                List<ModelAddImages> arrSavedImages = checkIsImagesPresentInGSON(); // Check for is aleady images peresent in GSON
                String frontPath = "", backpath = "";

                for (int i = 0; i < arrSavedImages.size(); i++) {
                    System.out.println(">> name " + arrSavedImages.get(i).getImgName() + "-->" + arrSavedImages.get(i).getImgPath());
                }

                for (int i = 0; i < arrSavedImages.size(); i++) {
                    String imgName = arrSavedImages.get(i).getImgName();
                    if (imgName.equalsIgnoreCase("Front")) {
                        frontPath = arrSavedImages.get(i).getImgPath();
                        appPreference.setFront(frontPath);
                        // arrSavedImages.remove(i);
                    }
                    if (imgName.equalsIgnoreCase("Back")) {
                        backpath = arrSavedImages.get(i).getImgPath();
                        appPreference.setBack(backpath);
                        //arrSavedImages.remove(i);
                    }
                }

                System.out.println(">>After removel arr size : " + arrSavedImages.size());

                System.out.println(">> frontPath :" + appPreference.getFront().toString().trim());
                File sourceFile1 = new File(appPreference.getFront().toString().trim());
                entity.addPart("front", new FileBody(sourceFile1));
                entity.addPart("type1", new StringBody("1"));

                System.out.println(">> BackPath :" + appPreference.getBack().toString().trim());
                File sourceFile2 = new File(appPreference.getBack().toString().trim());
                entity.addPart("back", new FileBody(sourceFile2));
                entity.addPart("type2", new StringBody("2"));

               /* for(int i = 0; i < arrSavedImages.size(); i++){
                    String imgName = arrSavedImages.get(i).getImgName();
                    if(imgName.equalsIgnoreCase("Front")){
                        arrSavedImages.remove(i);
                    }
                    if(imgName.equalsIgnoreCase("Back")){
                        arrSavedImages.remove(i);
                    }
                }*/

                for (int i = 0; i < arrSavedImages.size(); i++) {
                    String imgName = arrSavedImages.get(i).getImgName().toString().trim();

                    if (!imgName.equalsIgnoreCase("Front") && !imgName.equalsIgnoreCase("Back")) {
                        String testPath = arrSavedImages.get(i).getImgPath().toString().trim();
                        System.out.println(">>> Img Path in loop is :" + testPath);
                        File sourceFileObj = new File(testPath);
                        entity.addPart("pic" + i, new FileBody(sourceFileObj));
                    }

                }

                entity.addPart("type3", new StringBody("3"));
                entity.addPart("wash_type", new StringBody(appPreference.getCostumeWashType()));
                entity.addPart("production_name", new StringBody(appPreference.getProductionName()));
                entity.addPart("costume_name", new StringBody(appPreference.getCostumeName()));
                entity.addPart("category", new StringBody(appPreference.getCategoryId()));
                entity.addPart("gender", new StringBody(appPreference.getGender()));
                entity.addPart("size", new StringBody(appPreference.getSize()));
                entity.addPart("theme", new StringBody(appPreference.getThemeId()));
                entity.addPart("condition", new StringBody(appPreference.getCondition()));
                entity.addPart("keywords", new StringBody(appPreference.getKeyword()));
                entity.addPart("make_costume", new StringBody(appPreference.getMakeCostume()));
                entity.addPart("costume_time", new StringBody(appPreference.getCostumeTime()));
                entity.addPart("costume_desc", new StringBody(appPreference.getCostumeDescription()));
                entity.addPart("user_id", new StringBody(appPreference.getUserId()));
                entity.addPart("donation_amount", new StringBody(appPreference.getDonationAmt()));
                entity.addPart("charity_id", new StringBody(appPreference.getCharityId()));
                entity.addPart("return_policy", new StringBody(appPreference.getReturnPolicy()));
                entity.addPart("handling_time", new StringBody(appPreference.getHandalingTime()));

                entity.addPart("faq", new StringBody(appPreference.getFaq()));
                entity.addPart("shipping_option", new StringBody(appPreference.getShippingOptions()));
                entity.addPart("length", new StringBody(appPreference.getLenght()));
                entity.addPart("width", new StringBody(appPreference.getWidth()));
                entity.addPart("height", new StringBody(appPreference.getHeight()));

                entity.addPart("price", new StringBody(appPreference.getShippingPrice()));
                entity.addPart("weight_ounces", new StringBody(appPreference.getOunces()));
                entity.addPart("weight_pounds", new StringBody(appPreference.getPounds()));
                entity.addPart("fun_fact", new StringBody(appPreference.getFunFact()));
                entity.addPart("height-ft", new StringBody(appPreference.getBDHeightfoot()));

                entity.addPart("height-in", new StringBody(appPreference.getBDHeightIn()));
                entity.addPart("weight-lbs", new StringBody(appPreference.getWeghtLbs()));
                entity.addPart("chest-in", new StringBody(appPreference.getChestIn()));
                entity.addPart("waist-lbs", new StringBody(appPreference.getWaistLbs()));
                entity.addPart("quantity", new StringBody(appPreference.getQuantity())); //########

                entity.addPart("fimquality", new StringBody(appPreference.getQuestomQuality()));
                entity.addPart("suggested_organization", new StringBody(appPreference.getSuggestedChrity())); //########

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                entity.writeTo(bytes);
                String content = bytes.toString();

                System.out.println("***********content************" + content.toString());
                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                responseString = EntityUtils.toString(r_entity);

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                p.dismiss();
                System.out.println(">>> Costume Saved Result : " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String strResult = jsonObject.getString("result");
                    String strResultMsg = jsonObject.getString("message");
                    if (strResult.equals("success")) {

                        // Toast.makeText(getActivity(),strResultMsg,Toast.LENGTH_LONG).show();
                        JSONObject dataObj = jsonObject.getJSONObject("data");

                        editor.putString("costumeid", dataObj.getString("costumeid"));
                        editor.putString("skuno", dataObj.getString("skuno"));
                        editor.commit();

                        clearPrefrences();

                        Intent i = new Intent(getActivity(), ConfirmationActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getActivity(), strResultMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                p.dismiss();
            }
        }
    }

}
