
package com.chrysalis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class UniqueFashion extends RealmObject {

    @SerializedName("144")
    @Expose
    private String _144;
    @SerializedName("154")
    @Expose
    private String _154;
    @SerializedName("177")
    @Expose
    private String _177;
    @SerializedName("179")
    @Expose
    private String _179;
    @SerializedName("180")
    @Expose
    private String _180;
    @SerializedName("203")
    @Expose
    private String _203;
    @SerializedName("208")
    @Expose
    private String _208;

    public String get144() {
        return _144;
    }

    public void set144(String _144) {
        this._144 = _144;
    }

    public String get154() {
        return _154;
    }

    public void set154(String _154) {
        this._154 = _154;
    }

    public String get177() {
        return _177;
    }

    public void set177(String _177) {
        this._177 = _177;
    }

    public String get179() {
        return _179;
    }

    public void set179(String _179) {
        this._179 = _179;
    }

    public String get180() {
        return _180;
    }

    public void set180(String _180) {
        this._180 = _180;
    }

    public String get203() {
        return _203;
    }

    public void set203(String _203) {
        this._203 = _203;
    }

    public String get208() {
        return _208;
    }

    public void set208(String _208) {
        this._208 = _208;
    }

}
