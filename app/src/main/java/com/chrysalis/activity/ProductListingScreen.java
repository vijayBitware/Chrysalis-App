
package com.chrysalis.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chrysalis.R;
import com.chrysalis.adapter.AdapterProductListing;
import com.chrysalis.domain.AppPreference;
import com.chrysalis.domain.AppSingleton;
import com.chrysalis.domain.Config;
import com.chrysalis.domain.ConnectionDetector;
import com.chrysalis.model.ModelMyListingNew;
import com.chrysalis.model.ModelProductListing;
import com.chrysalis.model.mylisting.ModelMyListing;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.chrysalis.domain.Config.BASE_URL;

/**
 * Created by Bitware Marketing on 23-05-2017.
 */
public class ProductListingScreen extends Activity {
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    GridView gridView;
    AdapterProductListing adapterProductListing;
    Dialog dialogService;
    int screenWidth, screenHeight;
    ImageView ivAddImages, ivRefresh;
    ArrayList<ModelProductListing> arrProductsPage;
    // TextView tvCreateListing;
    LinearLayout llIntroList, llGridList;
    LinearLayout llCreateListing;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    ProgressDialog progressDialog = null;
    ArrayList<ModelMyListingNew> myDataList = null;
    TextView tvLogout;
    AppPreference appPreference;
    boolean isResumeCalled = false;
    String[] permissionsRequired = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_new);
        permissionStatus = this.getSharedPreferences("MyPref", MODE_PRIVATE);
        inIt();
        // showWelcomeDialog();
    }

    private void inIt() {
        Config.strIsEditCalled = "No";
        ivAddImages = findViewById(R.id.ivAddImages);
        ivRefresh = findViewById(R.id.ivRefresh);

        gridView = findViewById(R.id.gridView);
        sharedPreferences = this.getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        llIntroList = findViewById(R.id.llIntroList);
        llGridList = findViewById(R.id.llGridList);
        llCreateListing = findViewById(R.id.llCreateListing);
        //  tvCreateListing = (TextView) findViewById(R.id.tvCreateListing);
        appPreference = new AppPreference(ProductListingScreen.this);
        screenWidth = ProductListingScreen.this.getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = ProductListingScreen.this.getWindowManager().getDefaultDisplay().getHeight();
        arrProductsPage = new ArrayList<>();

        tvLogout = findViewById(R.id.tvLogout);

        callListAPI();

        widgetsOnClick();

        appPreference.remove("photoModel");

    }

    private void callListAPI() {
        if (isInternetPresent) {
            String myListingURL = BASE_URL + "my_listing";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userid", sharedPreferences.getString("userId", ""));
                callListingAPI(myListingURL, jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
        }
    }

    private void widgetsOnClick() {
        ivAddImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPermission();
            }
        });

        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callListAPI();
            }
        });

        llCreateListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPermission();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String myId = myDataList.get(pos).getCostumeId();
                editor.putString("SelectedCostumeID", myId);
                editor.commit();

                System.out.println(">> Click Id is :" + myId);
                Intent i2 = new Intent(ProductListingScreen.this, ProductDetailsScreen.class);
                startActivity(i2);
                //  finish();
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogOutDilaog(getApplicationContext().getApplicationContext().getString(R.string.strlogoutHeader),
                        getApplicationContext().getApplicationContext().getString(R.string.strlogoutDetails));

            }
        });
    }

    private void showLogOutDilaog(String strHeader, String strDetails) {

        dialogService = new Dialog(ProductListingScreen.this);
        dialogService.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogService.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogService.setContentView(R.layout.dialog_logout_new);
        dialogService.setCancelable(false);
        TextView tvYes = dialogService.findViewById(R.id.tvYesLogout);
        TextView tvNo = dialogService.findViewById(R.id.tvNoLogout);
        TextView tvDialogHeader = dialogService.findViewById(R.id.tvDialogCostumeHeader);
        TextView tvDialogDetailsText = dialogService.findViewById(R.id.tvDialogDetailsText);

        tvDialogHeader.setText(strHeader);
        tvDialogDetailsText.setText(strDetails);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("isLogin", "No");
                editor.commit();
                dialogService.dismiss();
                Intent i = new Intent(ProductListingScreen.this, LoginActivity.class);
                startActivity(i);
                finish();

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

    private void callListingAPI(final String myListingURL, JSONObject jsonObject) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(myListingURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.hide();
                        System.out.println(">>> Listing REsponse : " + response.toString());
                        Gson gson = new Gson(); // Or use new GsonBuilder().create();
                        ModelMyListing myListResponse = gson.fromJson(response.toString(), ModelMyListing.class);
                        if (myListResponse.getResult().equalsIgnoreCase("success")) {
                            myDataList = new ArrayList<>();

                            for (int i = 0; i < myListResponse.getData().size(); i++) {

                                ModelMyListingNew modelMyListingNew = new ModelMyListingNew();
                                modelMyListingNew.setCostumeId(myListResponse.getData().get(i).getCostumeId());
                                modelMyListingNew.setImagePath(myListResponse.getData().get(i).getImage());
                                modelMyListingNew.setImgName(myListResponse.getData().get(i).getName());
                                modelMyListingNew.setPrice(myListResponse.getData().get(i).getPrice());

                                myDataList.add(modelMyListingNew);
                            }

                            if (myDataList.size() > 0) {
                                llIntroList.setVisibility(View.INVISIBLE);
                                llGridList.setVisibility(View.VISIBLE);
                                adapterProductListing = new AdapterProductListing(ProductListingScreen.this, R.layout.row_product_list, myDataList);
                                gridView.setAdapter(adapterProductListing);
                            } else {
                                llIntroList.setVisibility(View.VISIBLE);
                                if (llGridList.getVisibility() == View.VISIBLE) {
                                    llGridList.setVisibility(View.INVISIBLE);
                                }
                            }

                        } else {
                            //Toast.makeText(ProductListingScreen.this, "Something went wrong, please try again later.", Toast.LENGTH_SHORT).show();
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
    }

    private void showWelcomeDialog() {
        dialogService = new Dialog(ProductListingScreen.this);
        dialogService.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogService.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogService.setContentView(R.layout.dialog_got_it);
        dialogService.setCancelable(false);
        TextView tvName = dialogService.findViewById(R.id.tvGotIt);

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogService.dismiss();
                adapterProductListing = new AdapterProductListing(ProductListingScreen.this, R.layout.row_product_list, myDataList);
                gridView.setAdapter(adapterProductListing);
            }
        });

        WindowManager.LayoutParams wmlp = dialogService.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_HORIZONTAL;
        dialogService.show();
        dialogService.getWindow().setLayout((int) ((screenWidth / 5) * 4.2), (screenHeight / 12) * 5);

    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(ProductListingScreen.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || (ActivityCompat.checkSelfPermission(ProductListingScreen.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(ProductListingScreen.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ProductListingScreen.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ProductListingScreen.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ProductListingScreen.this, permissionsRequired[2])) {
                //ActivityCompat.requestPermissions(ProductListingScreen.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProductListingScreen.this);
                builder.setTitle(ProductListingScreen.this.getResources().getString(R.string.Permissionheader));
                builder.setMessage(ProductListingScreen.this.getResources().getString(R.string.storagePermission));
                builder.setPositiveButton(ProductListingScreen.this.getResources().getString(R.string.grantpermission), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ProductListingScreen.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton(ProductListingScreen.this.getResources().getString(R.string.cancelpermission), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProductListingScreen.this);
                builder.setTitle(ProductListingScreen.this.getResources().getString(R.string.Permissionheader));
                builder.setMessage(ProductListingScreen.this.getResources().getString(R.string.storagePermission));
                builder.setPositiveButton(ProductListingScreen.this.getResources().getString(R.string.grantpermission), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton(ProductListingScreen.this.getResources().getString(R.string.cancelpermission), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(ProductListingScreen.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            proceedAfterPermission();
        }
    }

    private void proceedAfterPermission() {
        Config.MODE = Config.ADD_MODE;
        //Intent i = new Intent(ProductListingScreen.this,AddProductsActivity.class);
        Intent i = new Intent(ProductListingScreen.this, AddProductsActivityNew.class);
        startActivity(i);
        // finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application");
        alertDialogBuilder
                .setMessage("Are you sure, you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                moveTaskToBack(true);
                                finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(0);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
