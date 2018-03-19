
package com.chrysalis.model.editcostume;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EditCostumeResult extends RealmObject {
    @PrimaryKey
    private String id;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private EditData data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public EditData getData() {
        return data;
    }

    public void setData(EditData data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
