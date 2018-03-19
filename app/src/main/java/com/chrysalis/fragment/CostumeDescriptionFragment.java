package com.chrysalis.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chrysalis.R;
import com.chrysalis.activity.AddEditCostumeActivity;
import com.chrysalis.adapter.AdapterCategoryList;
import com.chrysalis.adapter.CostumeSearchAdapter;
import com.chrysalis.domain.AppPreference;
import com.chrysalis.domain.Config;
import com.chrysalis.domain.ConnectionDetector;
import com.chrysalis.domain.RealmController;
import com.chrysalis.domain.Utility;
import com.chrysalis.model.ModelDataObject;
import com.chrysalis.model.editcostume.EditCostumeResult;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by samsung on 28/7/17.
 */

public class CostumeDescriptionFragment extends BaseFragment {

    @BindView(R.id.etProdName)
    EditText etProdName;
    @BindView(R.id.tvHandWashed)
    TextView tvHandWashed;
    @BindView(R.id.tvDryClean)
    TextView tvDryClean;
    @BindView(R.id.tvMachineWashed)
    TextView tvMachineWashed;
    @BindView(R.id.linearCategoryOption)
    LinearLayout linearCategoryOption;
    @BindView(R.id.tvBabies)
    TextView tvBabies;
    @BindView(R.id.etCostumeName)
    EditText etCostumeName;
    @BindView(R.id.tvCostumeCategory)
    TextView tvCostumeCategory;
    @BindView(R.id.tvSexMale)
    TextView tvSexMale;
    @BindView(R.id.tvSexFemale)
    TextView tvSexFemale;
    @BindView(R.id.tvBoys)
    TextView tvBoys;
    @BindView(R.id.tvGirls)
    TextView tvGirls;
    @BindView(R.id.tvCostumeSize)
    TextView tvCostumeSize;
    @BindView(R.id.tvCostumeTheme)
    TextView tvCostumeTheme;
    @BindView(R.id.tvCostumeGood)
    TextView tvCostumeGood;
    @BindView(R.id.tvCostumeExecellent)
    TextView tvCostumeExecellent;
    @BindView(R.id.tvCostumeBrandNew)
    TextView tvCostumeBrandNew;
    @BindView(R.id.tvLikeNew)
    TextView tvLikeNew;
    @BindView(R.id.ivBackCostumeDesc)
    ImageView ivBackCostumeDesc;

    @BindView(R.id.rvMain)
    RecyclerView recyclerSearch;
    @BindView(R.id.ivCostumeYes)
    ImageView ivCostumeYes;
    @BindView(R.id.ivCostumeNo)
    ImageView ivCostumeNo;

    @BindView(R.id.etHour)
    EditText etHour;
    @BindView(R.id.etCostumeDescription)
    EditText etCostumeDescription;
    @BindView(R.id.etFAQ)
    EditText etFAQ;
    @BindView(R.id.llConditionbg)
    LinearLayout llConditionbg;
    @BindView(R.id.llSexbg)
    LinearLayout llSexbg;
    @BindView(R.id.llCostumeMakeTiming)
    LinearLayout llCostumeMakeTiming;
    @BindView(R.id.llFilmTheater)
    LinearLayout llFilmTheater;
    @BindView(R.id.btnCosutumeNext)
    Button btnCosutumeNext;
    @BindView(R.id.etHightFt)
    EditText etHightFt;
    @BindView(R.id.etHightIn)
    EditText etHightIn;
    @BindView(R.id.etWeightLbs)
    EditText etWeightLbs;
    @BindView(R.id.etChestIn)
    EditText etChestIn;
    @BindView(R.id.etWeistLbs)
    EditText etWeistLbs;
    @BindView(R.id.etFunFact)
    EditText etFunFact;
    @BindView(R.id.ivQualityYes)
    ImageView ivQualityYes;
    @BindView(R.id.ivQualityNo)
    ImageView ivQualityNo;
    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(R.id.llSize)
    LinearLayout llSize;
    @BindView(R.id.llCategory)
    LinearLayout llCategory;
    @BindView(R.id.llTheme)
    LinearLayout llTheme;
    @BindView(R.id.linearFaq)
    LinearLayout linearFaq;
    @BindView(R.id.linearBodyDim)
    LinearLayout linearBodyDim;

    String strCostumeCleaned = "", strProductinName = "", strReturnPolicy = "";
    boolean isCategoryClicked = false;
    int screenWidth, screenHeight;
    ListView lvMainCategory;
    TextView tvDialogCostumeHeader;
    boolean isDataforObj = false;
    ArrayList arrNew = new ArrayList<String>();

    Dialog dialogService2;
    ImageView ivClose;
    ConnectionDetector cd;
    boolean isSearchTextCalled = false;
    Boolean isInternetPresent = false;
    ArrayList arrDataObj;
    ArrayList<ModelDataObject> arrOne;
    ArrayList<ModelDataObject> arrTwo;
    ArrayList<ModelDataObject> arrThree;
    ArrayList arrOnClickOfCategory;
    String selectedCat = "", selectedId = "";
    JSONObject responseMainJsonObject;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String strCostumeName = "", strSex = "", strCostumeSize = "", strCostumeTheme = "", strCostumeThemeId = "",
            strCostumeCondition = "", strCostumeDescrSearch = "", strMakeCostume = "", strHour = "", strCostumeDesc = "", strFAQ = "", strSearchKeyword = "";
    String strHeightFt = "", strHeightIn = "", strWeightLbs = "", strChestIn = "", strWaistLbs = "", strCostumeQuality = "", strFunFact = "";
    boolean isAutoCompleteClicked = false;
    ArrayList<ModelDataObject> arrFinalSearch;
    ArrayList arrStrSearch;
    JSONObject jSearchObject;
    AppPreference appPreference;
    boolean boolAdd = false;
    AutoCompleteTextView tvAutoComplete;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.activity_costume_description, container, false);
            ButterKnife.bind(this, view);
            init();
            prepareDataForView();

            System.out.println("# Mode Costume Description :" + Config.MODE);
            if (Config.MODE.equals(Config.EDIT_MODE)) {
                setEditView();
            } else {
                //for initial value set
                onMaleClick();
                onBrandNewClick();
                costumeDefaultState();
                //onCostumeYesClick();
                //costumeQualityYes();
            }
        }
        return view;
    }

    private void costumeDefaultState() {
        strMakeCostume = "";
        ivCostumeYes.setImageResource(R.drawable.no);
        ivCostumeNo.setImageResource(R.drawable.no);

        strCostumeQuality = "";
        ivQualityYes.setImageResource(R.drawable.no);
        ivQualityNo.setImageResource(R.drawable.no);
    }

    private void init() {
        appPreference = AppPreference.getInstance(getActivity());
        screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        sharedPreferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        onCostumeYesClick();

/*
        String mText = "Tip: Have a specific costume? To increase your chances of making a sale, input the appropriate keyword with our existing list of categories here";        Spannable wordtoSpan = new SpannableString("I know just how to whisper, And I know just how to cry,I know just where to find the answers");
        tvSearch.setText(wordtoSpan);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                dialogAvailableCat();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        wordtoSpan.setSpan(clickableSpan,mText.length()-4, mText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
*/

        String mText = "Tip: Have a specific costume? To increase your chances of making a sale, input the appropriate keyword with our existing list of categories here";
        SpannableString ss = new SpannableString(mText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                dialogAvailableCat();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        //ss.setSpan(new ForegroundColorSpan(Color.BLUE), 15, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan, mText.length() - 4, mText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSearch.setText(ss);
        tvSearch.setMovementMethod(LinkMovementMethod.getInstance());
        tvSearch.setHighlightColor(Color.TRANSPARENT);
    }

    private void setEditView() {

        EditCostumeResult editCostumeResult = RealmController.with(getActivity()).getEditResponse(appPreference.getCostumeId());
        if (editCostumeResult != null) {

            //String policy = editCostumeResult.getData().getCostumedescScreen().getReturn_policy();
            //strReturnPolicy = policy;

            String name = editCostumeResult.getData().getCostumedescScreen().getName();
            etCostumeName.setText(name);
            strCostumeName = name;

            String categoryName = editCostumeResult.getData().getCostumedescScreen().getCategory();
            tvCostumeCategory.setText(categoryName);
            selectedCat = categoryName;

            String productionName = editCostumeResult.getData().getCostumedescScreen().getProduction_name();
            etProdName.setText(productionName);
            strCostumeName = productionName;

            String washType = editCostumeResult.getData().getCostumedescScreen().getWash_type();
            //etProdName.setText(washType);
            strCostumeCleaned = washType;

            String mSelectedId = editCostumeResult.getData().getCostumedescScreen().getCatId();
            selectedId = mSelectedId;

            String size = editCostumeResult.getData().getCostumedescScreen().getSize();
            tvCostumeSize.setText(size);
            strCostumeSize = size;

            String mCostumeTheme = editCostumeResult.getData().getCostumedescScreen().getTheme();
            tvCostumeTheme.setText(mCostumeTheme);
            strCostumeTheme = mCostumeTheme;

            String mCostumeThemeId = editCostumeResult.getData().getCostumedescScreen().getThemeId();
            strCostumeThemeId = mCostumeThemeId;

            String mSearchKeyword = editCostumeResult.getData().getCostumedescScreen().getKeyword();
            System.out.println(">>> mSearchKeyword :" + editCostumeResult.getData().getCostumedescScreen().getKeyword());
            strSearchKeyword = mSearchKeyword;
            System.out.println(">>> After assigning data :" + strSearchKeyword);
            ArrayList<String> myList = new ArrayList<String>(Arrays.asList(strSearchKeyword.split(",")));

            arrNew = myList;
            System.out.println(">>> Lenght after assignin : " + arrNew);
            CostumeSearchAdapter searchAdapter = new CostumeSearchAdapter(getActivity(), arrNew);
            LinearLayoutManager llManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerSearch.setLayoutManager(llManager);
            recyclerSearch.setAdapter(searchAdapter);

            strCostumeDesc = editCostumeResult.getData().getCostumedescScreen().getDescription();
            etCostumeDescription.setText(strCostumeDesc);

            strFAQ = editCostumeResult.getData().getCostumedescScreen().getFaq();
            etFAQ.setText(strFAQ);

            strHeightFt = editCostumeResult.getData().getCostumedescScreen().getHeightFt();
            etHightFt.setText(strHeightFt);

            strHeightIn = editCostumeResult.getData().getCostumedescScreen().getHeightInBody();
            etHightIn.setText(strHeightIn);

            strWeightLbs = editCostumeResult.getData().getCostumedescScreen().getWeightLbs();
            etWeightLbs.setText(strWeightLbs);

            strChestIn = editCostumeResult.getData().getCostumedescScreen().getChestIn();
            etChestIn.setText(strChestIn);

            strWaistLbs = editCostumeResult.getData().getCostumedescScreen().getWaistLbs();
            etWeistLbs.setText(strWaistLbs);

            strHour = editCostumeResult.getData().getCostumedescScreen().getCostumeTime();
            etHour.setText(strHour);

            String mMakeCostume = editCostumeResult.getData().getCostumedescScreen().getMakeCostume();
            strMakeCostume = mMakeCostume;
            if (mMakeCostume.equalsIgnoreCase("Yes")) {
                onCostumeYesClick();
            } else {
                onCostumeNoClick();
            }
            String mCondition = editCostumeResult.getData().getCostumedescScreen().getCondition();
            strCostumeCondition = mCondition;
            if (mCondition.equalsIgnoreCase("brand_new")) {
                onBrandNewClick();
            } else if (mCondition.equalsIgnoreCase("good")) {
                onGoodClick();
            } else if (mCondition.equalsIgnoreCase("like_new")) {
                onLikeNew();
            } else if (mCondition.equalsIgnoreCase("excellent")) {
                onExcellentClick();
            }

            if (strCostumeCleaned.equalsIgnoreCase("HandWashed")) {
                onHandWashedClick();
            } else if (strCostumeCleaned.equalsIgnoreCase("DryCleaning")) {
                onDryCleanClick();
            } else if (strCostumeCleaned.equalsIgnoreCase("MachinWashed")) {
                onMachineWashed();
            }

            strFunFact = editCostumeResult.getData().getCostumedescScreen().getFunFact();
            etFunFact.setText(strFunFact);
            String gender = editCostumeResult.getData().getCostumedescScreen().getSex();
            if (gender.equalsIgnoreCase("male")) {
                onMaleClick();
            } else if (gender.equalsIgnoreCase("female")) {
                onFemaleClick();
            } else if (gender.equalsIgnoreCase("boy")) {
                onBoysClick();
            } else if (gender.equalsIgnoreCase("girl")) {
                onGirlsClick();
            } else if (gender.equalsIgnoreCase("baby")) {
                onBabiesClick();
            }
            strCostumeQuality = editCostumeResult.getData().getCostumedescScreen().getFimquality();
            if (strCostumeQuality.equalsIgnoreCase("Yes")) {
                costumeQualityYes();
            } else {
                costumeQualityNo();
            }
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        ((AddEditCostumeActivity) getActivity()).getSupportActionBar().setTitle("Costume Description");
    }

    private void prepareDataForView() {
        // ResponseCategoryList responseCategoryList  =  RealmController.with(getActivity()).getCategoryResponse("1");
        try {
            responseMainJsonObject = new JSONObject(AppPreference.getInstance(getActivity()).getCategoryJson());
            Gson gson = new Gson();
            ModelDataObject s = gson.fromJson(responseMainJsonObject.toString(), ModelDataObject.class);

            //System.out.println("prepared data views " + s.getVal().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String resVal = responseMainJsonObject.getString("result");

            if (resVal.equalsIgnoreCase("success")) {
                arrFinalSearch = new ArrayList<>();
                JSONObject dataObject = responseMainJsonObject.getJSONObject("data");

                JSONArray jsonArray = dataObject.names();
                arrDataObj = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    ModelDataObject modelDataObject = new ModelDataObject();
                    modelDataObject.setId(jsonArray.getString(i));
                    modelDataObject.setVal(dataObject.getString(String.valueOf(jsonArray.get(i))));
                    arrDataObj.add(modelDataObject);
                }

                JSONObject objSearchOne = responseMainJsonObject.getJSONObject("1");
                arrOne = new ArrayList<>();
                JSONArray arrSearchOne = objSearchOne.names();
                for (int i = 0; i < arrSearchOne.length(); i++) {
                    ModelDataObject modelDataObject = new ModelDataObject();
                    modelDataObject.setId(arrSearchOne.getString(i));
                    modelDataObject.setVal(objSearchOne.getString(String.valueOf(arrSearchOne.get(i))));
                    arrOne.add(modelDataObject);
                    arrFinalSearch.add(modelDataObject);
                }

                JSONObject objSearchTwo = responseMainJsonObject.getJSONObject("2");
                arrTwo = new ArrayList<>();
                JSONArray arrSearchTwo = objSearchTwo.names();
                for (int i = 0; i < arrSearchTwo.length(); i++) {
                    ModelDataObject modelDataObject = new ModelDataObject();
                    modelDataObject.setId(arrSearchTwo.getString(i));
                    modelDataObject.setVal(objSearchTwo.getString(String.valueOf(arrSearchTwo.get(i))));
                    arrTwo.add(modelDataObject);
                    arrFinalSearch.add(modelDataObject);
                }

                JSONObject objSearchThree = responseMainJsonObject.getJSONObject("3");
                arrThree = new ArrayList<>();
                JSONArray arrSearchThree = objSearchThree.names();
                for (int i = 0; i < arrSearchThree.length(); i++) {
                    ModelDataObject modelDataObject = new ModelDataObject();
                    modelDataObject.setId(arrSearchThree.getString(i));
                    modelDataObject.setVal(objSearchThree.getString(String.valueOf(arrSearchThree.get(i))));
                    arrThree.add(modelDataObject);
                    arrFinalSearch.add(modelDataObject);
                }
                arrStrSearch = new ArrayList<>();
                System.out.println(">>>>  arrFinalSearch :" + arrFinalSearch.size());
                for (int i = 0; i < arrFinalSearch.size(); i++) {
                    //   System.out.println("--"+arrFinalSearch.get(i).getId()+"--"+arrFinalSearch.get(i).getVal());
                    arrStrSearch.add(arrFinalSearch.get(i).getVal());
                }

                tvAutoCompleteOnClick(); //OnCLick of auto complete searchView

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ivBackCostumeDesc)
    public void onBackClick() {
        //Intent i = new Intent(getActivity(), AddProductsActivity.class);
        //startActivity(i);
        getActivity().finish();
        /*//  getFragmentManager().popBackStackImmediate();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();*/
    }

    @OnClick(R.id.btnCosutumeNext)
    public void onClickNext() {
        // dialogAvailableCat();
        if (strMakeCostume.equalsIgnoreCase("") && strCostumeQuality.equalsIgnoreCase("")) {
            strFAQ = "";
        }

        if (Utility.isNetworkAvailable(getActivity())) {
            System.out.println(">>> Search Array Size :" + arrNew.size());
            strCostumeName = etCostumeName.getText().toString().trim();
            System.out.println(">> strCostumeName :" + strCostumeName);
            strCostumeSize = tvCostumeSize.getText().toString().trim().toLowerCase();
            System.out.println(">> strCostumeSize :" + strCostumeSize);
            strCostumeTheme = tvCostumeTheme.getText().toString().trim();
            System.out.println(">> strCostumeTheme :" + strCostumeTheme);
            strHour = etHour.getText().toString().trim();
            System.out.println(">> strHour :" + strHour);
            strCostumeDesc = etCostumeDescription.getText().toString().trim();
            System.out.println(">> strCostumeDesc :" + strCostumeDesc);
            strFAQ = etFAQ.getText().toString().trim();
            System.out.println(">> strFAQ :" + strFAQ);

            strHeightFt = etHightFt.getText().toString().trim();
            System.out.println(">> strHeightFt :" + strHeightFt);
            strHeightIn = etHightIn.getText().toString().trim();
            System.out.println(">> strHeightIn :" + strHeightIn);
            strWeightLbs = etWeightLbs.getText().toString().trim();
            System.out.println(">> strWeightLbs :" + strWeightLbs);
            strChestIn = etChestIn.getText().toString().trim();
            System.out.println(">> strChestIn :" + strChestIn);
            strWaistLbs = etWeistLbs.getText().toString().trim();
            System.out.println(">> strWaistLbs :" + strWaistLbs);
            strFunFact = etFunFact.getText().toString().trim();
            System.out.println(">> strFunFact :" + strFunFact);
            System.out.println(">> strSearchKeyword  :" + strSearchKeyword);
            strProductinName = etProdName.getText().toString().trim();

            if (!strCostumeName.equals("")) {
                if (!selectedId.equals("")) {
                    if (!strSex.equals("")) {
                        if (!strCostumeSize.equals("")) {
                            // if(!strCostumeThemeId.equals("")){
                            if (!strCostumeCondition.equals("")) {
                                if (arrNew.size() > 0) {
                                    if (!strMakeCostume.equals("")) {
                                        if (!strCostumeQuality.equals("")) {
                                            if (!strCostumeDesc.equals("")) {
                                                if (!strMakeCostume.equals("yes") || strCostumeQuality.equals("yes") && !strFAQ.equals("")) {
                                                    if (strCostumeQuality.equalsIgnoreCase("yes") && !etProdName.getText().toString().equalsIgnoreCase("") || strCostumeQuality.equalsIgnoreCase("no") && etProdName.getText().toString().equalsIgnoreCase("")) {
                                                        if (strCostumeCondition.equalsIgnoreCase("good") && !strCostumeCleaned.equalsIgnoreCase("") || strCostumeCondition.equalsIgnoreCase("like_new") && !strCostumeCleaned.equalsIgnoreCase("") || strCostumeCondition.equalsIgnoreCase("brand_new") && strCostumeCleaned.equalsIgnoreCase("")) {
                                                            // appPreference.setFrontViewImg(AddProductsActivity.strFrontPath);
                                                            // appPreference.setBackViewImg(AddProductsActivity.strBackPath);
                                                            System.out.println("******strCostumeSize*****" + strCostumeSize);
                                                           /* if (!strCostumeSize.equalsIgnoreCase("custom") && etHightFt.getText().toString().equals("") && etHightIn.getText().toString().equals("")
                                                                    && etWeightLbs.getText().toString().equals("") && etChestIn.getText().toString().equals("") &&
                                                                    !etWeistLbs.getText().toString().equals("")) {*/
                                                            if (validation()) {
                                                                appPreference.setCostumeWashType(strCostumeCleaned);
                                                                appPreference.setproductionName(strProductinName);
                                                                appPreference.setCostumeName(strCostumeName);
                                                                appPreference.setCategoryId(selectedId);
                                                                appPreference.setCategoryName(selectedCat);
                                                                appPreference.setGender(strSex);
                                                                appPreference.setSize(strCostumeSize);
                                                                appPreference.setThemeId(strCostumeThemeId);
                                                                appPreference.setThemeName(strCostumeTheme);
                                                                appPreference.setCondition(strCostumeCondition);
                                                                appPreference.setKeyword(strSearchKeyword);
                                                                appPreference.setMakeCostume(strMakeCostume);
                                                                appPreference.setCostumeDescription(strCostumeDesc);
                                                                appPreference.setFaq(strFAQ);

                                                                appPreference.setBDHeightfoot(strHeightFt);
                                                                appPreference.setBDHeightIn(strHeightIn);
                                                                appPreference.setWeghtLbs(strWeightLbs);
                                                                appPreference.setChestIn(strChestIn);
                                                                appPreference.setWaistLbs(strWaistLbs);
                                                                appPreference.setFunFact(strFunFact);
                                                                appPreference.setQuestomQuality(strCostumeQuality);

                                                                if (strHour.equals("")) {
                                                                    strHour = "0";
                                                                    appPreference.setCostumeTime(strHour);
                                                                } else {
                                                                    appPreference.setCostumeTime(strHour);
                                                                }
                                                                PricingShippingFragment priceFragment = new PricingShippingFragment();
                                                                ((AddEditCostumeActivity) getActivity()).replaceFragment(priceFragment);
                                                            }
                                                           /* } else {
                                                                Toast.makeText(getActivity(), "Please enter costume dimensions", Toast.LENGTH_SHORT).show();
                                                            }*/

                                                        } else {
                                                            Toast.makeText(getActivity(), "Please select costume clean type", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(getActivity(), "Please enter production name", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(getActivity(), "Please enter your FAQ for your costume", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getActivity(), "Please describe your costume ", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(getActivity(), "Please select costume quality", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "Please select option for make your costume", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please select appropriate keyword with our existing auto populate category list.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Please select condition.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please select size.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please select sex.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please select category.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please enter costume name.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validation() {
        if (strCostumeSize.equalsIgnoreCase("custom") && etHightFt.getText().toString().equals("") && etHightIn.getText().toString().equals("")
                && etWeightLbs.getText().toString().equals("") && etChestIn.getText().toString().equals("") &&
                etWeistLbs.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Please, enter costume dimensions", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!strCostumeSize.equalsIgnoreCase("custom") && etHightFt.getText().toString().equals("") && etHightIn.getText().toString().equals("")
                && etWeightLbs.getText().toString().equals("") && etChestIn.getText().toString().equals("") &&
                etWeistLbs.getText().toString().equals("")) {
            return true;
        }
        return true;
    }

    private void dialogAvailableCat() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_search);

        dialog.show();
        ImageView close_dialog = dialog.findViewById(R.id.closeCategory);
        LinearLayout llLeft1 = dialog.findViewById(R.id.llLeft1);
        LinearLayout llRight1 = dialog.findViewById(R.id.llRight1);

        LinearLayout llLeft2 = dialog.findViewById(R.id.llLeft2);
        LinearLayout llRight2 = dialog.findViewById(R.id.llRight2);

        LinearLayout llLeft3 = dialog.findViewById(R.id.llLeft3);
        LinearLayout llRight3 = dialog.findViewById(R.id.llRight3);

        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        for (int i = 0; i < arrOne.size(); i++) {
            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.txt_row, null);
            TextView textView = v.findViewById(R.id.tvSearchView);
            textView.setText("\u2022" + arrOne.get(i).getVal().toString());
            if (i % 2 != 0) {
                llLeft1.addView(v);
            } else {
                llRight1.addView(v);
            }
        }

        for (int i = 0; i < arrTwo.size(); i++) {
            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.txt_row, null);
            TextView textView = v.findViewById(R.id.tvSearchView);
            textView.setText("\u2022" + arrTwo.get(i).getVal().toString());
            if (i % 2 != 0) {
                llLeft2.addView(v);
            } else {
                llRight2.addView(v);
            }
        }

        for (int i = 0; i < arrThree.size(); i++) {
            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.txt_row, null);
            TextView textView = v.findViewById(R.id.tvSearchView);
            textView.setText("\u2022" + arrThree.get(i).getVal().toString());
            if (i % 2 != 0) {
                llLeft3.addView(v);
            } else {
                llRight3.addView(v);
            }
        }
    }

    @OnClick(R.id.ivCostumeYes)
    public void onCostumeYesClick() {
        System.out.println("*********costume yes******");
        strMakeCostume = "Yes";
        linearFaq.setVisibility(View.VISIBLE);
        ivCostumeYes.setImageResource(R.drawable.yes);
        ivCostumeNo.setImageResource(R.drawable.no);
        llCostumeMakeTiming.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.ivCostumeNo)
    public void onCostumeNoClick() {
        System.out.println("*********costume no******");
        strMakeCostume = "No";
        if (strCostumeQuality.equalsIgnoreCase("yes")) {
            System.out.println("*********ivQualityYes  if***");
            linearFaq.setVisibility(View.VISIBLE);
        } else {
            linearFaq.setVisibility(View.GONE);
        }
        llCostumeMakeTiming.setVisibility(View.GONE);
        ivCostumeNo.setImageResource(R.drawable.yes);
        ivCostumeYes.setImageResource(R.drawable.no);
    }

    @OnClick(R.id.ivQualityYes)
    public void costumeQualityYes() {
        System.out.println("*********quality yes******");

        linearFaq.setVisibility(View.VISIBLE);
        llFilmTheater.setVisibility(View.VISIBLE);
        strCostumeQuality = "Yes";
        ivQualityYes.setImageResource(R.drawable.yes);
        ivQualityNo.setImageResource(R.drawable.no);
    }

    @OnClick(R.id.ivQualityNo)
    public void costumeQualityNo() {
        strProductinName = "";
        strFAQ = "";
        etProdName.setText("");
        System.out.println("*********costume no******");
        if (strMakeCostume.equalsIgnoreCase("yes")) {
            System.out.println("*********costume yes if******" + ivCostumeYes.getVisibility() + "**" + View.VISIBLE);
            linearFaq.setVisibility(View.VISIBLE);
        } else {
            linearFaq.setVisibility(View.GONE);
        }
        llFilmTheater.setVisibility(View.GONE);
        strCostumeQuality = "No";
        ivQualityNo.setImageResource(R.drawable.yes);
        ivQualityYes.setImageResource(R.drawable.no);
    }

  /*  @OnClick(R.id.tvCostumeSize)
    public void tvCostumeSize() {
        if (Utility.isNetworkAvailable(getActivity())) {
            DialogForSize(tvCostumeSize, "Size");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
        }
    }*/

    @OnClick(R.id.llSize)
    public void llSizeClick() {
        if (Utility.isNetworkAvailable(getActivity())) {
            DialogForSize(tvCostumeSize, "Size");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.llCategory)
    public void llCategory() {
        if (Utility.isNetworkAvailable(getActivity())) {
            isCategoryClicked = true;
            dialogCostumeCategory(tvCostumeCategory, "Category", arrDataObj);
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.llTheme)
    public void llTheme() {
        if (Utility.isNetworkAvailable(getActivity())) {
            if (!selectedId.equals("")) {
                if (isDataforObj) {
                    Toast.makeText(getActivity(), "Subcategory is not available for selected category", Toast.LENGTH_SHORT).show();
                } else {
                    if (Config.MODE.equalsIgnoreCase(Config.ADD_MODE)) {
                        if (isCategoryClicked) {
                            dialogCostumeTheme(tvCostumeTheme, "Subcategory", arrOnClickOfCategory);
                        } else {
                            Toast.makeText(getActivity(), "Please select category first", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jsonObject = responseMainJsonObject.getJSONObject("0");
                            jSearchObject = responseMainJsonObject.getJSONObject("0");
                            System.out.println("## Edited Seleted Cat is :" + selectedCat);
                            String strData = jsonObject.getString(selectedCat);
                            Object json = new JSONTokener(strData).nextValue(); //Checking given val is JSON OBJ OR JSON Array
                            if (json instanceof JSONObject) {
                                isDataforObj = false;
                                JSONObject objSubCatData = jsonObject.getJSONObject(selectedCat);
                                JSONArray jsonArray = objSubCatData.names();
                                arrOnClickOfCategory = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ModelDataObject modelDataObject = new ModelDataObject();

                                    modelDataObject.setId(jsonArray.getString(i));
                                    modelDataObject.setVal(objSubCatData.getString(String.valueOf(jsonArray.get(i))));

                                    arrOnClickOfCategory.add(modelDataObject);
                                }
                                dialogCostumeTheme(tvCostumeTheme, "Subcategory", arrOnClickOfCategory);
                            } else if (json instanceof JSONArray) {
                                // isDataforObj = true;
                                Toast.makeText(getActivity(), "Subcategory is not available for selected category", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                Toast.makeText(getActivity(), "Please select category first.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.tvSexMale)
    public void onMaleClick() {
        strSex = "male";
        llSexbg.setBackground(getResources().getDrawable(R.drawable.green_cell_border));
        tvSexMale.setTextColor(getResources().getColor(R.color.white));
        tvSexMale.setBackgroundColor(getResources().getColor(R.color.green));
        tvSexFemale.setBackgroundColor(getResources().getColor(R.color.white));
        tvSexFemale.setTextColor(getResources().getColor(R.color.green));
        tvBoys.setBackgroundColor(getResources().getColor(R.color.white));
        tvBoys.setTextColor(getResources().getColor(R.color.green));
        tvGirls.setBackgroundColor(getResources().getColor(R.color.white));
        tvGirls.setTextColor(getResources().getColor(R.color.green));
        tvBabies.setBackgroundColor(getResources().getColor(R.color.white));
        tvBabies.setTextColor(getResources().getColor(R.color.green));
    }

    @OnClick(R.id.tvSexFemale)
    public void onFemaleClick() {
        strSex = "female";
        llSexbg.setBackground(getResources().getDrawable(R.drawable.green_cell_border));
        tvSexFemale.setTextColor(getResources().getColor(R.color.white));
        tvSexFemale.setBackgroundColor(getResources().getColor(R.color.green));
        tvSexMale.setBackgroundColor(getResources().getColor(R.color.white));
        tvSexMale.setTextColor(getResources().getColor(R.color.green));
        tvBoys.setBackgroundColor(getResources().getColor(R.color.white));
        tvBoys.setTextColor(getResources().getColor(R.color.green));
        tvGirls.setBackgroundColor(getResources().getColor(R.color.white));
        tvGirls.setTextColor(getResources().getColor(R.color.green));
        tvBabies.setBackgroundColor(getResources().getColor(R.color.white));
        tvBabies.setTextColor(getResources().getColor(R.color.green));
    }

    @OnClick(R.id.tvBoys)
    public void onBoysClick() {
        strSex = "boy";
        llSexbg.setBackground(getResources().getDrawable(R.drawable.green_cell_border));
        tvBoys.setTextColor(getResources().getColor(R.color.white));
        tvBoys.setBackgroundColor(getResources().getColor(R.color.green));
        tvSexMale.setBackgroundColor(getResources().getColor(R.color.white));
        tvSexMale.setTextColor(getResources().getColor(R.color.green));
        tvSexFemale.setBackgroundColor(getResources().getColor(R.color.white));
        tvSexFemale.setTextColor(getResources().getColor(R.color.green));
        tvGirls.setBackgroundColor(getResources().getColor(R.color.white));
        tvGirls.setTextColor(getResources().getColor(R.color.green));
        tvBabies.setBackgroundColor(getResources().getColor(R.color.white));
        tvBabies.setTextColor(getResources().getColor(R.color.green));
    }

    @OnClick(R.id.tvGirls)
    public void onGirlsClick() {
        strSex = "girl";
        llSexbg.setBackground(getResources().getDrawable(R.drawable.green_cell_border));
        tvGirls.setTextColor(getResources().getColor(R.color.white));
        tvGirls.setBackgroundColor(getResources().getColor(R.color.green));
        tvSexMale.setBackgroundColor(getResources().getColor(R.color.white));
        tvSexMale.setTextColor(getResources().getColor(R.color.green));
        tvSexFemale.setBackgroundColor(getResources().getColor(R.color.white));
        tvSexFemale.setTextColor(getResources().getColor(R.color.green));
        tvBoys.setBackgroundColor(getResources().getColor(R.color.white));
        tvBoys.setTextColor(getResources().getColor(R.color.green));
        tvBabies.setBackgroundColor(getResources().getColor(R.color.white));
        tvBabies.setTextColor(getResources().getColor(R.color.green));
    }

    @OnClick(R.id.tvBabies)
    public void onBabiesClick() {
        strSex = "baby";
        llSexbg.setBackground(getResources().getDrawable(R.drawable.green_cell_border));
        tvGirls.setTextColor(getResources().getColor(R.color.green));
        tvGirls.setBackgroundColor(getResources().getColor(R.color.white));
        tvSexMale.setBackgroundColor(getResources().getColor(R.color.white));
        tvSexMale.setTextColor(getResources().getColor(R.color.green));
        tvSexFemale.setBackgroundColor(getResources().getColor(R.color.white));
        tvSexFemale.setTextColor(getResources().getColor(R.color.green));
        tvBoys.setBackgroundColor(getResources().getColor(R.color.white));
        tvBoys.setTextColor(getResources().getColor(R.color.green));
        tvBabies.setBackgroundColor(getResources().getColor(R.color.green));
        tvBabies.setTextColor(getResources().getColor(R.color.white));
    }

    @OnClick(R.id.tvCostumeGood)
    public void onGoodClick() {
        strCostumeCondition = "good";
        llConditionbg.setBackground(getResources().getDrawable(R.drawable.green_cell_border));
        linearCategoryOption.setVisibility(View.VISIBLE);
        tvCostumeGood.setTextColor(getResources().getColor(R.color.white));
        tvCostumeGood.setBackgroundColor(getResources().getColor(R.color.green));
        tvCostumeExecellent.setBackgroundColor(getResources().getColor(R.color.white));
        tvCostumeExecellent.setTextColor(getResources().getColor(R.color.green));
        tvCostumeBrandNew.setBackgroundColor(getResources().getColor(R.color.white));
        tvCostumeBrandNew.setTextColor(getResources().getColor(R.color.green));
        tvLikeNew.setBackgroundColor(getResources().getColor(R.color.white));
        tvLikeNew.setTextColor(getResources().getColor(R.color.green));
    }

    @OnClick(R.id.tvCostumeExecellent)
    public void onExcellentClick() {
        strCostumeCondition = "excellent";
        linearCategoryOption.setVisibility(View.GONE);
        llConditionbg.setBackground(getResources().getDrawable(R.drawable.green_cell_border));
        tvCostumeExecellent.setTextColor(getResources().getColor(R.color.white));
        tvCostumeExecellent.setBackgroundColor(getResources().getColor(R.color.green));
        tvCostumeGood.setBackgroundColor(getResources().getColor(R.color.white));
        tvCostumeGood.setTextColor(getResources().getColor(R.color.green));
        tvCostumeBrandNew.setBackgroundColor(getResources().getColor(R.color.white));
        tvCostumeBrandNew.setTextColor(getResources().getColor(R.color.green));
        tvLikeNew.setBackgroundColor(getResources().getColor(R.color.white));
        tvLikeNew.setTextColor(getResources().getColor(R.color.green));
    }

    @OnClick(R.id.tvCostumeBrandNew)
    public void onBrandNewClick() {
        strCostumeCleaned = "";
        strCostumeCondition = "brand_new";
        linearCategoryOption.setVisibility(View.GONE);
        llConditionbg.setBackground(getResources().getDrawable(R.drawable.green_cell_border));
        tvCostumeBrandNew.setTextColor(getResources().getColor(R.color.white));
        tvCostumeBrandNew.setBackgroundColor(getResources().getColor(R.color.green));
        tvCostumeExecellent.setBackgroundColor(getResources().getColor(R.color.white));
        tvCostumeExecellent.setTextColor(getResources().getColor(R.color.green));
        tvCostumeGood.setBackgroundColor(getResources().getColor(R.color.white));
        tvCostumeGood.setTextColor(getResources().getColor(R.color.green));
        tvLikeNew.setBackgroundColor(getResources().getColor(R.color.white));
        tvLikeNew.setTextColor(getResources().getColor(R.color.green));
    }

    @OnClick(R.id.tvLikeNew)
    public void onLikeNew() {
        linearCategoryOption.setVisibility(View.VISIBLE);
        strCostumeCondition = "like_new";
        llConditionbg.setBackground(getResources().getDrawable(R.drawable.green_cell_border));
        tvLikeNew.setTextColor(getResources().getColor(R.color.white));
        tvLikeNew.setBackgroundColor(getResources().getColor(R.color.green));
        tvCostumeExecellent.setBackgroundColor(getResources().getColor(R.color.white));
        tvCostumeExecellent.setTextColor(getResources().getColor(R.color.green));
        tvCostumeBrandNew.setBackgroundColor(getResources().getColor(R.color.white));
        tvCostumeBrandNew.setTextColor(getResources().getColor(R.color.green));
        tvCostumeGood.setBackgroundColor(getResources().getColor(R.color.white));
        tvCostumeGood.setTextColor(getResources().getColor(R.color.green));
    }

    @OnClick(R.id.tvHandWashed)
    public void onHandWashedClick() {
        strCostumeCleaned = "HandWashed";
        //linearCategoryOption.setBackground(getResources().getDrawable(R.drawable.green_cell_border));
        tvHandWashed.setTextColor(getResources().getColor(R.color.white));
        tvHandWashed.setBackgroundColor(getResources().getColor(R.color.green));
        tvDryClean.setBackgroundColor(getResources().getColor(R.color.white));
        tvDryClean.setTextColor(getResources().getColor(R.color.green));
        tvMachineWashed.setBackgroundColor(getResources().getColor(R.color.white));
        tvMachineWashed.setTextColor(getResources().getColor(R.color.green));
    }

    @OnClick(R.id.tvDryClean)
    public void onDryCleanClick() {
        strCostumeCleaned = "DryCleaning";
        //linearCategoryOption.setBackground(getResources().getDrawable(R.drawable.green_cell_border));
        tvHandWashed.setTextColor(getResources().getColor(R.color.green));
        tvHandWashed.setBackgroundColor(getResources().getColor(R.color.white));
        tvDryClean.setBackgroundColor(getResources().getColor(R.color.green));
        tvDryClean.setTextColor(getResources().getColor(R.color.white));
        tvMachineWashed.setBackgroundColor(getResources().getColor(R.color.white));
        tvMachineWashed.setTextColor(getResources().getColor(R.color.green));
    }

    @OnClick(R.id.tvMachineWashed)
    public void onMachineWashed() {
        strCostumeCleaned = "MachinWashed";
        //linearCategoryOption.setBackground(getResources().getDrawable(R.drawable.green_cell_border));
        tvHandWashed.setTextColor(getResources().getColor(R.color.green));
        tvHandWashed.setBackgroundColor(getResources().getColor(R.color.white));
        tvDryClean.setBackgroundColor(getResources().getColor(R.color.white));
        tvDryClean.setTextColor(getResources().getColor(R.color.green));
        tvMachineWashed.setBackgroundColor(getResources().getColor(R.color.green));
        tvMachineWashed.setTextColor(getResources().getColor(R.color.white));

    }
/*    @OnClick(R.id.tvCostumeCategory)
    public void tvCostumeCategory() {
        isCategoryClicked = true;
        dialogCostumeCategory(tvCostumeCategory, "Category", arrDataObj);
    }*/


    /*@OnClick(R.id.tvCostumeTheme)
    public void tvCostumeThmeme() {

        if (!selectedId.equals("")) {
            if (isDataforObj) {
                Toast.makeText(getActivity(), "Theme is not available for selected category", Toast.LENGTH_SHORT).show();
            } else {
                if (Config.MODE.equalsIgnoreCase(Config.ADD_MODE)) {
                    if (isCategoryClicked) {
                        dialogCostumeTheme(tvCostumeTheme, "Theme", arrOnClickOfCategory);
                    } else {
                        Toast.makeText(getActivity(), "Please select category first", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    //;


                    try {
                        JSONObject jsonObject = responseMainJsonObject.getJSONObject("0");
                        jSearchObject = responseMainJsonObject.getJSONObject("0");
                        System.out.println("## Edited Seleted Cat is :"+selectedCat);
                        String strData = jsonObject.getString(selectedCat);
                        Object json = new JSONTokener(strData).nextValue(); //Checking given val is JSON OBJ OR JSON Array
                        if (json instanceof JSONObject) {
                            isDataforObj = false;
                            JSONObject objSubCatData = jsonObject.getJSONObject(selectedCat);
                            JSONArray jsonArray = objSubCatData.names();
                            arrOnClickOfCategory = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                ModelDataObject modelDataObject = new ModelDataObject();

                                modelDataObject.setId(jsonArray.getString(i));
                                modelDataObject.setVal(objSubCatData.getString(String.valueOf(jsonArray.get(i))));

                                arrOnClickOfCategory.add(modelDataObject);
                            }
                            dialogCostumeTheme(tvCostumeTheme, "Theme", arrOnClickOfCategory);
                        } else if (json instanceof JSONArray) {
                           // isDataforObj = true;
                             Toast.makeText(getActivity(),"Theme is not available for selected category",Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        } else {
            Toast.makeText(getActivity(), "Please select category first.", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void dialogCostumeTheme(final TextView tvName, String category, final ArrayList<ModelDataObject> arrDataObj) {

        dialogService2 = new Dialog(getActivity());
        dialogService2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogService2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogService2.setContentView(R.layout.dialog_cat_list);
        dialogService2.setCanceledOnTouchOutside(false);

        ivClose = dialogService2.findViewById(R.id.ivCloseNew);
        tvDialogCostumeHeader = dialogService2.findViewById(R.id.tvDialogCostumeHeader);
        lvMainCategory = dialogService2.findViewById(R.id.lvMainCategory);
        tvDialogCostumeHeader.setText(category);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogService2.dismiss();
            }
        });
        AdapterCategoryList adapterCategoryList = new AdapterCategoryList(getActivity(), R.layout.row_costume_category_dialog, arrDataObj);
        lvMainCategory.setAdapter(adapterCategoryList);

        lvMainCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedTheme = arrDataObj.get(position).getVal();
                String selectedThemeId = arrDataObj.get(position).getId();
                System.out.println(">>>-- " + selectedThemeId);
                tvName.setText(selectedTheme);
                strCostumeThemeId = selectedThemeId;
                dialogService2.dismiss();

            }
        });

        WindowManager.LayoutParams wmlp = dialogService2.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_HORIZONTAL;
        dialogService2.show();
        dialogService2.getWindow().setLayout((int) ((screenWidth / 5.5) * 4.2), (screenHeight / 12) * 5);
    }

    private void dialogCostumeCategory(final TextView tvCostumeCategory, String category, final ArrayList<ModelDataObject> arrDataObj) {

        dialogService2 = new Dialog(getActivity());
        dialogService2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogService2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogService2.setContentView(R.layout.dialog_cat_list);
        dialogService2.setCanceledOnTouchOutside(false);

        ivClose = dialogService2.findViewById(R.id.ivCloseNew);
        tvDialogCostumeHeader = dialogService2.findViewById(R.id.tvDialogCostumeHeader);
        lvMainCategory = dialogService2.findViewById(R.id.lvMainCategory);
        tvDialogCostumeHeader.setText(category);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogService2.dismiss();
            }
        });
        AdapterCategoryList adapterCategoryList = new AdapterCategoryList(getActivity(), R.layout.row_costume_category_dialog, arrDataObj);
        lvMainCategory.setAdapter(adapterCategoryList);

        lvMainCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCat = arrDataObj.get(position).getVal();
                selectedId = arrDataObj.get(position).getId();
                editor.putString("edtcategory", selectedCat);
                editor.commit();
                tvCostumeCategory.setText(selectedCat);
                tvCostumeTheme.setText("");
                dialogService2.dismiss();
                try {
                    JSONObject jsonObject = responseMainJsonObject.getJSONObject("0");
                    jSearchObject = responseMainJsonObject.getJSONObject("0");

                    String strData = jsonObject.getString(selectedCat);
                    Object json = new JSONTokener(strData).nextValue(); //Checking given val is JSON OBJ OR JSON Array
                    if (json instanceof JSONObject) {
                        isDataforObj = false;
                        JSONObject objSubCatData = jsonObject.getJSONObject(selectedCat);
                        JSONArray jsonArray = objSubCatData.names();
                        arrOnClickOfCategory = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            ModelDataObject modelDataObject = new ModelDataObject();

                            modelDataObject.setId(jsonArray.getString(i));
                            modelDataObject.setVal(objSubCatData.getString(String.valueOf(jsonArray.get(i))));

                            arrOnClickOfCategory.add(modelDataObject);
                        }

                    } else if (json instanceof JSONArray) {
                        isDataforObj = true;
                        // Toast.makeText(CostumeDescriptionActivity.this,"Theme is not available for selected category",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        WindowManager.LayoutParams wmlp = dialogService2.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_HORIZONTAL;
        dialogService2.show();
        dialogService2.getWindow().setLayout((int) ((screenWidth / 5.5) * 4.2), (screenHeight / 12) * 5);

    }

    private void DialogForSize(final TextView tvNew, String category) {
        dialogService2 = new Dialog(getActivity());
        dialogService2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogService2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogService2.setContentView(R.layout.dialog_cat_list);
        dialogService2.setCanceledOnTouchOutside(false);
        ivClose = dialogService2.findViewById(R.id.ivCloseNew);
        tvDialogCostumeHeader = dialogService2.findViewById(R.id.tvDialogCostumeHeader);
        lvMainCategory = dialogService2.findViewById(R.id.lvMainCategory);
        tvDialogCostumeHeader.setText(category);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogService2.dismiss();
            }
        });
        final String[] arrGivenSizes = {"1SZ", "XXS", "XS", "S", "M", "L", "XL", "XXL", "Custom"};
        ArrayList arrModelSize = new ArrayList<>();
        for (int i = 0; i < arrGivenSizes.length; i++) {
            ModelDataObject modelDataObject = new ModelDataObject();
            modelDataObject.setVal(arrGivenSizes[i]);
            modelDataObject.setId("");
            arrModelSize.add(modelDataObject);
        }
        lvMainCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                tvNew.setText(String.valueOf(arrGivenSizes[position]));
                if (String.valueOf(arrGivenSizes[position]).equalsIgnoreCase("Custom")) {
                    linearBodyDim.setVisibility(View.VISIBLE);
                } else {
                    linearBodyDim.setVisibility(View.GONE);
                }
                dialogService2.dismiss();
            }
        });
        AdapterCategoryList adapterCategoryList = new AdapterCategoryList(getActivity(), R.layout.row_costume_category_dialog, arrModelSize);
        lvMainCategory.setAdapter(adapterCategoryList);
        WindowManager.LayoutParams wmlp = dialogService2.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_HORIZONTAL;
        dialogService2.show();
        dialogService2.getWindow().setLayout((int) ((screenWidth / 5.5) * 4.2),
                (screenHeight / 12) * 5);

    }

    private void tvAutoCompleteOnClick() {
        isAutoCompleteClicked = true;
        tvAutoComplete = view.findViewById(R.id.autoCompleteTextView1);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrStrSearch);
        // arrNew = new ArrayList<String>();
        tvAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String strMyVal = tvAutoComplete.getText().toString();

                if (arrNew.size() <= 10) {
                    if (!arrNew.contains(strMyVal)) {
                        arrNew.add(strMyVal);
                    }
                } else {
                    Toast.makeText(getActivity(), "Maximum keyword is 10", Toast.LENGTH_SHORT).show();
                }

                tvAutoComplete.setText("");
                strSearchKeyword = android.text.TextUtils.join(",", arrNew);

                CostumeSearchAdapter searchAdapter = new CostumeSearchAdapter(getActivity(), arrNew);
                LinearLayoutManager llManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                recyclerSearch.setLayoutManager(llManager);
                recyclerSearch.setAdapter(searchAdapter);
            }
        });

        tvAutoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                isSearchTextCalled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    isSearchTextCalled = true;
                    String strMyVal = tvAutoComplete.getText().toString();
                    if (arrNew.size() <= 10) {
                        if (!arrNew.contains(strMyVal)) {
                            arrNew.add(strMyVal);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Maximum keyword is 10", Toast.LENGTH_SHORT).show();
                    }

                    tvAutoComplete.setText("");
                    strSearchKeyword = android.text.TextUtils.join(",", arrNew);

                    CostumeSearchAdapter searchAdapter = new CostumeSearchAdapter(getActivity(), arrNew);
                    LinearLayoutManager llManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerSearch.setLayoutManager(llManager);
                    recyclerSearch.setAdapter(searchAdapter);

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tvAutoComplete.getWindowToken(), 0);
                }
                return isSearchTextCalled;
            }
        });

        tvAutoComplete.setAdapter(adapter);
        tvAutoComplete.setThreshold(1);

    }

/*    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(getActivity(), "keyboard visible", Toast.LENGTH_SHORT).show();

        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(getActivity(), "keyboard Hide", Toast.LENGTH_SHORT).show();

            boolean isFocus = tvAutoComplete.hasFocus();
            System.out.println(">>> isFocus Autocomplete :"+isFocus);
            if( tvAutoComplete.hasFocus() && !tvAutoComplete.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), tvAutoComplete.getText().toString(), Toast.LENGTH_SHORT).show();
                String strMyVal = tvAutoComplete.getText().toString();
                if (arrNew.size() <= 10) {
                    if (!arrNew.contains(strMyVal)) {
                        arrNew.add(strMyVal);
                    }
                } else {
                    Toast.makeText(getActivity(), "Maximum keyword is 10", Toast.LENGTH_SHORT).show();
                }

                tvAutoComplete.setText("");
                strSearchKeyword = android.text.TextUtils.join(",", arrNew);

                CostumeSearchAdapter searchAdapter = new CostumeSearchAdapter(getActivity(), arrNew);
                LinearLayoutManager llManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                recyclerSearch.setLayoutManager(llManager);
                recyclerSearch.setAdapter(searchAdapter);
            }

        }
    }
    */
}
