
package com.chrysalis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class USERDTL {

    @SerializedName("usr_id")
    @Expose
    private int usrId;
    @SerializedName("log_idn")
    @Expose
    private int logIdn;
    @SerializedName("byr_idn")
    @Expose
    private int byrIdn;
    @SerializedName("rel_idn")
    @Expose
    private int relIdn;
    @SerializedName("saleEx_Nme")
    @Expose
    private String saleExNme;
    @SerializedName("SaleEx_Eml")
    @Expose
    private String saleExEml;
    @SerializedName("Nme")
    @Expose
    private String nme;
    @SerializedName("nme_Eml")
    @Expose
    private String nmeEml;
    @SerializedName("hh_dte_frm")
    @Expose
    private String hhDteFrm;
    @SerializedName("hh_dte_to")
    @Expose
    private String hhDteTo;
    @SerializedName("hh_dte_order")
    @Expose
    private String hhDteOrder;
    @SerializedName("allow_hh")
    @Expose
    private String allowHh;
    @SerializedName("bid_dte_frm")
    @Expose
    private String bidDteFrm;
    @SerializedName("bid_dte_to")
    @Expose
    private String bidDteTo;
    @SerializedName("allow_bid")
    @Expose
    private String allowBid;
    @SerializedName("paymentTerm")
    @Expose
    private String paymentTerm;

    public int getUsrId() {
        return usrId;
    }

    public void setUsrId(int usrId) {
        this.usrId = usrId;
    }

    public int getLogIdn() {
        return logIdn;
    }

    public void setLogIdn(int logIdn) {
        this.logIdn = logIdn;
    }

    public int getByrIdn() {
        return byrIdn;
    }

    public void setByrIdn(int byrIdn) {
        this.byrIdn = byrIdn;
    }

    public int getRelIdn() {
        return relIdn;
    }

    public void setRelIdn(int relIdn) {
        this.relIdn = relIdn;
    }

    public String getSaleExNme() {
        return saleExNme;
    }

    public void setSaleExNme(String saleExNme) {
        this.saleExNme = saleExNme;
    }

    public String getSaleExEml() {
        return saleExEml;
    }

    public void setSaleExEml(String saleExEml) {
        this.saleExEml = saleExEml;
    }

    public String getNme() {
        return nme;
    }

    public void setNme(String nme) {
        this.nme = nme;
    }

    public String getNmeEml() {
        return nmeEml;
    }

    public void setNmeEml(String nmeEml) {
        this.nmeEml = nmeEml;
    }

    public String getHhDteFrm() {
        return hhDteFrm;
    }

    public void setHhDteFrm(String hhDteFrm) {
        this.hhDteFrm = hhDteFrm;
    }

    public String getHhDteTo() {
        return hhDteTo;
    }

    public void setHhDteTo(String hhDteTo) {
        this.hhDteTo = hhDteTo;
    }

    public String getHhDteOrder() {
        return hhDteOrder;
    }

    public void setHhDteOrder(String hhDteOrder) {
        this.hhDteOrder = hhDteOrder;
    }

    public String getAllowHh() {
        return allowHh;
    }

    public void setAllowHh(String allowHh) {
        this.allowHh = allowHh;
    }

    public String getBidDteFrm() {
        return bidDteFrm;
    }

    public void setBidDteFrm(String bidDteFrm) {
        this.bidDteFrm = bidDteFrm;
    }

    public String getBidDteTo() {
        return bidDteTo;
    }

    public void setBidDteTo(String bidDteTo) {
        this.bidDteTo = bidDteTo;
    }

    public String getAllowBid() {
        return allowBid;
    }

    public void setAllowBid(String allowBid) {
        this.allowBid = allowBid;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

}
