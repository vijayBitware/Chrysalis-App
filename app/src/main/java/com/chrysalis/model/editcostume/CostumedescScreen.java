
package com.chrysalis.model.editcostume;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class CostumedescScreen extends RealmObject {

    @SerializedName("wash_type")
    @Expose
    private String wash_type;
    @SerializedName("production_name")
    @Expose
    private String production_name;
    @SerializedName("costume_id")
    @Expose
    private String costumeId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("condition")
    @Expose
    private String condition;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("donation_amount")
    @Expose
    private String donationAmount;
    @SerializedName("charity_id")
    @Expose
    private String charityId;
    @SerializedName("weight_pounds")
    @Expose
    private String weightPounds;
    @SerializedName("weight_ounces")
    @Expose
    private String weightOunces;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("keyword")
    @Expose
    private String keyword;
    @SerializedName("theme_id")
    @Expose
    private String themeId;
    @SerializedName("theme")
    @Expose
    private String theme;
    @SerializedName("make_costume")
    @Expose
    private String makeCostume;
    @SerializedName("costume_time")
    @Expose
    private String costumeTime;
    @SerializedName("faq")
    @Expose
    private String faq;
    @SerializedName("fimquality")
    @Expose
    private String fimquality;
    @SerializedName("fun_fact")
    @Expose
    private String funFact;
    @SerializedName("shipping_option")
    @Expose
    private String shippingOption;
    @SerializedName("height-ft")
    @Expose
    private String heightFt;
    @SerializedName("height-in-body")
    @Expose
    private String heightInBody;
    @SerializedName("weight-lbs")
    @Expose
    private String weightLbs;
    @SerializedName("chest-in")
    @Expose
    private String chestIn;
    @SerializedName("waist-lbs")
    @Expose
    private String waistLbs;
    @SerializedName("length-in")
    @Expose
    private String lengthIn;
    @SerializedName("width-in")
    @Expose
    private String widthIn;
    @SerializedName("height-in")
    @Expose
    private String heightIn;
    @SerializedName("handling_time")
    @Expose
    private String handlingTime;
    @SerializedName("return_policy")
    @Expose
    private String return_policy;

    public String getWash_type() {
        return wash_type;
    }

    public void setWash_type(String wash_type) {
        this.wash_type = wash_type;
    }

    public String getProduction_name() {
        return production_name;
    }

    public void setProduction_name(String production_name) {
        this.production_name = production_name;
    }

    public String getReturn_policy() {
        return return_policy;
    }

    public void setReturn_policy(String return_policy) {
        this.return_policy = return_policy;
    }

    public String getCostumeId() {
        return costumeId;
    }

    public void setCostumeId(String costumeId) {
        this.costumeId = costumeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(String donationAmount) {
        this.donationAmount = donationAmount;
    }

    public String getCharityId() {
        return charityId;
    }

    public void setCharityId(String charityId) {
        this.charityId = charityId;
    }

    public String getWeightPounds() {
        return weightPounds;
    }

    public void setWeightPounds(String weightPounds) {
        this.weightPounds = weightPounds;
    }

    public String getWeightOunces() {
        return weightOunces;
    }

    public void setWeightOunces(String weightOunces) {
        this.weightOunces = weightOunces;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getMakeCostume() {
        return makeCostume;
    }

    public void setMakeCostume(String makeCostume) {
        this.makeCostume = makeCostume;
    }

    public String getCostumeTime() {
        return costumeTime;
    }

    public void setCostumeTime(String costumeTime) {
        this.costumeTime = costumeTime;
    }

    public String getFaq() {
        return faq;
    }

    public void setFaq(String faq) {
        this.faq = faq;
    }

    public String getFimquality() {
        return fimquality;
    }

    public void setFimquality(String fimquality) {
        this.fimquality = fimquality;
    }

    public String getFunFact() {
        return funFact;
    }

    public void setFunFact(String funFact) {
        this.funFact = funFact;
    }

    public String getShippingOption() {
        return shippingOption;
    }

    public void setShippingOption(String shippingOption) {
        this.shippingOption = shippingOption;
    }

    public String getHeightFt() {
        return heightFt;
    }

    public void setHeightFt(String heightFt) {
        this.heightFt = heightFt;
    }

    public String getHeightInBody() {
        return heightInBody;
    }

    public void setHeightInBody(String heightInBody) {
        this.heightInBody = heightInBody;
    }

    public String getWeightLbs() {
        return weightLbs;
    }

    public void setWeightLbs(String weightLbs) {
        this.weightLbs = weightLbs;
    }

    public String getChestIn() {
        return chestIn;
    }

    public void setChestIn(String chestIn) {
        this.chestIn = chestIn;
    }

    public String getWaistLbs() {
        return waistLbs;
    }

    public void setWaistLbs(String waistLbs) {
        this.waistLbs = waistLbs;
    }

    public String getLengthIn() {
        return lengthIn;
    }

    public void setLengthIn(String lengthIn) {
        this.lengthIn = lengthIn;
    }

    public String getWidthIn() {
        return widthIn;
    }

    public void setWidthIn(String widthIn) {
        this.widthIn = widthIn;
    }

    public String getHeightIn() {
        return heightIn;
    }

    public void setHeightIn(String heightIn) {
        this.heightIn = heightIn;
    }

    public String getHandlingTime() {
        return handlingTime;
    }

    public void setHandlingTime(String handlingTime) {
        this.handlingTime = handlingTime;
    }

}
