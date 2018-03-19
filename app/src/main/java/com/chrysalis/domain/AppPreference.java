package com.chrysalis.domain;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {

    private static final String SMART_FARMING_PREF = "chrysalis_prefs";

    private static final String USER_LANGUAGE = "userLanguage";
    private static final String IMAGE_ARRAY = "imagearray";

    public static AppPreference mAppPreference;

    Context mContext;
    SharedPreferences sharedPreference;

    private AppPreference() {
    }

    public AppPreference(Context context) {
        mContext = context;
        sharedPreference = mContext.getSharedPreferences(SMART_FARMING_PREF, Context.MODE_PRIVATE);
    }

    public static AppPreference getInstance(Context context) {
        if (mAppPreference == null)
            mAppPreference = new AppPreference(context);

        return mAppPreference;
    }

    public void remove(String strVal) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.remove(strVal);
        prefsEditor.apply();

    }

    /**
     * set flag for first time user
     *
     * @param isFirstTime
     */
    public void setIsFirstTimeTag(boolean isFirstTime) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putBoolean("isFirstTime", isFirstTime);
        prefsEditor.commit();
    }

    /**
     * Get first time user flag
     *
     * @return isFirstTime
     */
    public boolean isFirstTime() {
        return sharedPreference.getBoolean("isFirstTime", false);
    }

    /**
     * set user last selected language to preference
     *
     * @param language
     */

    public void putString(String language) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString(IMAGE_ARRAY, language);
        prefsEditor.commit();
    }

    public String getUserLanguage() {
        return sharedPreference.getString(USER_LANGUAGE, "");
    }

    public void setUserLanguage(String language) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString(USER_LANGUAGE, language);
        prefsEditor.commit();
    }

    public String getCategoryJson() {
        return sharedPreference.getString("CategoryList", null);
    }

    /**
     * set category json
     *
     * @param json
     */
    public void setCategoryJson(String json) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("CategoryList", json);
        prefsEditor.commit();
    }

    public String getFrontViewImg() {
        return sharedPreference.getString("frontView", "");
    }

    public void setFrontViewImg(String name) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("frontView", name);
        prefsEditor.commit();
    }

    public String getBackViewImg() {
        return sharedPreference.getString("backView", "");
    }

    public void setBackViewImg(String name) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("backView", name);
        prefsEditor.commit();
    }

    public String getCostumeId() {

        return sharedPreference.getString("costumeId", "");
    }

    public void setCostumeId(String costumeId) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("costumeId", costumeId);
        prefsEditor.commit();
    }

    public void setproductionName(String name) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("ProductionName", name);
        prefsEditor.commit();
    }

    public String getProductionName() {
        return sharedPreference.getString("ProductionName", "");
    }

    public String getCostumeName() {
        return sharedPreference.getString("CostumeName", "");
    }

    public void setCostumeName(String name) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("CostumeName", name);
        prefsEditor.commit();
    }

    public String getUserId() {
        return sharedPreference.getString("userId", "");
    }

    public void setUserId(String userid) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("userId", userid);
        prefsEditor.commit();
    }

    public String getCategoryId() {
        return sharedPreference.getString("category", "");
    }

    public void setCategoryId(String name) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("category", name);
        prefsEditor.commit();

    }

    public String getCategoryName() {
        return sharedPreference.getString("categoryName", "");
    }

    public void setCategoryName(String name) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("categoryName", name);
        prefsEditor.commit();

    }

    public String getGender() {
        return sharedPreference.getString("gender", "");
    }

    public void setGender(String gender) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("gender", gender);
        prefsEditor.commit();

    }

    public String getSize() {
        return sharedPreference.getString("size", "");
    }

    public void setSize(String size) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("size", size);
        prefsEditor.commit();

    }

    public String getThemeId() {
        return sharedPreference.getString("theme", "");
    }

    public void setThemeId(String theme) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("theme", theme);
        prefsEditor.commit();

    }

    public String getThemeName() {
        return sharedPreference.getString("themeName", "");
    }

    public void setThemeName(String themeName) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("themeName", themeName);
        prefsEditor.commit();

    }

    public String getCondition() {
        return sharedPreference.getString("condition", "");
    }

    public void setCondition(String condition) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("condition", condition);
        prefsEditor.commit();

    }

    public String getKeyword() {
        return sharedPreference.getString("keyword", "");
    }

    public void setKeyword(String keyword) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("keyword", keyword);
        prefsEditor.commit();

    }

    public String getMakeCostume() {
        return sharedPreference.getString("make_costume", "");
    }

    public void setMakeCostume(String makecostume) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("make_costume", makecostume);
        prefsEditor.commit();

    }

    public String getCostumeTime() {
        return sharedPreference.getString("costume_time", "");
    }

    public void setCostumeTime(String costumeTime) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("costume_time", costumeTime);
        prefsEditor.commit();

    }

    public String getCostumeDescription() {
        return sharedPreference.getString("costume_desc", "");
    }

    public void setCostumeDescription(String desc) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("costume_desc", desc);
        prefsEditor.commit();

    }

    public String getFaq() {
        return sharedPreference.getString("faq", "");
    }

    public void setFaq(String faq) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("faq", faq);
        prefsEditor.commit();

    }

    public String getShippingOptions() {
        return sharedPreference.getString("strShippingOption", "");
    }

    public void setShippingOptions(String shippingOption) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("strShippingOption", shippingOption);
        prefsEditor.commit();
    }

    public String getWeightofPackgedItem() {
        return sharedPreference.getString("weightofPackgedItem", "");
    }

    public void setWeightofPackgedItem(String weight) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("weightofPackgedItem", weight);
        prefsEditor.commit();

    }

    public String getLenght() {
        return sharedPreference.getString("lenght", "");
    }

    public void setLenght(String length) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("lenght", length);
        prefsEditor.commit();

    }

    public String getWidth() {
        return sharedPreference.getString("width", "");
    }

    public void setWidth(String width) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("width", width);
        prefsEditor.commit();

    }

    public String getHeight() {
        return sharedPreference.getString("height", "");
    }

    public void setHeight(String length) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("height", length);
        prefsEditor.commit();

    }

    public String getPounds() {
        return sharedPreference.getString("pounds", "");
    }

    public void setPounds(String pounds) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("pounds", pounds);
        prefsEditor.commit();

    }

    public String getOunces() {
        return sharedPreference.getString("ounces", "");
    }

    public void setOunces(String ounces) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("ounces", ounces);
        prefsEditor.commit();

    }

    public String getShippingPrice() {
        return sharedPreference.getString("price", "");
    }

    public void setShippingPrice(String price) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("price", price);
        prefsEditor.commit();

    }

    public String getHandalingTime() {
        return sharedPreference.getString("handalingTime", "");
    }

    public void setHandalingTime(String time) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("handalingTime", time);
        prefsEditor.commit();

    }

    public String getSuggestedChrity() {
        return sharedPreference.getString("charity", "");
    }

    public void setSuggestedChrity(String time) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("charity", time);
        prefsEditor.commit();

    }

    public String getReturnPolicy() {
        return sharedPreference.getString("returnPolicy", "");
    }

    public void setReturnPolicy(String policy) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("returnPolicy", policy);
        prefsEditor.commit();

    }

    public String getPercentageVal() {
        return sharedPreference.getString("percentageVal", "");
    }

    public void setPercentageVal(String policy) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("percentageVal", policy);
        prefsEditor.commit();

    }

    public String getCharityId() {
        return sharedPreference.getString("charityId", "");
    }

    public void setCharityId(String charityId) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("charityId", charityId);
        prefsEditor.commit();

    }

    public String getDonationAmt() {
        return sharedPreference.getString("donationAmt", "");
    }

    public void setDonationAmt(String amt) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("donationAmt", amt);
        prefsEditor.commit();

    }

    public String getBDHeightIn() {
        return sharedPreference.getString("bd_height_in", "");
    }

    public void setBDHeightIn(String amt) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("bd_height_in", amt);
        prefsEditor.commit();

    }

    public String getBDHeightfoot() {
        return sharedPreference.getString("bd_height_foot", "");
    }

    public void setBDHeightfoot(String amt) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("bd_height_foot", amt);
        prefsEditor.commit();

    }

    public String getCostumeWashType() {
        return sharedPreference.getString("costume_wash_type", "");
    }

    public void setCostumeWashType(String clean) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("costume_wash_type", clean);
        prefsEditor.commit();

    }

    public String getWeghtLbs() {
        return sharedPreference.getString("weightLbs", "");
    }

    public void setWeghtLbs(String amt) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("weightLbs", amt);
        prefsEditor.commit();

    }

    public String getChestIn() {
        return sharedPreference.getString("chestIn", "");
    }

    public void setChestIn(String amt) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("chestIn", amt);
        prefsEditor.commit();

    }

    public String getWaistLbs() {
        return sharedPreference.getString("waistlbs", "");
    }

    public void setWaistLbs(String amt) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("waistlbs", amt);
        prefsEditor.commit();

    }

    public String getFunFact() {
        return sharedPreference.getString("funfact", "");
    }

    public void setFunFact(String amt) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("funfact", amt);
        prefsEditor.commit();

    }

    public String getQuestomQuality() {
        return sharedPreference.getString("costumeQuality", "");
    }

    public void setQuestomQuality(String amt) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("costumeQuality", amt);
        prefsEditor.commit();

    }

    public String getQuantity() {
        return sharedPreference.getString("qty", "");
    }

    public void setQuantity(String amt) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("qty", amt);
        prefsEditor.commit();

    }

    public String getFront() {
        return sharedPreference.getString("front", "");
    }

    public void setFront(String amt) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("front", amt);
        prefsEditor.commit();

    }

    public String getBack() {
        return sharedPreference.getString("back", "");
    }

    public void setBack(String amt) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("back", amt);
        prefsEditor.commit();

    }

    public String getSuggestedOrg() {
        return sharedPreference.getString("org", "");
    }

    public void setSuggestedOrg(String amt) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("org", amt);
        prefsEditor.commit();

    }

    public String getPhotoModel() {
        return sharedPreference.getString("photoModel", "");
    }

    public void setPhotoModel(String amt) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString("photoModel", amt);
        prefsEditor.commit();

    }

    public void setPreferencesBody(String key, String text) {
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putString(key, text);
        prefsEditor.commit();
    }

   /* public String getPreferencesString(String key) {
        return mAppPreference.getString(key, "");
    }
*/

}
