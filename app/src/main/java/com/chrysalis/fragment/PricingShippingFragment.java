package com.chrysalis.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chrysalis.R;
import com.chrysalis.activity.AddEditCostumeActivity;
import com.chrysalis.domain.AppPreference;
import com.chrysalis.domain.Config;
import com.chrysalis.domain.RealmController;
import com.chrysalis.domain.Utility;
import com.chrysalis.model.PricingShippingModel;
import com.chrysalis.model.editcostume.EditCostumeResult;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by samsung on 28/7/17.
 */

public class PricingShippingFragment extends BaseFragment {

    public static ArrayList<PricingShippingModel> arrFourNew = null;
    @BindView(R.id.btnNextPricingShipping)
    Button btnNextPricingShipping;
    /*   @BindView(R.id.tvFixedCost)
       TextView tvFixedCost;
       @BindView(R.id.tvActualCost)
       TextView tvActualCost;*/
    // @BindView(R.id.etShippingPrice)
    EditText etShippingPrice;
    @BindView(R.id.tvEstimatedCost)
    TextView tvEstimatedCost;
    @BindView(R.id.etLenght)
    EditText etLenght;
    @BindView(R.id.etWidth)
    EditText etWidth;
    @BindView(R.id.etHeight)
    EditText etHeight;
    @BindView(R.id.etOunces)
    EditText etOunces;
    @BindView(R.id.etPounds)
    EditText etPounds;
    @BindView(R.id.etQuantity)
    EditText etQuantity;
    @BindView(R.id.ivBackPricing)
    ImageView ivBackPricing;
    ArrayList<PricingShippingModel> arrData = null, arrOne = null, arrTwo = null, arrThree = null, arrFour = null, arrFive = null;//,arrZero = null,

    int screenWidth, screenHeight;
    String strShippingOption = "", strShippingPrice = "", strLength = "",
            strWidth = "", strHeight = "", strPounds = "", strOunces = "", strQuantity = "";
    AppPreference appPreference;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_pricing_shippping, container, false);
            ButterKnife.bind(this, view);
            inIt();
            System.out.println("## getMode() at Pricing : " + Config.MODE);
            if (Config.MODE.equals(Config.EDIT_MODE)) {
                setEditView();
            }
        }

        return view;
    }

    private void inIt() {
        screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        appPreference = AppPreference.getInstance(getActivity());
        appPreference = new AppPreference(getActivity());
        etShippingPrice = view.findViewById(R.id.etShippingPrice);

        etShippingPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { // TODO Auto-generated method stub length_loan = edt_loan.length(); if (length_loan == 1 && edt_loan.getText().toString() == "0") edt_loan.setText(""); } } });

                int length_loan = etShippingPrice.length();
                System.out.println("**********" + length_loan);
                if (length_loan == 1 && etShippingPrice.getText().toString().equals("0")) {
                    etShippingPrice.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (Utility.isNetworkAvailable(getActivity())) {
            getShippingDialogDataAPI();
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
        }

    }

    private void getShippingDialogDataAPI() {
        new getShippingData().execute("");
    }

    private void setEditView() {
        EditCostumeResult editCostumeResult = RealmController.with(getActivity()).getEditResponse(appPreference.getCostumeId());

        etShippingPrice.setText(editCostumeResult.getData().getCostumedescScreen().getPrice());
        etLenght.setText(editCostumeResult.getData().getCostumedescScreen().getLengthIn());
        etWidth.setText(editCostumeResult.getData().getCostumedescScreen().getWidthIn());
        etHeight.setText(editCostumeResult.getData().getCostumedescScreen().getHeightIn());
        etPounds.setText(editCostumeResult.getData().getCostumedescScreen().getWeightPounds());
        etOunces.setText(editCostumeResult.getData().getCostumedescScreen().getWeightOunces());
        etQuantity.setText(editCostumeResult.getData().getCostumedescScreen().getQuantity());

       /* String strShippingPrice = editCostumeResult.getData().getCostumedescScreen().getShippingOption();
        if (strShippingPrice.equalsIgnoreCase("USPS Shipping")) {
            onClickOfActualCost();
        } else {
            onClickOfFixedCost();
        }*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // ((AddEditCostumeActivity)getActivity()).getSupportActionBar().setTitle("Price Shipping");
    }

    @OnClick(R.id.ivBackPricing)
    public void onBackClickPricing() {
        getFragmentManager().popBackStackImmediate();
    }

    /* @OnClick(R.id.tvFixedCost)
     public void onClickOfFixedCost() {
         strShippingOption = "Free Shipping";
         tvFixedCost.setTextColor(getResources().getColor(R.color.white));
         tvFixedCost.setBackgroundColor(getResources().getColor(R.color.green));

         tvActualCost.setBackgroundColor(getResources().getColor(R.color.white));
         tvActualCost.setTextColor(getResources().getColor(R.color.green));
     }

     @OnClick(R.id.tvActualCost)
     public void onClickOfActualCost() {
         strShippingOption = "USPS Shipping";
         tvActualCost.setTextColor(getResources().getColor(R.color.white));
         tvActualCost.setBackgroundColor(getResources().getColor(R.color.green));
         tvFixedCost.setBackgroundColor(getResources().getColor(R.color.white));
         tvFixedCost.setTextColor(getResources().getColor(R.color.green));
     }
 */

    @OnClick(R.id.btnNextPricingShipping)
    public void onClickNext() {
        if (Utility.isNetworkAvailable(getActivity())) {
            System.out.println("> strShippingOption :" + strShippingOption);
            strShippingPrice = etShippingPrice.getText().toString().trim();
            System.out.println("> strShippingPrice :" + strShippingPrice);
            strLength = etLenght.getText().toString().trim();
            System.out.println("> strLength :" + strLength);
            strWidth = etWidth.getText().toString().trim();
            System.out.println("> strWidth :" + strWidth);
            strHeight = etHeight.getText().toString().trim();
            System.out.println("> strHeight :" + strHeight);
            strPounds = etPounds.getText().toString().trim();
            strOunces = etOunces.getText().toString().trim();
            strQuantity = etQuantity.getText().toString().trim();

            if (!strShippingPrice.equals("")) {
                if (!strQuantity.isEmpty()) {
                    if (Integer.parseInt(strQuantity) >= 0) {
                        if (!strPounds.equals("")) {
                            if (!strOunces.equals("")) {
                                //if (!strLength.equals("")) {
                                //if (!strWidth.equals("")) {
                                //if (!strHeight.equals("")) {

                                appPreference.setShippingOptions(strShippingOption);
                                appPreference.setShippingPrice(strShippingPrice);
                                appPreference.setLenght(strLength);
                                appPreference.setHeight(strHeight);
                                appPreference.setWidth(strWidth);
                                appPreference.setPounds(strPounds);
                                appPreference.setOunces(strOunces);
                                appPreference.setQuantity(strOunces);

                                ReviewPrefrencesFragment reviewFragment = new ReviewPrefrencesFragment();
                                ((AddEditCostumeActivity) getActivity()).replaceFragment(reviewFragment);

                                        /*} else {
                                            Toast.makeText(getActivity(), "Please enter height", Toast.LENGTH_SHORT).show();
                                        }*/

                                  /*  } else {
                                        Toast.makeText(getActivity(), "Please enter width", Toast.LENGTH_SHORT).show();
                                    }*/

                               /* } else {
                                    Toast.makeText(getActivity(), "Please enter length", Toast.LENGTH_SHORT).show();
                                }*/
                            } else {
                                Toast.makeText(getActivity(), "Please enter ounces", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please enter pounds", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Please enter valid quantity", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter quantity", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), "Please enter costume price", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
        }
    }

    class getShippingData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            com.squareup.okhttp.Response response = null;
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Log.e("request", params[0]);
            RequestBody body = RequestBody.create(JSON, params[0]);
            Request request = new Request.Builder()
                    .url(Config.BASE_URL + "pricing_shipping_data")
                    .post(body)
                    .build();
            try {
                response = client.newCall(request).execute();
                Log.d("response123", String.valueOf(response));
                return response.body().string();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(">>> pricing_shipping_data result : " + s);
            if (s != null) {
                try {
                    JSONObject responseMainJsonObject = new JSONObject(s);
                    String resCode = responseMainJsonObject.getString("result");
                    if (resCode.equalsIgnoreCase("success")) {
                        JSONObject dataObject = responseMainJsonObject.getJSONObject("data");
                        arrData = new ArrayList<>();
                        JSONArray arrJsonData = dataObject.names();
                        for (int i = 0; i < arrJsonData.length(); i++) {

                            PricingShippingModel pricingShippingModel = new PricingShippingModel();
                            pricingShippingModel.setId(arrJsonData.getString(i));
                            pricingShippingModel.setVal(dataObject.getString(String.valueOf(arrJsonData.get(i))));
                            arrData.add(pricingShippingModel);    //Add data
                        }
                     /*   JSONObject ObjZero  = responseMainJsonObject.getJSONObject("0");
                          arrZero = new ArrayList<>();
                        JSONArray arrJsonZero = ObjZero.names();
                        for(int i = 0; i<arrJsonZero.length(); i++){
                            PricingShippingModel pricingShippingModel = new PricingShippingModel();
                            pricingShippingModel.setId(arrJsonZero.getString(i));
                            pricingShippingModel.setVal(ObjZero.getString(String.valueOf(arrJsonZero.get(i))));
                            arrZero.add(pricingShippingModel); //Add data
                        }*/

                        JSONObject ObjOne = responseMainJsonObject.getJSONObject("1");
                        arrOne = new ArrayList<>();
                        JSONArray arrJsonOne = ObjOne.names();
                        for (int i = 0; i < arrJsonOne.length(); i++) {
                            PricingShippingModel pricingShippingModel = new PricingShippingModel();
                            pricingShippingModel.setId(arrJsonOne.getString(i));
                            pricingShippingModel.setVal(ObjOne.getString(String.valueOf(arrJsonOne.get(i))));
                            arrOne.add(pricingShippingModel); //Add data
                        }

                        JSONObject ObjTwo = responseMainJsonObject.getJSONObject("2");
                        arrTwo = new ArrayList<>();
                        JSONArray arrJsonTwo = ObjTwo.names();
                        for (int i = 0; i < arrJsonTwo.length(); i++) {
                            PricingShippingModel pricingShippingModel = new PricingShippingModel();

                            pricingShippingModel.setId(arrJsonTwo.getString(i));
                            pricingShippingModel.setVal(ObjTwo.getString(String.valueOf(arrJsonTwo.get(i))));
                            arrTwo.add(pricingShippingModel); //Add data
                        }

                        JSONObject ObjThree = responseMainJsonObject.getJSONObject("3");
                        arrThree = new ArrayList<>();
                        JSONArray arrJsonThree = ObjThree.names();
                        for (int i = 0; i < arrJsonThree.length(); i++) {
                            PricingShippingModel pricingShippingModel = new PricingShippingModel();

                            pricingShippingModel.setId(arrJsonThree.getString(i));
                            pricingShippingModel.setVal(ObjThree.getString(String.valueOf(arrJsonThree.get(i))));
                            arrThree.add(pricingShippingModel); //Add data
                        }

                        JSONObject ObjFour = responseMainJsonObject.getJSONObject("4");
                        arrFour = new ArrayList<>();
                        //arrFourNew =  new ArrayList<>();
                        JSONArray arrJsonFour = ObjFour.names();
                        for (int i = 0; i < arrJsonFour.length(); i++) {

                            PricingShippingModel pricingShippingModel = new PricingShippingModel();
                            pricingShippingModel.setId(arrJsonFour.getString(i));
                            pricingShippingModel.setVal(ObjFour.getString(String.valueOf(arrJsonFour.get(i))));
                            arrFour.add(pricingShippingModel); //Add data
                            arrFourNew = arrFour;
                            System.out.println(">>>> Size of workign days attr :" + arrFourNew.size());
                        }

                        JSONObject ObjFive = responseMainJsonObject.getJSONObject("5");
                        arrFive = new ArrayList<>();
                        JSONArray arrJsonFive = ObjFive.names();
                        for (int i = 0; i < arrJsonFive.length(); i++) {
                            PricingShippingModel pricingShippingModel = new PricingShippingModel();

                            pricingShippingModel.setId(arrJsonFive.getString(i));
                            pricingShippingModel.setVal(ObjFive.getString(String.valueOf(arrJsonFive.get(i))));
                            arrFive.add(pricingShippingModel); //Add data
                        }

                    } else {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
