package com.chrysalis.domain;

import com.chrysalis.model.ModelAddImages;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
    public static final int API_LOGIN = 99;
    // public static String BASE_URL = "http://api.chrysalis.dotcomweavers.net/chrysalis_webservices/api/";
    // public static String BASE_URL = "http://103.224.243.227/cakephp/chrysalis_webservices/api/";
    // public static String BASE_URL_NEW = "http://api.chrysaliscostumes.com/chrysalis_webservices/api/";
    public static final int API_CATEGORY_LIST = 100;
    public static final int API_EDIT_COSTUME = 101;
    public static final int API_CHARITY_LIST = 102;
    public static final int API_UPDATE_COSTUME = 104;
    public static final int API_UPDATE_EDIT_IMAGES = 105;
    public static final int API_DELETE_IMAGE = 106;
    public static final int API_SIGNUP = 107;
    public static final int API_FORGET_PASS = 108;
    public static String BASE_URL = "http://api.chrysaliscostumes.com/chrysalis_webservices/api/";
    public static String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static String strIsEditCalled = "No"; // Flag for calling the Normal API or Edit API flow Android.
    // public static ArrayList<String> arrAddImages = new ArrayList<>();
    public static String isBackFromPricingScreen = "No";
    public static String isBackFromReviewPrefrences = "No";
    public static ArrayList<String> arrForKeywordSearch = new ArrayList<>();
    public static String GET = "GET";
    public static String POST = "POST";

    public static String MODE = "";
    public static String ADD_MODE = "ADD";
    public static String EDIT_MODE = "EDIT";
    public static List<ModelAddImages> arrSavedImages;

    public static boolean flagImgPresent = false;

    public static boolean isContainSpecialChar(String strVal) {
        Pattern regex = Pattern.compile("[$&+,:;=?@#|]");
        Matcher matcher = regex.matcher(strVal);
        boolean boolres = matcher.find();

        return boolres;
    }


 /* private void generateKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.chrysalis",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("KeyHash >>> "+Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }*/

}
