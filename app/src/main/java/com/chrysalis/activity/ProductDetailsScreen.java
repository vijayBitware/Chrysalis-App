package com.chrysalis.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.chrysalis.R;
import com.chrysalis.domain.AppPreference;
import com.chrysalis.domain.Config;
import com.chrysalis.domain.ConnectionDetector;
import com.chrysalis.model.ModelSliderImages;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PageItemClickListener;
import me.crosswall.lib.coverflow.core.PagerContainer;

import static com.chrysalis.domain.Config.BASE_URL;

/**
 * Created by Bitware Marketing on 23-05-2017.
 */

public class ProductDetailsScreen extends Activity {
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    @BindView(R.id.ivProductDetailsBack)
    ImageView ivProductDetailsBack;
    @BindView(R.id.llCostumeDescription)
    LinearLayout llCostumeDescription;
    @BindView(R.id.llFAQ)
    LinearLayout llFAQ;
    @BindView(R.id.llSellerInfo)
    LinearLayout llSellerInfo;
    @BindView(R.id.tvPopup)
    TextView tvPopup;
    @BindView(R.id.tvCostumeDescription)
    TextView tvCostumeDescription;
    @BindView(R.id.tvFAQ)
    TextView tvFAQ;
    @BindView(R.id.tvSellerInfo)
    TextView tvSellerInfo;
    @BindView(R.id.ivCostume)
    ImageView ivCostume;
    @BindView(R.id.ivFAQ)
    ImageView ivFAQ;
    @BindView(R.id.ivSeller)
    ImageView ivSeller;
    @BindView(R.id.tvItemConditionsNew)
    TextView tvItemConditionsNew;
    @BindView(R.id.tvSizeDetails)
    TextView tvSizeDetails;
    /*  @BindView(R.id.tvItemLocations)
      TextView tvItemLocations;*/
    @BindView(R.id.tvShipsToDetails)
    TextView tvShipsToDetails;
    @BindView(R.id.tvDeliveryTo)
    TextView tvDeliveryTo;
    @BindView(R.id.tvMainCostumeName)
    TextView tvMainCostumeName;
    @BindView(R.id.tvCostumeMainPrice)
    TextView tvCostumeMainPrice;
    @BindView(R.id.tvImgHeader)
    TextView tvImgHeader;
    ProgressDialog pSignIn;
    JSONArray arrImg;
    AQuery aQuery;
    ArrayList<ModelSliderImages> arrSliderImages;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    Dialog dialogService;
    int screenWidth, screenHeight;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CirclePageIndicator indicator;
    boolean boolCostume = true, boolFAQ = true, boolSeller = true;
    Dialog dialogService2;
    // public static String strIsEditCalled = "No";
    AppPreference appPreference;
    TextView tvGender;
    EditText etHightFt, etHightIn, etWeightLbs, etChestIn, etWeistLbs;
    LinearLayout linearBodyDim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);

        inIt();
    }

    private void inIt() {
        aQuery = new AQuery(this);
        sharedPreferences = this.getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        appPreference = AppPreference.getInstance(ProductDetailsScreen.this);
        tvGender = findViewById(R.id.tvGender);

        etHightFt = findViewById(R.id.etHightFt);
        etChestIn = findViewById(R.id.etChestIn);
        etHightIn = findViewById(R.id.etHightIn);
        etWeightLbs = findViewById(R.id.etWeightLbs);
        etWeistLbs = findViewById(R.id.etWeistLbs);

        linearBodyDim = findViewById(R.id.linearBodyDim);
        appPreference.setCostumeId(sharedPreferences.getString("SelectedCostumeID", ""));

        if (isInternetPresent) {
            new ProductDetails().execute("{\"costume_id\":\"" + sharedPreferences.getString("SelectedCostumeID", "") + "\"}");
        } else {
            Toast.makeText(ProductDetailsScreen.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.tvPopup)
    public void OnPopupClick() {
        CostumeOptionsDialog();
    }

    private void CostumeOptionsDialog() {
        dialogService2 = new Dialog(ProductDetailsScreen.this);
        dialogService2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogService2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogService2.setContentView(R.layout.dialogoptioncastume);
        dialogService2.setCanceledOnTouchOutside(false);

        TextView tvEdit = dialogService2.findViewById(R.id.tvEdit);
        TextView tvDelete = dialogService2.findViewById(R.id.tvDelete);
        TextView tvCancel = dialogService2.findViewById(R.id.tvCancel);

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    Config.strIsEditCalled = "Yes";
                    Config.MODE = Config.EDIT_MODE;
                    Intent i = new Intent(ProductDetailsScreen.this, AddProductsActivityNew.class);
                    // Intent i = new Intent(ProductDetailsScreen.this,AddEditCostumeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(ProductDetailsScreen.this, getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogService2.dismiss();

                if (isInternetPresent) {
                    deleteCostumeDialog(getApplicationContext().getApplicationContext().getString(R.string.strDeleteHeader),
                            getApplicationContext().getApplicationContext().getString(R.string.strDeleteDetails));
                } else {
                    Toast.makeText(ProductDetailsScreen.this, getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogService2.dismiss();
            }
        });

        WindowManager.LayoutParams wmlp = dialogService2.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_HORIZONTAL;
        dialogService2.show();
        dialogService2.getWindow().setLayout((int) ((screenWidth / 5.5) * 4.2), (screenHeight / 12) * 5);
    }



   /* @OnClick(R.id.tvPopup)
    public void OnPopupClick(){
        PopupMenu popup = new PopupMenu(ProductDetailsScreen.this, tvPopup);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Edit")){
                    Config.strIsEditCalled = "Yes";
                    Intent i = new Intent(ProductDetailsScreen.this,AddProductsActivity.class);
                    startActivity(i);
                    finish();
                }
                if(item.getTitle().equals("Delete")){

                    deleteCostumeDialog(getApplicationContext().getApplicationContext().getString(R.string.strDeleteHeader),
                            getApplicationContext().getApplicationContext().getString(R.string.strDeleteDetails));

                }
                Toast.makeText(ProductDetailsScreen.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popup.show();//showing popup menu
    }*/

    @OnClick(R.id.ivProductDetailsBack)
    public void onBackClick() {
        Intent i = new Intent(ProductDetailsScreen.this, ProductListingScreen.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.llCostumeDescription)
    public void onClickOfCostumeDesc() {
        if (boolCostume) {
            boolCostume = false;
            tvCostumeDescription.setVisibility(View.VISIBLE);
            ivCostume.setImageResource(R.drawable.arrdown);
        } else {
            boolCostume = true;
            tvCostumeDescription.setVisibility(View.GONE);
            ivCostume.setImageResource(R.drawable.arrup);
        }
    }

    @OnClick(R.id.llFAQ)
    public void onClickOfllFAQ() {
        if (boolFAQ) {
            boolFAQ = false;
            tvFAQ.setVisibility(View.VISIBLE);
            ivFAQ.setImageResource(R.drawable.arrdown);
        } else {
            boolFAQ = true;
            tvFAQ.setVisibility(View.GONE);
            ivFAQ.setImageResource(R.drawable.arrup);
        }
    }

    @OnClick(R.id.llSellerInfo)
    public void onClickOfllSellerInfo() {
        if (boolSeller) {
            boolSeller = false;
            tvSellerInfo.setVisibility(View.VISIBLE);
            ivSeller.setImageResource(R.drawable.arrdown);
        } else {
            boolSeller = true;
            tvSellerInfo.setVisibility(View.GONE);
            ivSeller.setImageResource(R.drawable.arrup);
        }
    }

    private void deleteCostumeDialog(String strHeader, String strDetails) {
        {
            dialogService = new Dialog(ProductDetailsScreen.this);
            dialogService.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogService.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogService.setContentView(R.layout.dialog_logout_new);
            dialogService.setCancelable(false);
            TextView tvDialogHeader = dialogService.findViewById(R.id.tvDialogCostumeHeader);
            TextView tvDialogDetailsText = dialogService.findViewById(R.id.tvDialogDetailsText);
            TextView tvYes = dialogService.findViewById(R.id.tvYesLogout);
            TextView tvNo = dialogService.findViewById(R.id.tvNoLogout);
            tvDialogHeader.setText(strHeader);
            tvDialogDetailsText.setText(strDetails);
            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogService.dismiss();

                    if (isInternetPresent) {
                        new deleteCostume().execute("{\"costume_id\":\"" + sharedPreferences.getString("SelectedCostumeID", "") + "\"}");
                    } else {
                        Toast.makeText(ProductDetailsScreen.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            tvNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogService.dismiss();
                }
            });
            WindowManager.LayoutParams wmlp = dialogService.getWindow().getAttributes();
            wmlp.gravity = Gravity.CENTER_HORIZONTAL;
            dialogService.show();
            dialogService.getWindow().setLayout((int) ((screenWidth / 5.5) * 4.2), (screenHeight / 12) * 5);
        }
    }

    private void setUpIndicator() {
        PagerContainer container = findViewById(R.id.pager_container);
        final ViewPager pager = container.getViewPager();
        pager.setAdapter(new MyPagerAdapter());
        pager.setClipChildren(false);
        //
        pager.setOffscreenPageLimit(15);
        container.setPageItemClickListener(new PageItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Toast.makeText(ProductDetailsScreen.this,"position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        // boolean showTransformer = getIntent().getBooleanExtra("showTransformer",true);
        boolean showTransformer = true;
        if (showTransformer) {
            new CoverFlow.Builder()
                    .with(pager)
                    .scale(0.5f)
                    .pagerMargin(-10)
                    .spaceSize(1f)
                    .build();
        } else {
            pager.setPageMargin(30);
        }

        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
        System.out.println(">>> Size of arr :" + arrSliderImages.size());
        NUM_PAGES = arrSliderImages.size();
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                pager.setCurrentItem(currentPage++, true);
            }
        };

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ProductDetailsScreen.this, ProductListingScreen.class);
        startActivity(i);
        finish();
    }

    class ProductDetails extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pSignIn = new ProgressDialog(ProductDetailsScreen.this);
            pSignIn.setMessage("In Progress..");
            pSignIn.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            com.squareup.okhttp.Response response = null;
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Log.e("request", params[0]);
            RequestBody body = RequestBody.create(JSON, params[0]);
            Request request = new Request.Builder()
                    .url(BASE_URL + "costume_details")
                    .post(body)
                    .build();
            Log.d("Login request params", String.valueOf(request));
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
            pSignIn.dismiss();
            System.out.println(">>> Result : " + s);
            if (s != null) {
                try {

                    JSONObject jsonObject = new JSONObject(s);
                    String result = jsonObject.getString("result");
                    arrSliderImages = new ArrayList<>();
                    if (result.equals("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        arrImg = jsonObject1.getJSONArray("imgScreen");

                        for (int i = 0; i < arrImg.length(); i++) {
                            JSONObject jsonObject2 = arrImg.getJSONObject(i);
                            ModelSliderImages modelSliderImages = new ModelSliderImages();
                            modelSliderImages.setImagePath(jsonObject2.getString("images"));
                            arrSliderImages.add(modelSliderImages);
                        }
//        holder.tv_productName.setText(arrProductList.get(position).getImgName().substring(0, 1).toUpperCase() + arrProductList.get(position).getImgName().substring(1));

                        JSONObject jsonObject2 = jsonObject1.getJSONObject("costumedetails");
                        tvItemConditionsNew.setText(jsonObject2.getString("condition").substring(0, 1).toUpperCase() + jsonObject2.getString("condition").substring(1));
                        tvSizeDetails.setText(jsonObject2.getString("size").substring(0, 1).toUpperCase() + jsonObject2.getString("size").substring(1));
                        tvGender.setText(jsonObject2.getString("gender").substring(0, 1).toUpperCase() + jsonObject2.getString("gender").substring(1));

                        if (tvSizeDetails.getText().toString().equalsIgnoreCase("custom")) {
                            linearBodyDim.setVisibility(View.VISIBLE);
                            etHightFt.setText(jsonObject2.getString("height_ft"));
                            etHightIn.setText(jsonObject2.getString("height_in"));
                            etChestIn.setText(jsonObject2.getString("chest_in"));
                            etWeightLbs.setText(jsonObject2.getString("weight_lbs"));
                            etWeistLbs.setText(jsonObject2.getString("waist_lbs"));
                        } else {
                            linearBodyDim.setVisibility(View.GONE);
                        }

                        if (jsonObject2.getString("shipto").equals("null") || jsonObject2.getString("shipto").equals(null)) {
                            tvShipsToDetails.setText("");
                        }
                        if (jsonObject2.getString("description").equals("null") || jsonObject2.getString("description").equals(null)) {
                            tvDeliveryTo.setText("");
                        }
                        // tvItemLocations.setText(jsonObject2.getString("item_location"));
                        // tvDeliveryTo.setText(jsonObject2.getString("delivery"));

                        tvCostumeDescription.setText(jsonObject2.getString("description").substring(0, 1).toUpperCase() + jsonObject2.getString("description").substring(1));

                        if (jsonObject2.getString("faq").equalsIgnoreCase("null")) {
                            tvFAQ.setText("");
                        } else {
                            tvFAQ.setText(jsonObject2.getString("faq").substring(0, 1).toUpperCase() + jsonObject2.getString("faq").substring(1));
                        }

                        tvSellerInfo.setText(jsonObject2.getString("seller_info").substring(0, 1).toUpperCase() + jsonObject2.getString("seller_info").substring(1));
                        tvImgHeader.setText(jsonObject2.getString("name").substring(0, 1).toUpperCase() + jsonObject2.getString("name").substring(1));
                        tvMainCostumeName.setText(jsonObject2.getString("name").substring(0, 1).toUpperCase() + jsonObject2.getString("name").substring(1));
                        tvCostumeMainPrice.setText("$" + jsonObject2.getString("price"));

                        setUpIndicator();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                pSignIn.dismiss();
                //Toast.makeText(LoginDetailsActivity.this, R.string.strNetworkErrorMsg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    class deleteCostume extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pSignIn = new ProgressDialog(ProductDetailsScreen.this);
            pSignIn.setMessage("In Progress..");
            pSignIn.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            com.squareup.okhttp.Response response = null;
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Log.e("request", params[0]);
            RequestBody body = RequestBody.create(JSON, params[0]);
            Request request = new Request.Builder()
                    .url(BASE_URL + "delete_costume")
                    .post(body)
                    .build();
            Log.d(" request params", String.valueOf(request));
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
            pSignIn.dismiss();
            System.out.println(">>> Delete Result : " + s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String result = jsonObject.getString("result");
                    if (result.equalsIgnoreCase("success")) {
                        Toast.makeText(ProductDetailsScreen.this, jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ProductDetailsScreen.this, ProductListingScreen.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(ProductDetailsScreen.this, jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                pSignIn.dismiss();
                //Toast.makeText(LoginDetailsActivity.this, R.string.strNetworkErrorMsg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = LayoutInflater.from(ProductDetailsScreen.this).inflate(R.layout.item_cover, null);
            ImageView imageView = view.findViewById(R.id.image_cover);
            System.out.println(">> path :" + arrSliderImages.get(position).getImagePath());
            aQuery.id(imageView).image(arrSliderImages.get(position).getImagePath());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return arrSliderImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

}