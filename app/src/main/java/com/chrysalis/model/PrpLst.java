
package com.chrysalis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrpLst {

    @SerializedName("val")
    @Expose
    private String val;
    @SerializedName("prt1")
    @Expose
    private String prt1;
    @SerializedName("srt")
    @Expose
    private int srt;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getPrt1() {
        return prt1;
    }

    public void setPrt1(String prt1) {
        this.prt1 = prt1;
    }

    public int getSrt() {
        return srt;
    }

    public void setSrt(int srt) {
        this.srt = srt;
    }

}
