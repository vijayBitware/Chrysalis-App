package com.chrysalis.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.chrysalis.R;
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

/**
 * Created by Bitware Marketing on 24-05-2017.
 */

public class AddProductsActivity extends Activity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    public static ArrayList<String> arrImgPaths = new ArrayList<>(); // used just for hide & visible new imageview
    //public static String  strFrontPath = "",strBackPath = "",strAddOne = "";
    private final int SELECT_PHOTO = 1;
    private final int SELECT_PHOTO1 = 2;
    private final int SELECT_PHOTO3 = 3;
    private final int SELECT_PHOTO12 = 4;
    private final int SELECT_PHOTO13 = 5;
    private final int SELECT_PHOTO14 = 6;
    private final int SELECT_PHOTO15 = 7;
    private final int SELECT_PHOTO16 = 8;
    private final int SELECT_PHOTO17 = 9;
    private final int SELECT_PHOTO18 = 10;
    private final int IMG_SELECT_PHOTO = 21;
    private final int IMG_SELECT_PHOTO1 = 22;
    private final int IMG_SELECT_PHOTO3 = 23;
    private final int IMG_SELECT_PHOTO12 = 24;
    private final int IMG_SELECT_PHOTO13 = 25;
    private final int IMG_SELECT_PHOTO14 = 26;
    private final int IMG_SELECT_PHOTO15 = 27;
    private final int IMG_SELECT_PHOTO16 = 28;
    private final int IMG_SELECT_PHOTO17 = 29;
    private final int IMG_SELECT_PHOTO18 = 30;
    // GridView gridView;
    // RecyclerView rvMainImages;
    Dialog dialogService;
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
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    ArrayList<ModelAddImages> arrImgsModelPath = new ArrayList<>();
    ArrayList<ModelImgScreenEdit> arrReceivedIMgs;
    String editFront = "", editBack = "";
    boolean isFrontViewPresent = false, isBackViewPresent = false;
    boolean isEditFrontPresent = false, isEditBackPresent = false;
    String strSavedImages;
    private Uri fileUriId;
    private boolean sentToSettings = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_products_old_new_way);
        System.out.println("***********AddProductsActivity**********");
        Toast.makeText(this, "Add Products activity old", Toast.LENGTH_SHORT).show();

        inIt();
        checkPermission();

        if (Config.MODE.equalsIgnoreCase(Config.EDIT_MODE)) {
            if (isInternetPresent) {
                getCostumeEditData();
            } else {
                Toast.makeText(this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
            }
        } else {
            System.out.println("*****else add img*****");

            Config.arrSavedImages = checkIsImagesPresentInGSON(); // Check for is aleady images peresent in GSON
            //arrSavedImages
            if (Config.arrSavedImages != null && Config.arrSavedImages.size() > 0) {         // if images added in normal flow & view is refreshed then display those image previously added
                if (Config.arrSavedImages.size() == 2) {

                    ivCloseOneFront.setVisibility(View.VISIBLE);
                    ivCloseTwoBack.setVisibility(View.VISIBLE);

                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(0).getImgPath()).into(ivFrontView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(1).getImgPath()).into(ivBackView);
                    setFrontBackImgTrue();

                } else if (Config.arrSavedImages.size() == 3) {
                    setFrontBackImgTrue();
                    ll2_img2.setVisibility(View.VISIBLE);

                    ivCloseOneFront.setVisibility(View.VISIBLE);
                    ivCloseTwoBack.setVisibility(View.VISIBLE);
                    ivCloseThree.setVisibility(View.VISIBLE);

                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(0).getImgPath()).into(ivFrontView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(1).getImgPath()).into(ivBackView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(2).getImgPath()).into(ivaddone);

                    //aQuery.id(R.id.ivaddone).image(Config.arrSavedImages.get(2).getImgPath());
                } else if (Config.arrSavedImages.size() == 4) {
                    setFrontBackImgTrue();

                    ll2.setVisibility(View.VISIBLE);
                    ll2_img2.setVisibility(View.VISIBLE);
                    ll3.setVisibility(View.VISIBLE);

                    ivCloseOneFront.setVisibility(View.VISIBLE);
                    ivCloseTwoBack.setVisibility(View.VISIBLE);
                    ivCloseThree.setVisibility(View.VISIBLE);
                    ivCloseFour.setVisibility(View.VISIBLE);

                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(0).getImgPath()).into(ivFrontView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(1).getImgPath()).into(ivBackView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(2).getImgPath()).into(ivaddone);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(3).getImgPath()).into(ivaddtwo);

                } else if (Config.arrSavedImages.size() == 5) {
                    setFrontBackImgTrue();

                    ll2.setVisibility(View.VISIBLE);
                    ll2_img2.setVisibility(View.VISIBLE);
                    ll3.setVisibility(View.VISIBLE);
                    ll3_img2.setVisibility(View.VISIBLE);

                    ivCloseOneFront.setVisibility(View.VISIBLE);
                    ivCloseTwoBack.setVisibility(View.VISIBLE);
                    ivCloseThree.setVisibility(View.VISIBLE);
                    ivCloseFour.setVisibility(View.VISIBLE);
                    ivCloseFive.setVisibility(View.VISIBLE);

                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(0).getImgPath()).into(ivFrontView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(1).getImgPath()).into(ivBackView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(2).getImgPath()).into(ivaddone);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(3).getImgPath()).into(ivaddtwo);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(4).getImgPath()).into(ivaddthree);

                } else if (Config.arrSavedImages.size() == 6) {
                    setFrontBackImgTrue();

                    ll2.setVisibility(View.VISIBLE);
                    ll2_img2.setVisibility(View.VISIBLE);
                    ll3.setVisibility(View.VISIBLE);
                    ll3_img2.setVisibility(View.VISIBLE);
                    ll4.setVisibility(View.VISIBLE);

                    ivCloseOneFront.setVisibility(View.VISIBLE);
                    ivCloseTwoBack.setVisibility(View.VISIBLE);
                    ivCloseThree.setVisibility(View.VISIBLE);
                    ivCloseFour.setVisibility(View.VISIBLE);
                    ivCloseFive.setVisibility(View.VISIBLE);
                    ivCloseSix.setVisibility(View.VISIBLE);

                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(0).getImgPath()).into(ivFrontView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(1).getImgPath()).into(ivBackView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(2).getImgPath()).into(ivaddone);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(3).getImgPath()).into(ivaddtwo);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(4).getImgPath()).into(ivaddthree);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(5).getImgPath()).into(ivaddfour);
                } else if (Config.arrSavedImages.size() == 7) {
                    setFrontBackImgTrue();
                    ll2.setVisibility(View.VISIBLE);
                    ll2_img2.setVisibility(View.VISIBLE);
                    ll3.setVisibility(View.VISIBLE);
                    ll3_img2.setVisibility(View.VISIBLE);
                    ll4.setVisibility(View.VISIBLE);
                    ll4_img2.setVisibility(View.VISIBLE);

                    ivCloseOneFront.setVisibility(View.VISIBLE);
                    ivCloseTwoBack.setVisibility(View.VISIBLE);
                    ivCloseThree.setVisibility(View.VISIBLE);
                    ivCloseFour.setVisibility(View.VISIBLE);
                    ivCloseFive.setVisibility(View.VISIBLE);
                    ivCloseSix.setVisibility(View.VISIBLE);
                    ivCloseSeven.setVisibility(View.VISIBLE);

                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(0).getImgPath()).into(ivFrontView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(1).getImgPath()).into(ivBackView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(2).getImgPath()).into(ivaddone);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(3).getImgPath()).into(ivaddtwo);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(4).getImgPath()).into(ivaddthree);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(5).getImgPath()).into(ivaddfour);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(6).getImgPath()).into(ivaddFive);
                } else if (Config.arrSavedImages.size() == 8) {
                    setFrontBackImgTrue();
                    ll2.setVisibility(View.VISIBLE);
                    ll2_img2.setVisibility(View.VISIBLE);
                    ll3.setVisibility(View.VISIBLE);
                    ll3_img2.setVisibility(View.VISIBLE);
                    ll4.setVisibility(View.VISIBLE);
                    ll4_img2.setVisibility(View.VISIBLE);
                    ll5.setVisibility(View.VISIBLE);

                    ivCloseOneFront.setVisibility(View.VISIBLE);
                    ivCloseTwoBack.setVisibility(View.VISIBLE);
                    ivCloseThree.setVisibility(View.VISIBLE);
                    ivCloseFour.setVisibility(View.VISIBLE);
                    ivCloseFive.setVisibility(View.VISIBLE);
                    ivCloseSix.setVisibility(View.VISIBLE);
                    ivCloseSeven.setVisibility(View.VISIBLE);
                    ivCloseEight.setVisibility(View.VISIBLE);

                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(0).getImgPath()).into(ivFrontView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(1).getImgPath()).into(ivBackView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(2).getImgPath()).into(ivaddone);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(3).getImgPath()).into(ivaddtwo);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(4).getImgPath()).into(ivaddthree);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(5).getImgPath()).into(ivaddfour);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(6).getImgPath()).into(ivaddFive);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(7).getImgPath()).into(ivaddSix);
                } else if (Config.arrSavedImages.size() == 9) {
                    setFrontBackImgTrue();

                    ll2.setVisibility(View.VISIBLE);
                    ll2_img2.setVisibility(View.VISIBLE);
                    ll3.setVisibility(View.VISIBLE);
                    ll3_img2.setVisibility(View.VISIBLE);
                    ll4.setVisibility(View.VISIBLE);
                    ll4_img2.setVisibility(View.VISIBLE);
                    ll5.setVisibility(View.VISIBLE);
                    ll5_img2.setVisibility(View.VISIBLE);

                    ivCloseOneFront.setVisibility(View.VISIBLE);
                    ivCloseTwoBack.setVisibility(View.VISIBLE);
                    ivCloseThree.setVisibility(View.VISIBLE);
                    ivCloseFour.setVisibility(View.VISIBLE);
                    ivCloseFive.setVisibility(View.VISIBLE);
                    ivCloseSix.setVisibility(View.VISIBLE);
                    ivCloseSeven.setVisibility(View.VISIBLE);
                    ivCloseEight.setVisibility(View.VISIBLE);
                    ivCloseNine.setVisibility(View.VISIBLE);

                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(0).getImgPath()).into(ivFrontView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(1).getImgPath()).into(ivBackView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(2).getImgPath()).into(ivaddone);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(3).getImgPath()).into(ivaddtwo);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(4).getImgPath()).into(ivaddthree);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(5).getImgPath()).into(ivaddfour);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(6).getImgPath()).into(ivaddFive);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(7).getImgPath()).into(ivaddSix);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(8).getImgPath()).into(ivaddSeven);
                } else if (Config.arrSavedImages.size() >= 10) {

                    setFrontBackImgTrue();

                    ll2.setVisibility(View.VISIBLE);
                    ll2_img2.setVisibility(View.VISIBLE);
                    ll3.setVisibility(View.VISIBLE);
                    ll3_img2.setVisibility(View.VISIBLE);
                    ll4.setVisibility(View.VISIBLE);
                    ll4_img2.setVisibility(View.VISIBLE);
                    ll5.setVisibility(View.VISIBLE);
                    ll5_img2.setVisibility(View.VISIBLE);

                    ivCloseOneFront.setVisibility(View.VISIBLE);
                    ivCloseTwoBack.setVisibility(View.VISIBLE);
                    ivCloseThree.setVisibility(View.VISIBLE);
                    ivCloseFour.setVisibility(View.VISIBLE);
                    ivCloseFive.setVisibility(View.VISIBLE);
                    ivCloseSix.setVisibility(View.VISIBLE);
                    ivCloseSeven.setVisibility(View.VISIBLE);
                    ivCloseEight.setVisibility(View.VISIBLE);
                    ivCloseNine.setVisibility(View.VISIBLE);
                    ivCloseTen.setVisibility(View.VISIBLE);

                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(0).getImgPath()).into(ivFrontView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(1).getImgPath()).into(ivBackView);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(2).getImgPath()).into(ivaddone);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(3).getImgPath()).into(ivaddtwo);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(4).getImgPath()).into(ivaddthree);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(5).getImgPath()).into(ivaddfour);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(6).getImgPath()).into(ivaddFive);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(7).getImgPath()).into(ivaddSix);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(8).getImgPath()).into(ivaddSeven);
                    Glide.with(AddProductsActivity.this).load(Config.arrSavedImages.get(9).getImgPath()).into(ivaddEight);
                }
            }
        }
        // showWelcomeDialog();
    }

    private void setFrontBackImgTrue() {
        isFrontViewPresent = true;
        isBackViewPresent = true;
    }

    @Nullable
    private List<ModelAddImages> checkIsImagesPresentInGSON() {
        List<ModelAddImages> arrSavedList = new ArrayList<>();
        strSavedImages = appPreference.getPhotoModel();
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
        if (Utility.isNetworkAvailable(AddProductsActivity.this)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("costume_id", appPreference.getCostumeId());
                String CategoryURL = Config.BASE_URL + "getAllDataForEdit";

                new APIRequest(AddProductsActivity.this, jsonObject, CategoryURL, Config.API_EDIT_COSTUME, Config.POST);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void getEditData(EditCostumeResult editCostumeResult) {

        System.out.println(" >> Result in get Edit Data : " + editCostumeResult.toString());

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

            arrImgPaths.add(arrEditImages.get(i).getImage());
            arrReceivedIMgs.add(modelEdit);
        }

        if (arrReceivedIMgs.size() == 2) {
            //setFrontBackImgTrue();
            isFrontViewPresent = true;
            isBackViewPresent = true;

            ivCloseOneFront.setVisibility(View.VISIBLE);
            ivCloseTwoBack.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(1).getImgPath()).into(ivBackView);

            strImgIdOne = arrReceivedIMgs.get(0).getId();
            strImgIdTwo = arrReceivedIMgs.get(1).getId();

        } else if (arrReceivedIMgs.size() == 3) {
            setFrontBackImgTrue();
            ll2_img2.setVisibility(View.VISIBLE);

            ivCloseOneFront.setVisibility(View.VISIBLE);
            ivCloseTwoBack.setVisibility(View.VISIBLE);
            ivCloseThree.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(2).getImgPath()).into(ivaddone);

            strImgIdOne = arrReceivedIMgs.get(0).getId();
            strImgIdTwo = arrReceivedIMgs.get(1).getId();
            strImgIdThree = arrReceivedIMgs.get(2).getId();

        } else if (arrReceivedIMgs.size() == 4) {
            setFrontBackImgTrue();
            ll2.setVisibility(View.VISIBLE);
            ll2_img2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);

            ivCloseOneFront.setVisibility(View.VISIBLE);
            ivCloseTwoBack.setVisibility(View.VISIBLE);
            ivCloseThree.setVisibility(View.VISIBLE);
            ivCloseFour.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(2).getImgPath()).into(ivaddone);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(3).getImgPath()).into(ivaddtwo);

            strImgIdOne = arrReceivedIMgs.get(0).getId();
            strImgIdTwo = arrReceivedIMgs.get(1).getId();
            strImgIdThree = arrReceivedIMgs.get(2).getId();
            strImgIdFour = arrReceivedIMgs.get(3).getId();

        } else if (arrReceivedIMgs.size() == 5) {
            setFrontBackImgTrue();
            ll2.setVisibility(View.VISIBLE);
            ll2_img2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll3_img2.setVisibility(View.VISIBLE);

            ivCloseOneFront.setVisibility(View.VISIBLE);
            ivCloseTwoBack.setVisibility(View.VISIBLE);
            ivCloseThree.setVisibility(View.VISIBLE);
            ivCloseFour.setVisibility(View.VISIBLE);
            ivCloseFive.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(2).getImgPath()).into(ivaddone);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(3).getImgPath()).into(ivaddtwo);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(4).getImgPath()).into(ivaddthree);

            strImgIdOne = arrReceivedIMgs.get(0).getId();
            strImgIdTwo = arrReceivedIMgs.get(1).getId();
            strImgIdThree = arrReceivedIMgs.get(2).getId();
            strImgIdFour = arrReceivedIMgs.get(3).getId();
            strImgIdFive = arrReceivedIMgs.get(4).getId();

        } else if (arrReceivedIMgs.size() == 6) {
            setFrontBackImgTrue();
            ll2.setVisibility(View.VISIBLE);
            ll2_img2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll3_img2.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);

            ivCloseOneFront.setVisibility(View.VISIBLE);
            ivCloseTwoBack.setVisibility(View.VISIBLE);
            ivCloseThree.setVisibility(View.VISIBLE);
            ivCloseFour.setVisibility(View.VISIBLE);
            ivCloseFive.setVisibility(View.VISIBLE);
            ivCloseSix.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(2).getImgPath()).into(ivaddone);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(3).getImgPath()).into(ivaddtwo);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(4).getImgPath()).into(ivaddthree);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(5).getImgPath()).into(ivaddfour);

            strImgIdOne = arrReceivedIMgs.get(0).getId();
            strImgIdTwo = arrReceivedIMgs.get(1).getId();
            strImgIdThree = arrReceivedIMgs.get(2).getId();
            strImgIdFour = arrReceivedIMgs.get(3).getId();
            strImgIdFive = arrReceivedIMgs.get(4).getId();
            strImgIdSix = arrReceivedIMgs.get(5).getId();

        } else if (arrReceivedIMgs.size() == 7) {
            setFrontBackImgTrue();
            ll2.setVisibility(View.VISIBLE);
            ll2_img2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll3_img2.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);
            ll4_img2.setVisibility(View.VISIBLE);

            ivCloseOneFront.setVisibility(View.VISIBLE);
            ivCloseTwoBack.setVisibility(View.VISIBLE);
            ivCloseThree.setVisibility(View.VISIBLE);
            ivCloseFour.setVisibility(View.VISIBLE);
            ivCloseFive.setVisibility(View.VISIBLE);
            ivCloseSix.setVisibility(View.VISIBLE);
            ivCloseSeven.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(2).getImgPath()).into(ivaddone);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(3).getImgPath()).into(ivaddtwo);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(4).getImgPath()).into(ivaddthree);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(5).getImgPath()).into(ivaddfour);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(6).getImgPath()).into(ivaddFive);

            strImgIdOne = arrReceivedIMgs.get(0).getId();
            strImgIdTwo = arrReceivedIMgs.get(1).getId();
            strImgIdThree = arrReceivedIMgs.get(2).getId();
            strImgIdFour = arrReceivedIMgs.get(3).getId();
            strImgIdFive = arrReceivedIMgs.get(4).getId();
            strImgIdSix = arrReceivedIMgs.get(5).getId();
            strImgIdSeven = arrReceivedIMgs.get(6).getId();

        } else if (arrReceivedIMgs.size() == 8) {
            setFrontBackImgTrue();

            ll2.setVisibility(View.VISIBLE);
            ll2_img2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll3_img2.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);
            ll4_img2.setVisibility(View.VISIBLE);
            ll5.setVisibility(View.VISIBLE);

            ivCloseOneFront.setVisibility(View.VISIBLE);
            ivCloseTwoBack.setVisibility(View.VISIBLE);
            ivCloseThree.setVisibility(View.VISIBLE);
            ivCloseFour.setVisibility(View.VISIBLE);
            ivCloseFive.setVisibility(View.VISIBLE);
            ivCloseSix.setVisibility(View.VISIBLE);
            ivCloseSeven.setVisibility(View.VISIBLE);
            ivCloseEight.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(2).getImgPath()).into(ivaddone);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(3).getImgPath()).into(ivaddtwo);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(4).getImgPath()).into(ivaddthree);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(5).getImgPath()).into(ivaddfour);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(6).getImgPath()).into(ivaddFive);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(7).getImgPath()).into(ivaddSix);

            strImgIdOne = arrReceivedIMgs.get(0).getId();
            strImgIdTwo = arrReceivedIMgs.get(1).getId();
            strImgIdThree = arrReceivedIMgs.get(2).getId();
            strImgIdFour = arrReceivedIMgs.get(3).getId();
            strImgIdFive = arrReceivedIMgs.get(4).getId();
            strImgIdSix = arrReceivedIMgs.get(5).getId();
            strImgIdSeven = arrReceivedIMgs.get(6).getId();
            strImgIdEight = arrReceivedIMgs.get(7).getId();

        } else if (arrReceivedIMgs.size() == 9) {
            setFrontBackImgTrue();

            ll2.setVisibility(View.VISIBLE);
            ll2_img2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll3_img2.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);
            ll4_img2.setVisibility(View.VISIBLE);
            ll5.setVisibility(View.VISIBLE);
            ll5_img2.setVisibility(View.VISIBLE);

            ivCloseOneFront.setVisibility(View.VISIBLE);
            ivCloseTwoBack.setVisibility(View.VISIBLE);
            ivCloseThree.setVisibility(View.VISIBLE);
            ivCloseFour.setVisibility(View.VISIBLE);
            ivCloseFive.setVisibility(View.VISIBLE);
            ivCloseSix.setVisibility(View.VISIBLE);
            ivCloseSeven.setVisibility(View.VISIBLE);
            ivCloseEight.setVisibility(View.VISIBLE);
            ivCloseNine.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(2).getImgPath()).into(ivaddone);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(3).getImgPath()).into(ivaddtwo);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(4).getImgPath()).into(ivaddthree);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(5).getImgPath()).into(ivaddfour);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(6).getImgPath()).into(ivaddFive);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(7).getImgPath()).into(ivaddSix);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(8).getImgPath()).into(ivaddSeven);

            strImgIdOne = arrReceivedIMgs.get(0).getId();
            strImgIdTwo = arrReceivedIMgs.get(1).getId();
            strImgIdThree = arrReceivedIMgs.get(2).getId();
            strImgIdFour = arrReceivedIMgs.get(3).getId();
            strImgIdFive = arrReceivedIMgs.get(4).getId();
            strImgIdSix = arrReceivedIMgs.get(5).getId();
            strImgIdSeven = arrReceivedIMgs.get(6).getId();
            strImgIdEight = arrReceivedIMgs.get(7).getId();
            strImgIdNine = arrReceivedIMgs.get(8).getId();

        } else if (arrReceivedIMgs.size() >= 10) {
            setFrontBackImgTrue();

            ll2.setVisibility(View.VISIBLE);
            ll2_img2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll3_img2.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);
            ll4_img2.setVisibility(View.VISIBLE);
            ll5.setVisibility(View.VISIBLE);
            ll5_img2.setVisibility(View.VISIBLE);

            ivCloseOneFront.setVisibility(View.VISIBLE);
            ivCloseTwoBack.setVisibility(View.VISIBLE);
            ivCloseThree.setVisibility(View.VISIBLE);
            ivCloseFour.setVisibility(View.VISIBLE);
            ivCloseFive.setVisibility(View.VISIBLE);
            ivCloseSix.setVisibility(View.VISIBLE);
            ivCloseSeven.setVisibility(View.VISIBLE);
            ivCloseEight.setVisibility(View.VISIBLE);
            ivCloseNine.setVisibility(View.VISIBLE);
            ivCloseTen.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(2).getImgPath()).into(ivaddone);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(3).getImgPath()).into(ivaddtwo);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(4).getImgPath()).into(ivaddthree);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(5).getImgPath()).into(ivaddfour);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(6).getImgPath()).into(ivaddFive);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(7).getImgPath()).into(ivaddSix);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(8).getImgPath()).into(ivaddSeven);
            Glide.with(AddProductsActivity.this).load(arrReceivedIMgs.get(9).getImgPath()).into(ivaddEight);

            strImgIdOne = arrReceivedIMgs.get(0).getId();
            strImgIdTwo = arrReceivedIMgs.get(1).getId();
            strImgIdThree = arrReceivedIMgs.get(2).getId();
            strImgIdFour = arrReceivedIMgs.get(3).getId();
            strImgIdFive = arrReceivedIMgs.get(4).getId();
            strImgIdSix = arrReceivedIMgs.get(5).getId();
            strImgIdSeven = arrReceivedIMgs.get(6).getId();
            strImgIdEight = arrReceivedIMgs.get(7).getId();
            strImgIdNine = arrReceivedIMgs.get(8).getId();
            strImgIdTen = arrReceivedIMgs.get(9).getId();
        }

    }

    /*private void showWelcomeDialog() {
        dialogService = new Dialog(AddProductsActivity.this);
        dialogService.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogService.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogService.setContentView(R.layout.dialog_got_it);
        dialogService.setCancelable(false);
        TextView tvName = (TextView) dialogService.findViewById(R.id.tvGotIt);

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogService.dismiss();
                adapterProductListing = new AdapterProductListing(AddProductsActivity.this,R.layout.row_product_list,arrProductsPage);
                gridView.setAdapter(adapterProductListing);
            }
        });

        WindowManager.LayoutParams wmlp = dialogService.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_HORIZONTAL;
        dialogService.show();
        dialogService.getWindow().setLayout((int) ((screenWidth / 5) * 4.2), (int) ((screenHeight / 12) * 5));

    }*/

    private void inIt() {
        //  gridView = (GridView) findViewById(R.id.gridAddProducts);
        //rvMainImages = (RecyclerView)findViewById(R.id.rvMainImages);
        tvCancel = findViewById(R.id.tvCancel);
        screenWidth = AddProductsActivity.this.getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = AddProductsActivity.this.getWindowManager().getDefaultDisplay().getHeight();
        btnNext = findViewById(R.id.btnAddProductNext);

        ivFrontView = findViewById(R.id.ivFrontView);
        ivBackView = findViewById(R.id.ivBackView);
        appPreference = new AppPreference(AddProductsActivity.this);

        ivaddone = findViewById(R.id.ivaddone);
        ivaddtwo = findViewById(R.id.ivaddtwo);
        ivaddthree = findViewById(R.id.ivaddThreeNew);
        ivaddfour = findViewById(R.id.ivaddFourNew);
        ivaddFive = findViewById(R.id.ivaddFive);
        ivaddSix = findViewById(R.id.ivaddSix);
        ivaddSeven = findViewById(R.id.ivaddSeven);
        ivaddEight = findViewById(R.id.ivaddEight);

        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);
        ll5 = findViewById(R.id.ll5);

        ll2_img2 = findViewById(R.id.ll2_img2);
        ll3_img1 = findViewById(R.id.ll3_img1);
        ll3_img2 = findViewById(R.id.ll3_img2);
        ll4_img1 = findViewById(R.id.ll4_img1);
        ll4_img2 = findViewById(R.id.ll4_img2);
        ll5_img1 = findViewById(R.id.ll5_img1);
        ll5_img2 = findViewById(R.id.ll5_img2);

        //ivCloseFront,ivBackView, ivaddone,ivCloseTwo,ivCloseThree,ivCloseFour,ivCloseFive, ivCloseSix,ivCloseSeven,ivCloseEight;

        ivCloseOneFront = findViewById(R.id.ivCloseOneFront);
        ivCloseTwoBack = findViewById(R.id.ivCloseTwoBack);

        ivCloseThree = findViewById(R.id.ivCloseThree);
        ivCloseFour = findViewById(R.id.ivCloseFour);
        ivCloseFive = findViewById(R.id.ivCloseFive);
        ivCloseSix = findViewById(R.id.ivCloseSix);
        ivCloseSeven = findViewById(R.id.ivCloseSeven);
        ivCloseEight = findViewById(R.id.ivCloseEight);
        ivCloseNine = findViewById(R.id.ivCloseNine);
        ivCloseTen = findViewById(R.id.ivCloseTen);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        sharedPreferences = this.getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        aQuery = new AQuery(this);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddProductsActivity.this, ProductListingScreen.class);
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
                        Intent i = new Intent(AddProductsActivity.this, AddEditCostumeActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(AddProductsActivity.this, "Please select frontview & backview images ", Toast.LENGTH_SHORT).show();
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
                                new callUpdateImagesAPI().execute("");
                            }

                        } else {
                            Intent i = new Intent(AddProductsActivity.this, AddEditCostumeActivity.class);
                            startActivity(i);
                            //finish();
                        }
                    } else {
                        Toast.makeText(AddProductsActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
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
        ivFrontView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFrontImage();
            }
        });

        ivBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBackView();
            }
        });

        ivaddone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if(arrImgsModelPath.size() >= 2){
                if (isFrontViewPresent && isBackViewPresent) {
                    selectAddOne();
                } else {
                    Toast.makeText(AddProductsActivity.this, "Front & Back view images are mandatory ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivaddtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddTwoImages();
            }
        });
        ivaddthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddThree();
            }
        });
        ivaddfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddFour();
            }
        });
        ivaddFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddFive();
            }
        });
        ivaddSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddSix();
            }
        });
        ivaddSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddSeven();
            }
        });
        ivaddEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddEight();
            }
        });

        ivCloseOneFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFrontViewPresent = false;
                ivFrontView.setImageResource(R.drawable.cam_holder);

                for (int i = 0; i < arrImgsModelPath.size(); i++) {
                    // String imgName =
                    if (arrImgsModelPath.get(i).getImgName().equalsIgnoreCase("Front")) {
                        arrImgsModelPath.remove(i);
                    }
                }
                if (ivCloseOneFront.getVisibility() == View.VISIBLE) {
                    ivCloseOneFront.setVisibility(View.GONE);
                }
            }
        });

        ivCloseTwoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivBackView.setImageResource(R.drawable.cam_holder);
                isBackViewPresent = false;
                for (int i = 0; i < arrImgsModelPath.size(); i++) {
                    // String imgName =
                    if (arrImgsModelPath.get(i).getImgName().equalsIgnoreCase("Back")) {
                        arrImgsModelPath.remove(i);
                    }
                }
                if (ivCloseTwoBack.getVisibility() == View.VISIBLE) {
                    ivCloseTwoBack.setVisibility(View.GONE);
                }
            }
        });

        ivCloseThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    if (!strImgIdThree.equals("")) {
                        callDeleteImageAPI(strImgIdThree);
                    } else {
                        if (!fileAddonePath.equals("")) {
                            ivaddone.setImageResource(R.drawable.cam_holder);
                            if (arrImgsModelPath.size() >= 3) {
                                arrImgsModelPath.remove(2);
                                hide_UnhidemImageView();
                            }

                        }
                        if (ivCloseThree.getVisibility() == View.VISIBLE) {
                            ivCloseThree.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Toast.makeText(AddProductsActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivCloseFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    if (!strImgIdFour.equals("")) {
                        callDeleteImageAPI(strImgIdFour);
                        //  new DeleteImage().execute("{\"costume_id\":\"" + sharedPreferences.getString("costumeid", "") + "\", \"image_id\":\"" + strImgIdFour + "\" }");
                    } else {
                        if (!fileAddTwo.equals("")) {
                            ivaddtwo.setImageResource(R.drawable.cam_holder);
                            if (arrImgsModelPath.size() >= 4) {
                                arrImgsModelPath.remove(3);
                                hide_UnhidemImageView();
                            }

                            if (ivCloseFour.getVisibility() == View.VISIBLE) {
                                ivCloseFour.setVisibility(View.GONE);
                            }
                        }
                    }

                } else {
                    Toast.makeText(AddProductsActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivCloseFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    if (!strImgIdFive.equals("")) {
                        callDeleteImageAPI(strImgIdFive);
                        // new DeleteImage().execute("{\"costume_id\":\"" + sharedPreferences.getString("costumeid", "") + "\", \"image_id\":\"" + strImgIdFive + "\" }");
                    } else {
                        if (!fileAddThree.equals("")) {
                            ivaddthree.setImageResource(R.drawable.cam_holder);
                            // ivaddthree.setVisibility(View.GONE);
                            if (!fileAddTwo.equals("")) {
                                ivaddthree.setImageResource(R.drawable.cam_holder);
                                if (arrImgsModelPath.size() >= 5) {
                                    arrImgsModelPath.remove(4);
                                }
                                hide_UnhidemImageView();

                                if (ivCloseFive.getVisibility() == View.VISIBLE) {
                                    ivCloseFive.setVisibility(View.GONE);
                                }
                            }
                        }
                    }

                } else {
                    Toast.makeText(AddProductsActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivCloseSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    if (!strImgIdSix.equals("")) {
                        callDeleteImageAPI(strImgIdSix);

                        // new DeleteImage().execute("{\"costume_id\":\"" + sharedPreferences.getString("costumeid", "") + "\", \"image_id\":\"" + strImgIdSix + "\" }");
                    } else {
                        if (!fileAddFour.equals("")) {
                            ivaddfour.setImageResource(R.drawable.cam_holder);
                            if (arrImgsModelPath.size() >= 6) {
                                arrImgsModelPath.remove(5);
                                hide_UnhidemImageView();
                            }

                            if (ivCloseSix.getVisibility() == View.VISIBLE) {
                                ivCloseSix.setVisibility(View.GONE);
                            }

                        }
                    }

                } else {
                    Toast.makeText(AddProductsActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivCloseSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    if (!strImgIdSeven.equals("")) {
                        callDeleteImageAPI(strImgIdSeven);

                        //new DeleteImage().execute("{\"costume_id\":\"" + sharedPreferences.getString("costumeid", "") + "\", \"image_id\":\"" + strImgIdSeven + "\" }");
                    } else {
                        if (!fileAddFive.equals("")) {
                            ivaddFive.setImageResource(R.drawable.cam_holder);
                            // ivaddFive.setVisibility(View.GONE);
                          /* for (int i = 0; i < arrImgsModelPath.size(); i++) {
                                if (arrImgsModelPath.get(i).getImgName().equalsIgnoreCase("Add5")) {
                                    arrImgsModelPath.remove(i);
                                }
                            }*/

                            if (arrImgsModelPath.size() >= 7) {
                                arrImgsModelPath.remove(6);
                                hide_UnhidemImageView();
                            }

                            if (ivCloseSeven.getVisibility() == View.VISIBLE) {
                                ivCloseSeven.setVisibility(View.GONE);
                            }

                        }
                    }

                } else {
                    Toast.makeText(AddProductsActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivCloseEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    if (!strImgIdEight.equals("")) {
                        callDeleteImageAPI(strImgIdEight);

                        //  new DeleteImage().execute("{\"costume_id\":\"" + sharedPreferences.getString("costumeid", "") + "\", \"image_id\":\"" + strImgIdEight + "\" }");
                    } else {
                        if (!fileAddSix.equals("")) {
                            ivaddSix.setImageResource(R.drawable.cam_holder);
                            // ivaddSix.setVisibility(View.GONE);
/*
                            for (int i = 0; i < arrImgsModelPath.size(); i++) {
                                if (arrImgsModelPath.get(i).getImgName().equalsIgnoreCase("Add6")) {
                                    arrImgsModelPath.remove(i);
                                }
                            }*/

                            if (arrImgsModelPath.size() >= 8) {
                                arrImgsModelPath.remove(7);
                                hide_UnhidemImageView();
                            }
                            if (ivCloseEight.getVisibility() == View.VISIBLE) {
                                ivCloseEight.setVisibility(View.GONE);
                            }

                        }
                    }

                } else {
                    Toast.makeText(AddProductsActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivCloseNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    if (!strImgIdNine.equals("")) {
                        callDeleteImageAPI(strImgIdNine);
                        // new DeleteImage().execute("{\"costume_id\":\"" + sharedPreferences.getString("costumeid", "") + "\", \"image_id\":\"" + strImgIdNine + "\" }");
                    } else {
                        if (!fileAddSeven.equals("")) {
                            ivaddSeven.setImageResource(R.drawable.cam_holder);
                            // ivaddSeven.setVisibility(View.GONE);
                            /*for (int i = 0; i < arrImgsModelPath.size(); i++) {
                                if (arrImgsModelPath.get(i).getImgName().equalsIgnoreCase("Add7")) {
                                    arrImgsModelPath.remove(i);
                                }
                            }*/

                            if (arrImgsModelPath.size() >= 9) {
                                arrImgsModelPath.remove(8);
                                hide_UnhidemImageView();
                            }
                            if (ivCloseNine.getVisibility() == View.VISIBLE) {
                                ivCloseNine.setVisibility(View.GONE);
                            }

                        }
                    }

                } else {
                    Toast.makeText(AddProductsActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivCloseTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {

                    if (!strImgIdTen.equals("")) {
                        callDeleteImageAPI(strImgIdTen);
                        // new DeleteImage().execute("{\"costume_id\":\"" + sharedPreferences.getString("costumeid", "") + "\", \"image_id\":\"" + strImgIdTen + "\" }");
                    } else {
                        if (!fileAddEight.equals("")) {
                            ivaddEight.setImageResource(R.drawable.cam_holder);

                            if (arrImgsModelPath.size() >= 10) {
                                arrImgsModelPath.remove(9);
                                hide_UnhidemImageView();
                            }

                            /*for (int i = 0; i < arrImgsModelPath.size(); i++) {
                                if (arrImgsModelPath.get(i).getImgName().equalsIgnoreCase("Add8")) {
                                    arrImgsModelPath.remove(i);
                                }
                            }*/

                            if (ivCloseTen.getVisibility() == View.VISIBLE) {
                                ivCloseTen.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {
                    Toast.makeText(AddProductsActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void callDeleteImageAPI(String strImg) {
        String deleteURL = BASE_URL + "deleteImages";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("costume_id", appPreference.getCostumeId());
            jsonObject.put("image_id", strImg);

            System.out.println(">>> my Dleted Params : " + jsonObject);
            new APIRequest(AddProductsActivity.this, jsonObject, deleteURL, Config.API_DELETE_IMAGE, Config.POST);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void deleteImageResponse(DeleteImageResponse deleteImageResponse) {
        if (deleteImageResponse.getResult().equalsIgnoreCase("success")) {
            Toast.makeText(AddProductsActivity.this, deleteImageResponse.getMessage(), Toast.LENGTH_SHORT).show();
            editor.putString("total_count", deleteImageResponse.getData().getTotalCount().toString());
            editor.commit();

            Intent i = new Intent(AddProductsActivity.this, AddProductsActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(AddProductsActivity.this, deleteImageResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void hide_UnhidemImageView() {
        if (arrImgsModelPath.size() == 2) {
            System.out.println("## >> In size 2");
            ivCloseOneFront.setVisibility(View.VISIBLE);
            ivCloseTwoBack.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(1).getImgPath()).into(ivBackView);

            ll2_img2.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
            ll4.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);

        } else if (arrImgsModelPath.size() == 3) {
            System.out.println("## >> In size 3");

            ivCloseOneFront.setVisibility(View.VISIBLE);
            ivCloseTwoBack.setVisibility(View.VISIBLE);
            ivCloseThree.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(2).getImgPath()).into(ivaddone);
            ll2_img2.setVisibility(View.VISIBLE);
            ivaddtwo.setImageResource(R.drawable.cam_holder);

            ll3.setVisibility(View.GONE);
            ll4.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);

        } else if (arrImgsModelPath.size() == 4) {
            System.out.println("## >> In size 4");

            ll2.setVisibility(View.VISIBLE);
            ll2_img2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);

            ivCloseOneFront.setVisibility(View.VISIBLE);
            ivCloseTwoBack.setVisibility(View.VISIBLE);
            ivCloseThree.setVisibility(View.VISIBLE);
            ivCloseFour.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(2).getImgPath()).into(ivaddone);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(3).getImgPath()).into(ivaddtwo);
            ivaddthree.setImageResource(R.drawable.cam_holder);
            ll3_img2.setVisibility(View.GONE);
            ll4.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);

        } else if (arrImgsModelPath.size() == 5) {

            System.out.println("## >> In size 5");

            ll2.setVisibility(View.VISIBLE);
            ll2_img2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll3_img2.setVisibility(View.VISIBLE);

            ivCloseOneFront.setVisibility(View.VISIBLE);
            ivCloseTwoBack.setVisibility(View.VISIBLE);
            ivCloseThree.setVisibility(View.VISIBLE);
            ivCloseFour.setVisibility(View.VISIBLE);
            ivCloseFive.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(2).getImgPath()).into(ivaddone);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(3).getImgPath()).into(ivaddtwo);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(4).getImgPath()).into(ivaddthree);
            ivaddfour.setImageResource(R.drawable.cam_holder);

            ll4.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);

        } else if (arrImgsModelPath.size() == 6) {
            System.out.println("## >> In size 6");
            ll2.setVisibility(View.VISIBLE);
            ll2_img2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll3_img2.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);

            ivCloseOneFront.setVisibility(View.VISIBLE);
            ivCloseTwoBack.setVisibility(View.VISIBLE);
            ivCloseThree.setVisibility(View.VISIBLE);
            ivCloseFour.setVisibility(View.VISIBLE);
            ivCloseFive.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(2).getImgPath()).into(ivaddone);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(3).getImgPath()).into(ivaddtwo);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(4).getImgPath()).into(ivaddthree);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(5).getImgPath()).into(ivaddfour);
            ivaddFive.setImageResource(R.drawable.cam_holder);

            ll4_img2.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);

        } else if (arrImgsModelPath.size() == 7) {
            System.out.println("## >> In size 7");
            ll2.setVisibility(View.VISIBLE);
            ll2_img2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll3_img2.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);
            ll4_img2.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(2).getImgPath()).into(ivaddone);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(3).getImgPath()).into(ivaddtwo);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(4).getImgPath()).into(ivaddthree);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(5).getImgPath()).into(ivaddfour);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(6).getImgPath()).into(ivaddFive);
            ivaddSix.setImageResource(R.drawable.cam_holder);

            ll5.setVisibility(View.GONE);

        } else if (arrImgsModelPath.size() == 8) {

            System.out.println("## >> In size 8");

            ll2.setVisibility(View.VISIBLE);
            ll2_img2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll3_img2.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);
            ll4_img2.setVisibility(View.VISIBLE);
            ll5.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(2).getImgPath()).into(ivaddone);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(3).getImgPath()).into(ivaddtwo);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(4).getImgPath()).into(ivaddthree);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(5).getImgPath()).into(ivaddfour);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(6).getImgPath()).into(ivaddFive);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(7).getImgPath()).into(ivaddSix);
            ivaddSeven.setImageResource(R.drawable.cam_holder);

            ll5_img2.setVisibility(View.GONE);

        } else if (arrImgsModelPath.size() == 9) {

            System.out.println("## >> In size 9");

            ll2.setVisibility(View.VISIBLE);
            ll2_img2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll3_img2.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);
            ll4_img2.setVisibility(View.VISIBLE);
            ll5.setVisibility(View.VISIBLE);
            ll5_img2.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(2).getImgPath()).into(ivaddone);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(3).getImgPath()).into(ivaddtwo);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(4).getImgPath()).into(ivaddthree);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(5).getImgPath()).into(ivaddfour);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(6).getImgPath()).into(ivaddFive);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(7).getImgPath()).into(ivaddSix);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(8).getImgPath()).into(ivaddSeven);
            ivaddEight.setImageResource(R.drawable.cam_holder);

        } else if (arrImgsModelPath.size() >= 10) {

            System.out.println("## >> In size 10");

            ll2.setVisibility(View.VISIBLE);
            ll2_img2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll3_img2.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);
            ll4_img2.setVisibility(View.VISIBLE);
            ll5.setVisibility(View.VISIBLE);
            ll5_img2.setVisibility(View.VISIBLE);

            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(0).getImgPath()).into(ivFrontView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(1).getImgPath()).into(ivBackView);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(2).getImgPath()).into(ivaddone);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(3).getImgPath()).into(ivaddtwo);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(4).getImgPath()).into(ivaddthree);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(5).getImgPath()).into(ivaddfour);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(6).getImgPath()).into(ivaddFive);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(7).getImgPath()).into(ivaddSix);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(8).getImgPath()).into(ivaddSeven);
            Glide.with(AddProductsActivity.this).load(arrImgsModelPath.get(9).getImgPath()).into(ivaddEight);

        }
    }

    private void selectFrontImage() {

        final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivity.this);
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

    private void selectBackView() {
        final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {
                    idBackView = true;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUriId = UploadStuff.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriId);
                    startActivityForResult(intent, SELECT_PHOTO1);

                } else if (options[item].equals("Gallery")) {
                    idBackView = false;
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    private void selectAddOne() {

        final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {
                    idAddone = true;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUriId = UploadStuff.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriId);
                    startActivityForResult(intent, SELECT_PHOTO3);

                } else if (options[item].equals("Gallery")) {
                    idAddone = false;
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO3);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    private void selectAddTwoImages() {
        final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {
                    boolAddTwo = true;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUriId = UploadStuff.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriId);
                    startActivityForResult(intent, SELECT_PHOTO12);

                } else if (options[item].equals("Gallery")) {
                    boolAddTwo = false;
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO12);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    private void selectAddThree() {
        final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {
                    boolAddThree = true;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUriId = UploadStuff.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriId);
                    startActivityForResult(intent, SELECT_PHOTO13);

                } else if (options[item].equals("Gallery")) {
                    boolAddThree = false;
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO13);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    private void selectAddFour() {
        final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {
                    boolAddFour = true;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUriId = UploadStuff.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriId);
                    startActivityForResult(intent, SELECT_PHOTO14);

                } else if (options[item].equals("Gallery")) {
                    boolAddFour = false;
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO14);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    private void selectAddFive() {
        final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {
                    boolAddFive = true;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUriId = UploadStuff.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriId);
                    startActivityForResult(intent, SELECT_PHOTO15);

                } else if (options[item].equals("Gallery")) {
                    boolAddFive = false;
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO15);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    private void selectAddSix() {
        final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {
                    boolAddSix = true;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUriId = UploadStuff.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriId);
                    startActivityForResult(intent, SELECT_PHOTO16);

                } else if (options[item].equals("Gallery")) {
                    boolAddSix = false;
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO16);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    private void selectAddSeven() {
        final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {
                    boolAddSeven = true;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUriId = UploadStuff.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriId);
                    startActivityForResult(intent, SELECT_PHOTO17);

                } else if (options[item].equals("Gallery")) {
                    boolAddSeven = false;
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO17);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    private void selectAddEight() {
        final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {
                    boolAddEight = true;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUriId = UploadStuff.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriId);
                    startActivityForResult(intent, SELECT_PHOTO18);

                } else if (options[item].equals("Gallery")) {
                    boolAddEight = false;
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO18);

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
            if (ActivityCompat.checkSelfPermission(AddProductsActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
            if (ActivityCompat.checkSelfPermission(AddProductsActivity.this, permissionsRequired[1]) == PackageManager.PERMISSION_GRANTED) {
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
                            isFrontViewPresent = true;
                            ivFrontView.setImageURI(editedImageUri);
                            ivCloseOneFront.setVisibility(View.VISIBLE);
                            String newPath = getResizedImagePath(getPath(editedImageUri), "front");
                            addImagePathToModel(newPath, "Front");
                        }

                    }
                }

                break;

            case SELECT_PHOTO1:
                if (resultCode == RESULT_OK) {
                    if (idBackView) {
                        filePathBackView = fileUriId.getPath();
                        if (!filePathBackView.isEmpty() && filePathBackView != null) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 8;
                            final Bitmap bitmap = BitmapFactory.decodeFile(filePathBackView, options);
                            // ivBackView.setImageBitmap(bitmap);

                        }
                    } else {
                        //if idCard pic  selected from Gallery
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            String imagepath = getPath(selectedImageUri);
                            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                            //  ivBackView.setImageBitmap(bitmap);
                            filePathBackView = imagepath;
                        }
                    }
                    if (!filePathBackView.equals("")) {
                        Intent imageEditorIntent = getAvairyImageIntent(filePathBackView);
                        startActivityForResult(imageEditorIntent, IMG_SELECT_PHOTO1);
                    }
                    break;
                }

            case IMG_SELECT_PHOTO1:
                Uri uriPhotoOne = null;
                if (AdobeImageIntent.EXTRA_OUTPUT_URI != null && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals("null")) {

                    if (data != null) {
                        uriPhotoOne = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
                        if (uriPhotoOne != null) {
                            isBackViewPresent = true;
                            ivBackView.setImageURI(uriPhotoOne);
                            ivCloseTwoBack.setVisibility(View.VISIBLE);
                            System.out.println(">>> Return URI pho: " + uriPhotoOne);
                            String newPath1 = getResizedImagePath(getPath(uriPhotoOne), "back");
                            addImagePathToModel(newPath1, "Back");
                        }
                    }
                }
                break;

            case SELECT_PHOTO3:
                if (resultCode == RESULT_OK) {
                    if (idAddone) {
                        fileAddonePath = fileUriId.getPath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        final Bitmap bitmap = BitmapFactory.decodeFile(fileAddonePath, options);
                        //  ivaddone.setImageBitmap(bitmap);

                    } else {
                        if (data != null) {
                            //if idCard pic  selected from Gallery
                            Uri selectedImageUri = data.getData();
                            String imagepath = getPath(selectedImageUri);
                            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                            //  ivaddone.setImageBitmap(bitmap);
                            fileAddonePath = imagepath;
                        }
                    }

                    System.out.println(">>>> fileAddonePath at crashing:" + fileAddonePath);
                    if (!fileAddonePath.isEmpty() && fileAddonePath != null && !fileAddonePath.equalsIgnoreCase("null")) {
                        Intent imageEditorIntent = getAvairyImageIntent(fileAddonePath);
                        startActivityForResult(imageEditorIntent, IMG_SELECT_PHOTO3);
                    }
                    break;
                }

            case IMG_SELECT_PHOTO3:
                Uri uriPhotothree = null;
                if (AdobeImageIntent.EXTRA_OUTPUT_URI != null && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals("null") && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals(null)) {
                    System.out.println(">> data.getData() at third Activity : " + data.getData());
                    if (data != null) {
                        uriPhotothree = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
                        if (uriPhotothree != null) {
                            ivaddone.setImageURI(uriPhotothree);
                            ivCloseThree.setVisibility(View.VISIBLE);
                            System.out.println(">>> Return URI pho: " + uriPhotothree);
                            String addone = getResizedImagePath(getPath(uriPhotothree), "add1");
                            System.out.println(">>> Actual back path is Front" + addone);

                            addImagePathToModel(addone, "Add1");
                            arrImgPaths.add(addone);
                            //arrImgsEditPath.add(addone);
                            if (arrImgPaths.size() >= 1) {
                                ll2_img2.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                }
                break;

            case SELECT_PHOTO12:
                if (resultCode == RESULT_OK) {
                    if (boolAddTwo) {
                        fileAddTwo = fileUriId.getPath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        final Bitmap bitmap = BitmapFactory.decodeFile(fileAddTwo, options);
                        // ivaddtwo.setImageBitmap(bitmap);
                    } else {
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            String imagepath = getPath(selectedImageUri);
                            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                            //   ivaddtwo.setImageBitmap(bitmap);
                            fileAddTwo = imagepath;
                        }
                    }
                    if (!fileAddTwo.equals("")) {
                        Intent imageEditorIntent = getAvairyImageIntent(fileAddTwo);
                        startActivityForResult(imageEditorIntent, IMG_SELECT_PHOTO12);
                    }

                    break;
                }

            case IMG_SELECT_PHOTO12:
                Uri uriPhotoFour = null;
                if (AdobeImageIntent.EXTRA_OUTPUT_URI != null && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals("null") && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals(null)) {
                    if (data != null) {
                        uriPhotoFour = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
                        if (uriPhotoFour != null) {
                            ivaddtwo.setImageURI(uriPhotoFour);
                            ivCloseFour.setVisibility(View.VISIBLE);
                            System.out.println(">>> Return URI pho: " + uriPhotoFour);
                            String addTwo = getResizedImagePath(getPath(uriPhotoFour), "add2");
                            addImagePathToModel(addTwo, "Add2");
                            arrImgPaths.add(addTwo);
                        }

                        if (arrImgPaths.size() >= 2) {
                            ll3.setVisibility(View.VISIBLE);
                        }
                    }

                }

                break;

            case SELECT_PHOTO13:
                if (resultCode == RESULT_OK) {
                    if (boolAddThree) {
                        fileAddThree = fileUriId.getPath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        final Bitmap bitmap = BitmapFactory.decodeFile(fileAddThree, options);
                        //  ivaddthree.setImageBitmap(bitmap);
                    } else {
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            String imagepath = getPath(selectedImageUri);
                            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                            // ivaddthree.setImageBitmap(bitmap);
                            fileAddThree = imagepath;
                        }
                    }
                    if (!fileAddThree.equals("")) {
                        Intent imageEditorIntent = getAvairyImageIntent(fileAddThree);
                        startActivityForResult(imageEditorIntent, IMG_SELECT_PHOTO13);
                    }

                    break;
                }
            case IMG_SELECT_PHOTO13:
                Uri uri13 = null;
                if (AdobeImageIntent.EXTRA_OUTPUT_URI != null && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals("null") && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals(null)) {
                    if (data != null) {
                        uri13 = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
                        if (uri13 != null) {
                            ivaddthree.setImageURI(uri13);
                            ivCloseFive.setVisibility(View.VISIBLE);
                            System.out.println(">>> Return URI pho: " + uri13);
                            String path13 = getResizedImagePath(getPath(uri13), "add3");
                            addImagePathToModel(path13, "Add3");
                            System.out.println(">>> Actual back path is Front" + path13);
                            arrImgPaths.add(path13);
                            if (arrImgPaths.size() >= 3) {
                                ll3_img2.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                break;

            case SELECT_PHOTO14:
                if (resultCode == RESULT_OK) {
                    if (boolAddFour) {
                        fileAddFour = fileUriId.getPath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        final Bitmap bitmap = BitmapFactory.decodeFile(fileAddFour, options);
                        // ivaddfour.setImageBitmap(bitmap);
                    } else {
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            String imagepath = getPath(selectedImageUri);
                            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                            // ivaddfour.setImageBitmap(bitmap);
                            fileAddFour = imagepath;
                        }
                    }
                    if (!fileAddFour.equals("")) {
                        Intent imageEditorIntent = getAvairyImageIntent(fileAddFour);
                        startActivityForResult(imageEditorIntent, IMG_SELECT_PHOTO14);
                    }
                    break;
                }
            case IMG_SELECT_PHOTO14:
                Uri uriPath14 = null;
                if (AdobeImageIntent.EXTRA_OUTPUT_URI != null && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals("null") && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals(null)) {
                    if (data != null) {
                        uriPath14 = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
                        if (uriPath14 != null) {
                            ivaddfour.setImageURI(uriPath14);
                            ivCloseSix.setVisibility(View.VISIBLE);
                            System.out.println(">>> Return URI pho: " + uriPath14);
                            String pathImgP14 = getResizedImagePath(getPath(uriPath14), "add4");
                            arrImgPaths.add(pathImgP14);
                            addImagePathToModel(pathImgP14, "Add4");
                            if (arrImgPaths.size() >= 4) {
                                ll4.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                }

                break;

            case SELECT_PHOTO15:
                if (resultCode == RESULT_OK) {
                    if (boolAddFive) {
                        fileAddFive = fileUriId.getPath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        final Bitmap bitmap = BitmapFactory.decodeFile(fileAddFive, options);
                        // ivaddFive.setImageBitmap(bitmap);
                    } else {
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            String imagepath = getPath(selectedImageUri);
                            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                            // ivaddFive.setImageBitmap(bitmap);
                            fileAddFive = imagepath;
                        }
                    }
                    if (!fileAddFive.equals("")) {
                        Intent imageEditorIntent = getAvairyImageIntent(fileAddFive);
                        startActivityForResult(imageEditorIntent, IMG_SELECT_PHOTO15);

                    }

                    break;
                }

            case IMG_SELECT_PHOTO15:
                Uri uri15 = null;
                if (AdobeImageIntent.EXTRA_OUTPUT_URI != null && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals("null") && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals(null)) {
                    if (data != null) {
                        uri15 = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
                        if (uri15 != null) {
                            ivaddFive.setImageURI(uri15);
                            ivCloseSeven.setVisibility(View.VISIBLE);
                            System.out.println(">>> Return URI pho: " + uri15);
                            String path15 = getResizedImagePath(getPath(uri15), "add5");
                            System.out.println(">>> Actual back path is Front" + path15);
                            addImagePathToModel(path15, "Add5");
                            arrImgPaths.add(path15);
                            if (arrImgPaths.size() >= 5) {
                                ll4_img2.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                break;

            case SELECT_PHOTO16:
                if (resultCode == RESULT_OK) {
                    if (boolAddSix) {
                        fileAddSix = fileUriId.getPath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        final Bitmap bitmap = BitmapFactory.decodeFile(fileAddSix, options);
                        //ivaddSix.setImageBitmap(bitmap);
                    } else {
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            String imagepath = getPath(selectedImageUri);
                            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                            // ivaddSix.setImageBitmap(bitmap);
                            fileAddSix = imagepath;
                        }
                    }
                    if (!fileAddSix.equals("")) {
                        Intent imageEditorIntent = getAvairyImageIntent(fileAddSix);
                        startActivityForResult(imageEditorIntent, IMG_SELECT_PHOTO16);
                    }
                    break;
                }

            case IMG_SELECT_PHOTO16:
                Uri uri16 = null;
                if (AdobeImageIntent.EXTRA_OUTPUT_URI != null && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals("null") && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals(null)) {
                    if (data != null) {
                        uri16 = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
                        if (uri16 != null) {
                            ivaddSix.setImageURI(uri16);
                            ivCloseEight.setVisibility(View.VISIBLE);
                            String path16 = getResizedImagePath(getPath(uri16), "add6");
                            addImagePathToModel(path16, "Add6");
                            arrImgPaths.add(path16);
                            if (arrImgPaths.size() >= 6) {
                                ll5.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                break;

            case SELECT_PHOTO17:
                if (resultCode == RESULT_OK) {
                    if (boolAddSeven) {
                        fileAddSeven = fileUriId.getPath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        final Bitmap bitmap = BitmapFactory.decodeFile(fileAddSeven, options);
                        ivaddSeven.setImageBitmap(bitmap);
                    } else {
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            String imagepath = getPath(selectedImageUri);
                            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                            ivaddSeven.setImageBitmap(bitmap);
                            fileAddSeven = imagepath;
                        }
                    }
                    if (!fileAddSeven.equals("")) {
                        Intent imageEditorIntent = getAvairyImageIntent(fileAddSeven);
                        startActivityForResult(imageEditorIntent, IMG_SELECT_PHOTO17);
                    }

                    break;
                }

            case IMG_SELECT_PHOTO17:
                Uri uri17 = null;
                if (AdobeImageIntent.EXTRA_OUTPUT_URI != null && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals("null") && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals(null)) {
                    if (data != null) {
                        uri17 = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
                        if (uri17 != null) {
                            ivaddSeven.setImageURI(uri17);
                            ivCloseNine.setVisibility(View.VISIBLE);
                            System.out.println(">>> Return URI pho: " + uri17);
                            String path17 = getResizedImagePath(getPath(uri17), "add7");
                            addImagePathToModel(path17, "Add7");
                            arrImgPaths.add(path17);
                            if (arrImgPaths.size() >= 7) {
                                ll5_img2.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                break;

            case SELECT_PHOTO18:
                if (resultCode == RESULT_OK) {
                    if (boolAddEight) {
                        fileAddEight = fileUriId.getPath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        final Bitmap bitmap = BitmapFactory.decodeFile(fileAddEight, options);
                        ivaddEight.setImageBitmap(bitmap);
                    } else {
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            String imagepath = getPath(selectedImageUri);
                            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                            ivaddEight.setImageBitmap(bitmap);
                            fileAddEight = imagepath;
                        }
                    }
                    if (!fileAddEight.equals("")) {
                        Intent imageEditorIntent = getAvairyImageIntent(fileAddEight);
                        startActivityForResult(imageEditorIntent, IMG_SELECT_PHOTO18);
                    }

                    break;
                }

            case IMG_SELECT_PHOTO18:
                Uri uri18 = null;
                if (AdobeImageIntent.EXTRA_OUTPUT_URI != null && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals("null") && !String.valueOf(AdobeImageIntent.EXTRA_OUTPUT_URI).equals(null)) {
                    if (data != null) {
                        uri18 = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
                        if (uri18 != null) {
                            ivaddEight.setImageURI(uri18);
                            ivCloseTen.setVisibility(View.VISIBLE);
                            String path18 = getResizedImagePath(getPath(uri18), "add8");
                            addImagePathToModel(path18, "Add8");
                            arrImgPaths.add(path18);
                        }
                    }
                }

                break;
        }
    }

    private void addImagePathToModel(String newPath, String imgName) {

        boolean isAlready = false;
        for (int i = 0; i < arrImgsModelPath.size(); i++) {
            String name = arrImgsModelPath.get(i).getImgName();
            if (imgName.equals(name)) {
                isAlready = true;
                arrImgsModelPath.get(i).setImgPath(newPath);
                break;
            }
        }
        if (!isAlready) {
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
        SharedPreferences sp = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddProductsActivity.this, ProductListingScreen.class);
        startActivity(i);
        finish();

    }

    /*class DeleteImage extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(AddProductsActivity.this);
            p.setMessage("In Progress..");
            p.setCanceledOnTouchOutside(false);
            p.show();
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
                    .url(Config.BASE_URL + "deleteImages")
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
            System.out.println(">>> Delete result : " + s);
            p.dismiss();
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String strResult = jsonObject.getString("result");
                    if (strResult.equalsIgnoreCase("success")) {
                        Toast.makeText(AddProductsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONObject objData = jsonObject.getJSONObject("data");
                        String strTotalCount = objData.getString("total_count");
                        editor.putString("total_count", strTotalCount);
                        editor.commit();

                        Intent i = new Intent(AddProductsActivity.this, AddProductsActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(AddProductsActivity.this, "Something went wrong, image not deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(AddProductsActivity.this, R.string.NoNetworkMsg, Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(AddProductsActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(AddProductsActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddProductsActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(AddProductsActivity.this, permissionsRequired[1])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and External permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(AddProductsActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivity.this);
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
                ActivityCompat.requestPermissions(AddProductsActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(AddProductsActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(AddProductsActivity.this, permissionsRequired[1])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductsActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(AddProductsActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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
        //Toast.makeText(getBaseContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(AddProductsActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
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

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println(">>> arrReceivedIMgs size : ");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("imgarray", strSavedImages);
        //savedInstanceState.putStringArrayList("imgarray, Config.arrSavedImages");
        // etc.
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        String myString = savedInstanceState.getString("imgarray");
        System.out.println("**********myString*********" + myString);
    }

    class callUpdateImagesAPI extends AsyncTask<String, Void, String> {
        ProgressDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(AddProductsActivity.this);
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

                        Toast.makeText(AddProductsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AddProductsActivity.this, AddEditCostumeActivity.class);
                        startActivity(i);
                        //  finish();
                    } else {
                        String strMessage = jsonObject.getString("message");
                        Toast.makeText(AddProductsActivity.this, strMessage, Toast.LENGTH_SHORT).show();
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
