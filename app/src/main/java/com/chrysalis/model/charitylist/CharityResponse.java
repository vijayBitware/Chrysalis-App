
package com.chrysalis.model.charitylist;

import com.chrysalis.domain.api.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;

public class CharityResponse extends BaseResponse {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private RealmList<Datum> data = null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public RealmList<Datum> getData() {
        return data;
    }

    public void setData(RealmList<Datum> data) {
        this.data = data;
    }
}
