
package com.chrysalis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PRPLIST {

    @SerializedName("prp")
    @Expose
    private String prp;
    @SerializedName("dataTyp")
    @Expose
    private String dataTyp;
    @SerializedName("prtNme")
    @Expose
    private String prtNme;
    @SerializedName("rnk")
    @Expose
    private String rnk;
    @SerializedName("flg")
    @Expose
    private String flg;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("srt")
    @Expose
    private int srt;
    @SerializedName("prpLst")
    @Expose
    private List<PrpLst> prpLst = null;

    public String getPrp() {
        return prp;
    }

    public void setPrp(String prp) {
        this.prp = prp;
    }

    public String getDataTyp() {
        return dataTyp;
    }

    public void setDataTyp(String dataTyp) {
        this.dataTyp = dataTyp;
    }

    public String getPrtNme() {
        return prtNme;
    }

    public void setPrtNme(String prtNme) {
        this.prtNme = prtNme;
    }

    public String getRnk() {
        return rnk;
    }

    public void setRnk(String rnk) {
        this.rnk = rnk;
    }

    public String getFlg() {
        return flg;
    }

    public void setFlg(String flg) {
        this.flg = flg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSrt() {
        return srt;
    }

    public void setSrt(int srt) {
        this.srt = srt;
    }

    public List<PrpLst> getPrpLst() {
        return prpLst;
    }

    public void setPrpLst(List<PrpLst> prpLst) {
        this.prpLst = prpLst;
    }

}
