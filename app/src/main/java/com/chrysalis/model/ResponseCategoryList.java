
package com.chrysalis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ResponseCategoryList extends RealmObject {
    @PrimaryKey
    private String id;
    @SerializedName("0")
    @Expose
    private _0 _0;
    @SerializedName("1")
    @Expose
    private _1 _1;
    @SerializedName("2")
    @Expose
    private _2 _2;
    @SerializedName("3")
    @Expose
    private _3 _3;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private Data data;

    public _0 get0() {
        return _0;
    }

    public void set0(_0 _0) {
        this._0 = _0;
    }

    public _1 get1() {
        return _1;
    }

    public void set1(_1 _1) {
        this._1 = _1;
    }

    public _2 get2() {
        return _2;
    }

    public void set2(_2 _2) {
        this._2 = _2;
    }

    public _3 get3() {
        return _3;
    }

    public void set3(_3 _3) {
        this._3 = _3;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
