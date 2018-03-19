package com.chrysalis.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.androidquery.AQuery;
import com.chrysalis.R;
import com.chrysalis.adapter.ProductListAdapter;
import com.chrysalis.domain.AndroidMultiPartEntity;
import com.chrysalis.domain.AppPreference;
import com.chrysalis.domain.Config;
import com.chrysalis.domain.ConnectionDetector;
import com.chrysalis.domain.UploadStuff;
import com.chrysalis.domain.Utility;
import com.chrysalis.domain.api.APIRequest;
import com.chrysalis.model.ModelAddImages;
import com.chrysalis.model.ModelImgScreenEdit;
import com.chrysalis.model.ModelProductListing;
import com.chrysalis.model.ProductModelClass;
import com.chrysalis.model.deleteimage.DeleteImageResponse;
import com.chrysalis.model.editcostume.EditCostumeResult;
import com.chrysalis.model.editcostume.ImgScreen;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import io.realm.Realm;
import io.realm.RealmList;

import static com.chrysalis.domain.Config.BASE_URL;

public class AddProductsActivityNew extends AppCompatActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    public static ArrayList<String> arrImgPaths = new ArrayList<>(); // used just for hide & visible new imageview
    //public static String  strFrontPath = "",strBackPath = "",strAddOne = "";
    private final int SELECT_PHOTO = 1;
    private final int IMG_SELECT_PHOTO = 21;
    GridView gridView;
    /////////////////////////////////////
    RecyclerView recyclerView;
    GridLayoutManager lm;
    ProductListAdapter adapter;
    ArrayList<ProductModelClass> list;
    int i = 0;
    String strImgId = "";
    int screenWidth, screenHeight;
    TextView tvCancel;
    Button btnNext;
    ArrayList<ModelProductListing> arrProductsPage;
    ImageView ivFrontView, ivBackView, ivaddone, ivaddtwo, ivaddthree, ivaddfour, ivaddFive, ivaddSix, ivaddSeven, ivaddEight;
    Boolean idCamera = false;
    Boolean idBackView = false;
    Boolean idAddone = false;
    Boolean boolAddTwo = false, boolAddThree = false, boolAddFour = false, boolAddFive = false, boolAddSix = false, boolAddSeven = false, boolAddEight = false;
    String filePathIdCard = "", filePathBackView = "", fileAddonePath = "", fileAddTwo = "", fileAddThree = "", fileAddFour = "", fileAddFive = "", fileAddSix = "", fileAddSeven = "", fileAddEight = "";
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog pSignIn;
    AQuery aQuery;
    HashMap<Integer, String> hm = new HashMap<Integer, String>();
    // String strReceivedFrontViewImg = "", strReceivedBackViewImg = "",strReceivedAddOneImg = "";
    String strReceivedOne = "", strReceivedTwo = "", strReceivedThree = "", strReceivedFour = "", strReceivedfive = "", strReceivedSix = "", strReceivedSeven = "", strReceivedEight = "", strReceivedNine = "", strReceivedTen = "";
    AppPreference appPreference;
    //public  ArrayList<String> arrImgsEditPath = new ArrayList<>() ;
    //public static ArrayList<String> arrFrontBack = new ArrayList<>()  ;
    LinearLayout ll1, ll2, ll3, ll4, ll5;
    LinearLayout ll2_img2, ll3_img1, ll3_img2, ll4_img1, ll4_img2, ll5_img1, ll5_img2;
    ImageView ivCloseThree, ivCloseFour, ivCloseFive, ivCloseSix, ivCloseSeven, ivCloseEight, ivCloseNine, ivCloseTen;
    String strImgIdOne = "", strImgIdTwo = "", strImgIdThree = "", strImgIdFour = "", strImgIdFive = "", strImgIdSix = "", strImgIdSeven = "", strImgIdEight = "", strImgIdNine = "", strImgIdTen = "";
    ImageView ivCloseOneFront, ivCloseTwoBack;
    ProgressDialog p;
    String strTotalCount = "";
    Boolean boolOne = false, boolTwo = false, boolThree = false, boolFour = false, boolFive = false, boolSix = false, boolSeven = false, boolEight = false, boolNine = false, boolTen = false;
    String[] permissionsRequired = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    ArrayList<ModelAddImages> arrImgsModelPath = new ArrayList<>();
    ArrayList<ModelImgScreenEdit> arrReceivedIMgs;
    String editFront = "", editBack = "";
    boolean isFrontViewPresent = false, isBackViewPresent = false;
    boolean isEditFrontPresent = false, isEditBackPresent = false;
    int selectedPosition;
    SharedPreferences sp, permissionStatus;
    private Uri fileUriId;
    private boolean sentToSettings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_new);
        sp = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        permissionStatus = this.getSharedPreferences("MyPref", MODE_PRIVATE);
        System.out.println("***********AddProductsActivity new**********");
        //Toast.makeText(this, "Add Products activity new", Toast.LENGTH_SHORT).show();

        init();
        //checkPermission();

        if (Config.MODE.equalsIgnoreCase(Config.EDIT_MODE)) {
            if (isInternetPresent) {
                getCostumeEditData();
            } else {
                Toast.makeText(this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
            }
        } else {
            list.clear();
            System.out.println("***********else mode**********" + Config.MODE);
            ProductModelClass data = new ProductModelClass();
            data.setImgName("Front View *");
            data.setTempName("front");
            data.setImgId("");
            data.setImg("");
            list.add(data);

            ProductModelClass data2 = new ProductModelClass();
            data2.setImgName("Back View *");
            data2.setTempName("back");
            data2.setImg("");
            data.setImgId("");
            list.add(data2);

            ProductModelClass data3 = new ProductModelClass();
            data3.setImgName("Additional");
            data3.setTempName("add1");
            data3.setImg("");
            data.setImgId("");
            list.add(data3);

            adapter = new ProductListAdapter(this, list);
            recyclerView.setAdapter(adapter);
        }

    }

    private void setFrontBackImgTrue() {
        isFrontViewPresent = true;
        isBackViewPresent = true;
    }

    @Nullable
    private List<ModelAddImages> checkIsImagesPresentInGSON() {
        List<ModelAddImages> arrSavedList = new ArrayList<>();
        String strSavedImages = appPreference.getPhotoModel();
        Gson gson = new Gson();
        Type type = new TypeToken<List<ModelAddImages>>() {
        }.getType();
        arrSavedList = gson.fromJson(strSavedImages, type);

        if (arrSavedList != null && arrSavedList.size() > 0) {
            System.out.println(">>> existing data is : " + arrSavedList.size());
            for (int i = 0; i < arrSavedList.size(); i++) {
                System.out.println(">> ---- :" + arrSavedList.get(i).getImgName() + "--" + arrSavedList.get(i).getImgPath());

            }
        }
        System.out.println(">>> outside : " + arrSavedList);
        return arrSavedList;
    }

    private void getCostumeEditData() {
        if (Utility.isNetworkAvailable(AddProductsActivityNew.this)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("costume_id", appPreference.getCostumeId());
                String CategoryURL = Config.BASE_URL + "getAllDataForEdit";

                new APIRequest(AddProductsActivityNew.this, jsonObject, CategoryURL, Config.API_EDIT_COSTUME, Config.POST);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void getEditData(EditCostumeResult editCostumeResult) {

        System.out.println(" >> Result in get Edit Data : " + editCostumeResult);

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        editCostumeResult.setId(appPreference.getCostumeId());
        realm.copyToRealmOrUpdate(editCostumeResult);
        realm.commitTransaction();

        RealmList<ImgScreen> arrEditImages = editCostumeResult.getData().getImgScreen();
        arrReceivedIMgs = new ArrayList<>();

        for (int i = 0; i < arrEditImages.size(); i++) {
            // ModelAddImages modelEditImages = new ModelAddImages();            // for adding the editing data
            ModelImgScreenEdit modelEdit = new ModelImgScreenEdit();

            modelEdit.setId(arrEditImages.get(i).getId());
            modelEdit.setImgPath(arrEditImages.get(i).getImage());
            modelEdit.setImgType(arrEditImages.get(i).getType());

            System.out.println("******************" + arrEditImages.get(i).getId() + "**" + arrEditImages.get(i).getImage() + "**" +
                    arrEditImages.get(i).getType());
            arrImgPaths.add(arrEditImages.get(i).getImage());
            arrReceivedIMgs.add(modelEdit);
        }

        list.clear();
        for (int i = 0; i < arrReceivedIMgs.size(); i++) {
            //System.out.println(">**************>" + mSelectedList.get(i).getImgName() + "***" + mSelectedList.get(i).getImgPath());
            ProductModelClass data3 = new ProductModelClass();
            if (arrEditImages.get(i).getType().equalsIgnoreCase("1")) {
                isFrontViewPresent = true;
                data3.setImgName("Front View *");
                data3.setTempName("front");
            } else if (arrEditImages.get(i).getType().equalsIgnoreCase("2")) {
                isBackViewPresent = true;
                data3.setImgName("Back View *");
                data3.setTempName("back");
            } else {
                int pos = i - 1;
                String s = String.valueOf(pos);
                data3.setImgName("Additional");
                data3.setTempName("add" + s);
            }
            data3.setImg(arrReceivedIMgs.get(i).getImgPath());
            data3.setImgId(arrReceivedIMgs.get(i).getId());
            list.add(data3);

        }

        for (int k = 0; k < list.size(); k++) {
            System.out.println("*********list idsss*********" + list.get(k).getImgId());
        }

        if (list.size() != 10) {
            ProductModelClass data = new ProductModelClass();
            data.setImgName("Additional");
            int i = list.size() - 1;
            data.setImgId("");
            data.setTempName("add" + i);
            data.setImg("");
            list.add(data);

        }

       /* for(int i = 0; i < list.size(); i++)
        {
            ModelAddImages detail = new ModelAddImages();
            detail.setImgName(list.get(i).getImgName());
            //detail.setTempName(mSelectedList.get(i).getImgName());
            detail.setImgPath(list.get(i).getImg());
            arrImgsModelPath.add(detail);
        }*/

        ///ArrayList<ProductModelClass> saveList = new ArrayList<>();

        adapter = new ProductListAdapter(this, list);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

       /* if (arrReceivedIMgs.size() == 2) {
            setFrontBackImgTrue();

            ivCloseOneFront.setVisibility(View.VISIBLE);
            ivCloseTwoBack.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivityNew.this).load(arrReceivedIMgs.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivityNew.this).load(arrReceivedIMgs.get(1).getImgPath()).into(ivBackView);

            strImgIdOne = arrReceivedIMgs.get(0).getId();
            strImgIdTwo = arrReceivedIMgs.get(1).getId();

        }*/

    }

    private void init() {
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        lm = new GridLayoutManager(this, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lm);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                selectedPosition = position;

                final TextView txt = view.findViewById(R.id.txtName);
                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(AddProductsActivityNew.this, txt.getText()+" clicked--"+position, Toast.LENGTH_LONG).show();
                    }
                });

                final ImageView img = view.findViewById(R.id.ivImgView);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(AddProductsActivityNew.this, " clicked", Toast.LENGTH_LONG).show();
                        checkPermission();
/////////////////////////////////////////////////////
                       /* int MyVersion = Build.VERSION.SDK_INT;
                        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                            System.out.println("***********version*********" + MyVersion);

                        }*/

                        ////////////////////////////////////////

                        /*System.out.println("*****on click******" + list.size() + "****" + selectedPosition + "***" + arrImgsModelPath.size());
                        System.out.println("*****list click******" + list.get(selectedPosition).getImg());
                        if (list.get(selectedPosition).getImg().equalsIgnoreCase("")) {
                            if (selectedPosition == 0 || selectedPosition == 1) {
                                selectFrontImage();
                            } else {

                                if (isFrontViewPresent && isBackViewPresent) {
                                    if (list.size() < 10) {
                                        ProductModelClass data = new ProductModelClass();
                                        data.setImgName("Additional");
                                        int pos = selectedPosition - 1;
                                        String s = String.valueOf(pos);
                                        data.setTempName("add" + s);
                                        data.setImg("");
                                        data.setImgId("");
                                        list.add(data);
                                        adapter.notifyDataSetChanged();
                                    }

                                    if (selectedPosition == 2 || selectedPosition == 3 || selectedPosition == 4 || selectedPosition == 5
                                            || selectedPosition == 6 || selectedPosition == 7 || selectedPosition == 8
                                            || selectedPosition == 9 || selectedPosition == 10) {
                                        selectFrontImage();
                                    }
                                } else {
                                    Toast.makeText(AddProductsActivityNew.this, "Please select front view & back view images first ", Toast.LENGTH_SHORT).show();

                                }

                            }
                        }*/

                    }

                });

                final ImageView imgDelete = view.findViewById(R.id.ivCloseOneFront);
                imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ProductModelClass detail = new ProductModelClass();
                        if (selectedPosition == 0) {
                            isFrontViewPresent = false;

                            detail.setImgName("Front View *");
                            detail.setTempName("front");
                            detail.setImg("");
                            detail.setImgId("");
                            list.set(selectedPosition, detail);
                            adapter.notifyDataSetChanged();

                        } else if (selectedPosition == 1) {
                            isBackViewPresent = false;

                            detail.setImgName("Back View *");
                            detail.setTempName("back");
                            detail.setImg("");
                            detail.setImgId("");
                            list.set(selectedPosition, detail);
                            adapter.notifyDataSetChanged();

                        } else {
                            System.out.println("****else dele****" + selectedPosition + "****" + arrImgsModelPath.size());

                            System.out.println("*******else del******" + selectedPosition + "**" + list.size() + "****" + list.get(selectedPosition).getImgId() + "****" + list.get(selectedPosition).getTempName()
                                    + "****" + list.get(selectedPosition).getImgId());

                            if (arrImgsModelPath.size() == 0) {
                                System.out.println("*******del if******" + list.get(selectedPosition).getImgId());
                                strImgId = list.get(selectedPosition).getImgId();
                                callDeleteImageAPI(strImgId);
                            } else {

                                if (list.get(selectedPosition).getImgId().equalsIgnoreCase("")) {
                                    System.out.println("*******del else aboveif******");
                                    for (int i = 0; i < arrImgsModelPath.size(); i++) {
                                        System.out.println("*******del if for******" + list.get(selectedPosition).getImgId() + "**********" + list.get(selectedPosition).getTempName() + "**" + arrImgsModelPath.get(i).getImgName());
                                        if (list.get(selectedPosition).getTempName().equalsIgnoreCase(arrImgsModelPath.get(i).getImgName())) {
                                            System.out.println("*******arr img******");
                                            arrImgsModelPath.remove(i);
                                            if (selectedPosition == 9) {
                                                System.out.println("***********if 99999");
                                                ProductModelClass data = new ProductModelClass();
                                                data.setImgName("Additional");
                                                //int i = list.size() - 1;
                                                data.setTempName("add" + selectedPosition);
                                                data.setImg("");
                                                data.setImgId("");
                                                list.set(selectedPosition, data);
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                System.out.println("***********else remove");
                                                list.remove(selectedPosition);
                                                adapter.notifyDataSetChanged();
                                            }

                                            if (list.size() != 10) {
                                                ProductModelClass data = new ProductModelClass();
                                                data.setImgName("Additional");
                                                int k = list.size() - 1;
                                                data.setTempName("add" + k);
                                                data.setImg("");
                                                data.setImgId("");
                                                if (list.get(list.size() - 1).getImg().equalsIgnoreCase("")) {
                                                    System.out.println("******if yes****");
                                                } else {
                                                    System.out.println("******if no****");
                                                    list.add(data);
                                                }

                                                adapter = new ProductListAdapter(AddProductsActivityNew.this, list);
                                                adapter.notifyDataSetChanged();
                                                recyclerView.setAdapter(adapter);

                                            }

                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }

                                    ///////////////////////////////////////////

                                } else {
                                    System.out.println("*******del else for******");
                                    strImgId = list.get(selectedPosition).getImgId();
                                    callDeleteImageAPI(strImgId);
                                }

                            }

                        }

                    }
                });

            }

            @Override
            public void onLongClick(View view, int position) {
                // Toast.makeText(AddProductsActivityNew.this, "Long press on position :" + position, Toast.LENGTH_LONG).show();
            }
        }));

        /////////////////////////////////////////////////////////////////////////

        tvCancel = findViewById(R.id.tvCancel);
        screenWidth = AddProductsActivityNew.this.getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = AddProductsActivityNew.this.getWindowManager().getDefaultDisplay().getHeight();
        btnNext = findViewById(R.id.btnAddProductNext);

        //ivFrontView = (ImageView) findViewById(R.id.ivFrontView);
        //ivBackView = (ImageView) findViewById(R.id.ivBackView);
        appPreference = new AppPreference(AddProductsActivityNew.this);

        //ivCloseFront,ivBackView, ivaddone,ivCloseTwo,ivCloseThree,ivCloseFour,ivCloseFive, ivCloseSix,ivCloseSeven,ivCloseEight;

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        sharedPreferences = this.getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        aQuery = new AQuery(this);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putString("KEY", "empty").commit();
                Intent i = new Intent(AddProductsActivityNew.this, ProductListingScreen.class);
                startActivity(i);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(">>> isFrontViewPresent :" + isFrontViewPresent + "--" + isBackViewPresent);
                if (Config.MODE.equalsIgnoreCase(Config.ADD_MODE)) {
                    if (isFrontViewPresent && isBackViewPresent) {
                        Intent i = new Intent(AddProductsActivityNew.this, AddEditCostumeActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(AddProductsActivityNew.this, "Please select front view & back view images ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (isInternetPresent) {
                        String frontPath = "", backpath = "";
                        System.out.println("# arrImgsModelPath size at edit:" + arrImgsModelPath.size());
                        if (arrImgsModelPath.size() > 0) {
                            for (int i = 0; i < arrImgsModelPath.size(); i++) {
                                String imgName = arrImgsModelPath.get(i).getImgName();
                                System.out.println("# " + arrImgsModelPath.get(i).getImgName() + "-- " + arrImgsModelPath.get(i).getImgPath());
                                if (imgName.equalsIgnoreCase("Front")) {
                                    frontPath = arrImgsModelPath.get(i).getImgPath();
                                    appPreference.setFront(frontPath);
                                    isEditFrontPresent = true;
                                }
                                if (imgName.equalsIgnoreCase("Back")) {
                                    backpath = arrImgsModelPath.get(i).getImgPath();
                                    appPreference.setBack(backpath);
                                    isEditBackPresent = true;
                                }
                                new AddProductsActivityNew.callUpdateImagesAPI().execute("");
                            }

                        } else {
                            if (isFrontViewPresent && isBackViewPresent) {
                                Intent i = new Intent(AddProductsActivityNew.this, AddEditCostumeActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(AddProductsActivityNew.this, "Please select front view & back view images ", Toast.LENGTH_SHORT).show();
                            }
                            //finish();
                        }
                    } else {
                        Toast.makeText(AddProductsActivityNew.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                    }
                }

                System.out.println(">> arrImgsItems-size- :" + arrImgsModelPath.size());
                System.out.println(" >> -------------------------------------------");
                for (int i = 0; i < arrImgsModelPath.size(); i++) {
                    System.out.println(arrImgsModelPath.get(i).getImgName() + "--> " + arrImgsModelPath.get(i).getImgPath());
                }
                Gson gson = new Gson();
                String json = gson.toJson(arrImgsModelPath);
                appPreference.setPhotoModel(json);
            }
        });

    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(AddProductsActivityNew.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || (ActivityCompat.checkSelfPermission(AddProductsActivityNew.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(AddProductsActivityNew.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddProductsActivityNew.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(AddProductsActivityNew.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(AddProductsActivityNew.this, permissionsRequired[2])) {
                //ActivityCompat.requestPermissions(ProductListingScreen.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AddProductsActivityNew.this);
                builder.setTitle(AddProductsActivityNew.this.getResources().getString(R.string.Permissionheader));
                builder.setMessage(AddProductsActivityNew.this.getResources().getString(R.string.storagePermission));
                builder.setPositiveButton(AddProductsActivityNew.this.getResources().getString(R.string.grantpermission), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(AddProductsActivityNew.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton(AddProductsActivityNew.this.getResources().getString(R.string.cancelpermission), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AddProductsActivityNew.this);
                builder.setTitle(AddProductsActivityNew.this.getResources().getString(R.string.Permissionheader));
                builder.setMessage(AddProductsActivityNew.this.getResources().getString(R.string.storagePermission));
                builder.setPositiveButton(AddProductsActivityNew.this.getResources().getString(R.string.grantpermission), new DialogInterface.OnClickListener() {
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
                builder.setNegativeButton(AddProductsActivityNew.this.getResources().getString(R.string.cancelpermission), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(AddProductsActivityNew.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            proceedAfterPermission();
        }
    }

    private void callDeleteImageAPI(String strImg) {
        String deleteURL = BASE_URL + "deleteImages";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("costume_id", appPreference.getCostumeId());
            jsonObject.put("image_id", strImg);

            System.out.println(">>> my Dleted Params : " + jsonObject);
            new APIRequest(AddProductsActivityNew.this, jsonObject, deleteURL, Config.API_DELETE_IMAGE, Config.POST);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void deleteImageResponse(DeleteImageResponse deleteImageResponse) {
        if (deleteImageResponse.getResult().equalsIgnoreCase("success")) {
            Toast.makeText(AddProductsActivityNew.this, deleteImageResponse.getMessage(), Toast.LENGTH_SHORT).show();
            editor.putString("total_count", deleteImageResponse.getData().getTotalCount().toString());
            editor.commit();

            list.remove(selectedPosition);

            if (list.size() != 10) {
                ProductModelClass data = new ProductModelClass();
                data.setImgName("Additional");
                int k = list.size() - 1;
                data.setTempName("add" + k);
                data.setImg("");
                data.setImgId("");
                if (list.get(list.size() - 1).getImg().equalsIgnoreCase("")) {
                    System.out.println("******if yes****");
                } else {
                    System.out.println("******if no****");
                    list.add(data);
                }

                adapter = new ProductListAdapter(AddProductsActivityNew.this, list);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

            }

            adapter.notifyDataSetChanged();
            //Intent i = new Intent(AddProductsActivityNew.this, AddProductsActivityNew.class);
            //startActivity(i);
        } else {
            Toast.makeText(AddProductsActivityNew.this, deleteImageResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddProductsActivityNew.this, ProductListingScreen.class);
        startActivity(i);
        finish();

    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(AddProductsActivityNew.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(AddProductsActivityNew.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddProductsActivityNew.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(AddProductsActivityNew.this, permissionsRequired[1])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivityNew.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and External permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(AddProductsActivityNew.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (sharedPreferences.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivityNew.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(AddProductsActivityNew.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(AddProductsActivityNew.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(AddProductsActivityNew.this, permissionsRequired[1])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivityNew.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(AddProductsActivityNew.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void proceedAfterPermission() {
        System.out.println("*****on click******" + list.size() + "****" + selectedPosition + "***" + arrImgsModelPath.size());
        System.out.println("*****list click******" + list.get(selectedPosition).getImg());
        if (list.get(selectedPosition).getImg().equalsIgnoreCase("")) {
            if (selectedPosition == 0 || selectedPosition == 1) {
                selectFrontImage();
            } else {

                if (isFrontViewPresent && isBackViewPresent) {
                    if (list.size() < 10) {
                        ProductModelClass data = new ProductModelClass();
                        data.setImgName("Additional");
                        int pos = selectedPosition - 1;
                        String s = String.valueOf(pos);
                        data.setTempName("add" + s);
                        data.setImg("");
                        data.setImgId("");
                        list.add(data);
                        adapter.notifyDataSetChanged();
                    }

                    if (selectedPosition == 2 || selectedPosition == 3 || selectedPosition == 4 || selectedPosition == 5
                            || selectedPosition == 6 || selectedPosition == 7 || selectedPosition == 8
                            || selectedPosition == 9 || selectedPosition == 10) {
                        selectFrontImage();
                    }
                } else {
                    Toast.makeText(AddProductsActivityNew.this, "Please select front view & back view images first ", Toast.LENGTH_SHORT).show();

                }

            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(AddProductsActivityNew.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
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

    private void addImagePathToModel(String newPath, String imgName) {
        Config.flagImgPresent = true;
        boolean isAlready = false;
        for (int i = 0; i < arrImgsModelPath.size(); i++) {
            String name = arrImgsModelPath.get(i).getImgName();
            System.out.println("*******is already for****" + isAlready + "****" + imgName + "******&&" + name);

            if (imgName.equals(name)) {
                isAlready = true;
                arrImgsModelPath.get(i).setImgPath(newPath);
                break;
            }
        }
        if (!isAlready) {
            System.out.println("*******is already****" + isAlready + "****" + imgName);
            ModelAddImages modelAddImg = new ModelAddImages();
            modelAddImg.setImgPath(newPath);
            modelAddImg.setImgName(imgName);
            arrImgsModelPath.add(modelAddImg);
        }
        System.out.println(">> In Menthod -- size" + arrImgsModelPath.size());
        for (int i = 0; i < arrImgsModelPath.size(); i++) {
            System.out.println(">> Inmethod  " + arrImgsModelPath.get(i).getImgName().toString() + " --> " + arrImgsModelPath.get(i).getImgPath().toString());
        }
        Gson gson = new Gson();

        //ArrayList<ListAdapterItemsSelected> mSelectedList = new ArrayList<ListAdapterItemsSelected>();
        String jsonString = gson.toJson(arrImgsModelPath);

//Save to SharedPreferences
        sp.edit().putString("KEY", jsonString).commit();

//Get to SharedPreferences

//For default value, just to get no errors while getting no value from the SharedPreferences
        String empty_list = gson.toJson(new ArrayList<ModelAddImages>());

        ArrayList<ModelAddImages> mSelectedList = gson.fromJson(sp.getString("KEY", empty_list),
                new TypeToken<ArrayList<ModelAddImages>>() {
                }.getType());

        for (int i = 0; i < mSelectedList.size(); i++) {
            System.out.println(">**************>" + arrImgsModelPath.get(i).getImgName());
        }

    }

    private String getResizedImagePath(String testPath, String strName) {
        String resizedPath = "";
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Bitmap b = BitmapFactory.decodeFile(testPath);

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        Bitmap out = Bitmap.createScaledBitmap(b, 750, 750, false);
        File file = new File(dir, strName + "_" + timeStamp + "_" + "resize.png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            resizedPath = file.getAbsolutePath();
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(">> Resized image exception :" + e);
        }
        return resizedPath;
    }

    private Intent getAvairyImageIntent(String imgpath) {
        Uri imageUri = null;
        imageUri = Uri.parse(imgpath);
        return new AdobeImageIntent.Builder(this)
                .setData(imageUri)
                .build();
    }

    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    private void selectFrontImage() {

        final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivityNew.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {
                    idCamera = true;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUriId = UploadStuff.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriId);
                    startActivityForResult(intent, SELECT_PHOTO);

                } else if (options[item].equals("Gallery")) {
                    idCamera = false;
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(AddProductsActivityNew.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
            if (ActivityCompat.checkSelfPermission(AddProductsActivityNew.this, permissionsRequired[1]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (idCamera) {
                        filePathIdCard = fileUriId.getPath();
                        if (!filePathIdCard.isEmpty() && filePathIdCard != null) {
                            // Uri selectedImageUri = data.getData();
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 8;
                            final Bitmap bitmap = BitmapFactory.decodeFile(filePathIdCard, options);
                            // ivFrontView.setImageBitmap(bitmap);
                        }
                    } else {
                        //if idCard pic  selected from Gallery
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            String imagepath = getPath(selectedImageUri);
                            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                            //   ivFrontView.setImageBitmap(bitmap);
                            filePathIdCard = imagepath;
                        }
                    }
                    if (!filePathIdCard.equals("")) {
                        System.out.println(">> In filePathIdCard is null ");
                        Intent imageEditorIntent = getAvairyImageIntent(filePathIdCard);
                        if (imageEditorIntent != null) {
                            System.out.println(">>> imageEditorIntent :" + imageEditorIntent);
                            startActivityForResult(imageEditorIntent, IMG_SELECT_PHOTO);
                        }
                    }
                    break;
                }

            case IMG_SELECT_PHOTO:
                Uri editedImageUri = null;
                if (AdobeImageIntent.EXTRA_OUTPUT_URI != null && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals("null")) {
                    // For chekcking front view is set or not
                    if (data != null) {
                        editedImageUri = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
                        System.out.println(">>>> editedImageUri :" + editedImageUri);
                        if (editedImageUri != null) {
                            //isFrontViewPresent = true;
                            //ivFrontView.setImageURI(editedImageUri);
                            //ivCloseOneFront.setVisibility(View.VISIBLE);
                            String newPath = null;
                            ProductModelClass detail = new ProductModelClass();

                            if (list.get(selectedPosition).getImgName().equalsIgnoreCase("front view *")) {
                                isFrontViewPresent = true;
                                newPath = getResizedImagePath(getPath(editedImageUri), "front");
                                detail.setImgName("Front View *");
                                detail.setTempName("front");
                                detail.setImg(newPath);
                                detail.setImgId("");
                                list.set(selectedPosition, detail);
                                adapter.notifyDataSetChanged();

                                addImagePathToModel(newPath, "Front");

                            } else if (list.get(selectedPosition).getImgName().equalsIgnoreCase("back view *")) {
                                isBackViewPresent = true;
                                newPath = getResizedImagePath(getPath(editedImageUri), "back");
                                detail.setImgName("Back View *");
                                detail.setTempName("back");
                                detail.setImg(newPath);
                                detail.setImgId("");
                                list.set(selectedPosition, detail);
                                adapter.notifyDataSetChanged();

                                addImagePathToModel(newPath, "Back");

                            } else {

                                i++;

                                newPath = getResizedImagePath(getPath(editedImageUri), "additional");
                                detail.setImgName("Additional");
                                int pos = selectedPosition - 1;
                                String s = String.valueOf(pos);
                                detail.setTempName("add" + i);
                                detail.setImg(newPath);
                                detail.setImgId("");
                                list.set(selectedPosition, detail);
                                adapter.notifyDataSetChanged();
                                System.out.println("************add**************" + "add" + i);
                                addImagePathToModel(newPath, "Add" + i);

                            }

                        }

                    }
                }

                break;

        }
    }

    @Override
    protected void onResume() {
        System.out.println("****onResume***" + sp.getString("KEY", ""));
        super.onResume();
        Gson gson = new Gson();
        String empty_list = gson.toJson(new ArrayList<ModelAddImages>());

        if (!Config.flagImgPresent) {
            if (!sp.getString("KEY", "").equalsIgnoreCase("empty")) {
                list.clear();
                ArrayList<ModelAddImages> mSelectedList = gson.fromJson(sp.getString("KEY", empty_list),
                        new TypeToken<ArrayList<ModelAddImages>>() {
                        }.getType());

                for (int i = 0; i < mSelectedList.size(); i++) {
                    System.out.println(">**************>" + mSelectedList.get(i).getImgName() + "***" + mSelectedList.get(i).getImgPath());
                    ProductModelClass data3 = new ProductModelClass();
                    if (mSelectedList.get(i).getImgName().equalsIgnoreCase("front")) {
                        data3.setImgName("Front View *");
                    } else if (mSelectedList.get(i).getImgName().equalsIgnoreCase("back")) {
                        data3.setImgName("Back View *");
                    } else {
                        data3.setImgName("Additional");
                    }

                    data3.setTempName(mSelectedList.get(i).getImgName());
                    data3.setImg(mSelectedList.get(i).getImgPath());
                    data3.setImgId("");
                    list.add(data3);

                }

                if (list.size() != 10) {
                    ProductModelClass data = new ProductModelClass();
                    data.setImgName("Additional");
                    int i = list.size() - 1;
                    data.setTempName("add" + i);
                    data.setImg("");
                    data.setImgId("");
                    list.add(data);

                }

                for (int i = 0; i < mSelectedList.size(); i++) {
                    ModelAddImages detail = new ModelAddImages();
                    detail.setImgName(mSelectedList.get(i).getImgName());
                    //detail.setTempName(mSelectedList.get(i).getImgName());
                    detail.setImgPath(mSelectedList.get(i).getImgPath());
                    arrImgsModelPath.add(detail);
                }

                ///ArrayList<ProductModelClass> saveList = new ArrayList<>();

                adapter = new ProductListAdapter(this, list);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

                //}
            }

        }
    }

    class callUpdateImagesAPI extends AsyncTask<String, Void, String> {
        ProgressDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(AddProductsActivityNew.this);
            p.setMessage("In Progress..");
            p.setCanceledOnTouchOutside(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseString = null;
            org.apache.http.client.HttpClient httpclient = new org.apache.http.impl.client.DefaultHttpClient();
            // org.apache.http.client.methods.HttpPost httppost = new org.apache.http.client.methods.HttpPost(Config.BASE_URL+"uploadImageMultiple");
            org.apache.http.client.methods.HttpPost httppost = new org.apache.http.client.methods.HttpPost(Config.BASE_URL + "updateImagesNew");
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                //publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                if (isEditFrontPresent) {
                    System.out.println("# FrontPath in edit flag :" + editFront);
                    File sourceFile1 = new File(appPreference.getFront());
                    entity.addPart("front", new FileBody(sourceFile1));
                    entity.addPart("type1", new StringBody("1"));
                }
                if (isEditBackPresent) {
                    System.out.println("# BackPath in edit flag :" + editBack);
                    File sourceFile2 = new File(appPreference.getBack());
                    entity.addPart("back", new FileBody(sourceFile2));
                    entity.addPart("type2", new StringBody("2"));
                }

                for (int i = 0; i < arrImgsModelPath.size(); i++) {
                    String imgName = arrImgsModelPath.get(i).getImgName();
                    if (imgName.equalsIgnoreCase("Front")) {
                        arrImgsModelPath.remove(i);
                    }
                    if (imgName.equalsIgnoreCase("Back")) {
                        arrImgsModelPath.remove(i);
                    }
                }

                if (arrImgsModelPath.size() > 0) {
                    //For getting the additional pic index for seeting the API call
                    String myCount = sharedPreferences.getString("total_count", "");
                    if (myCount.isEmpty()) {
                        // removing the front path from list
                        System.out.println(">>> arrReceivedIMgs size : " + arrReceivedIMgs.size());
                        if (arrReceivedIMgs.size() > 0) {
                            int receivedPicSize = arrReceivedIMgs.size() - 2;
                            strTotalCount = String.valueOf(receivedPicSize);
                        }
                    } else {
                        System.out.println(">>> Already get count");
                        strTotalCount = myCount;
                    }

                    System.out.println(">>> Outside  strTotalCount :" + strTotalCount);
                    ArrayList<ModelAddImages> arrUpdatedList = new ArrayList<>();

                    for (int i = 0; i < Integer.parseInt(strTotalCount); i++) {
                        ModelAddImages modelAddImages = new ModelAddImages();
                        modelAddImages.setImgName("ImgName" + i);
                        modelAddImages.setImgId("ImgId" + i);
                        modelAddImages.setImgType("ImgType" + i);
                        modelAddImages.setImgPath("ImgPath" + i);

                        arrUpdatedList.add(modelAddImages);
                    }
                    System.out.println(">> arrUpdatedList size :" + arrUpdatedList.size());
                    arrUpdatedList.addAll(arrImgsModelPath);

                    System.out.println(">>> After Size added is :" + arrUpdatedList.size());
                    for (int i = Integer.parseInt(strTotalCount); i < arrUpdatedList.size(); i++) {

                        String testPath = arrUpdatedList.get(i).getImgPath().toString().trim();
                        System.out.println("# Edit Img Path in loop is :" + testPath);
                        System.out.println("# pic val is     pic " + i);
                        File sourceFileObj = new File(testPath);
                        entity.addPart("pic" + i, new FileBody(sourceFileObj));
                    }
                }

                entity.addPart("type3", new StringBody("3"));
                entity.addPart("costume_id", new StringBody(appPreference.getCostumeId()));
                entity.addPart("total_count", new StringBody(sharedPreferences.getString("total_count", "")));

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
                System.out.println(">>> Costume Description result : " + s);
                try {

                    JSONObject jsonObject = new JSONObject(s);
                    String resCode = jsonObject.getString("result");
                    if (resCode.equalsIgnoreCase("success")) {

                        editor.remove("total_count");
                        editor.apply();

                        Toast.makeText(AddProductsActivityNew.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AddProductsActivityNew.this, AddEditCostumeActivity.class);
                        startActivity(i);
                        //  finish();
                    } else {
                        String strMessage = jsonObject.getString("message");
                        Toast.makeText(AddProductsActivityNew.this, strMessage, Toast.LENGTH_SHORT).show();
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
