
package com.chrysalis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestServiceResponse {

    @SerializedName("USERDTL")
    @Expose
    private List<USERDTL> uSERDTL = null;
    @SerializedName("PRPLIST")
    @Expose
    private List<PRPLIST> pRPLIST = null;

    public List<USERDTL> getUSERDTL() {
        return uSERDTL;
    }

    public void setUSERDTL(List<USERDTL> uSERDTL) {
        this.uSERDTL = uSERDTL;
    }

    public List<PRPLIST> getPRPLIST() {
        return pRPLIST;
    }

    public void setPRPLIST(List<PRPLIST> pRPLIST) {
        this.pRPLIST = pRPLIST;
    }

}
