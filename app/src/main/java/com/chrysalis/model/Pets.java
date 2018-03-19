
package com.chrysalis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Pets extends RealmObject {

    @SerializedName("139")
    @Expose
    private String _139;
    @SerializedName("164")
    @Expose
    private String _164;
    @SerializedName("184")
    @Expose
    private String _184;

    public String get139() {
        return _139;
    }

    public void set139(String _139) {
        this._139 = _139;
    }

    public String get164() {
        return _164;
    }

    public void set164(String _164) {
        this._164 = _164;
    }

    public String get184() {
        return _184;
    }

    public void set184(String _184) {
        this._184 = _184;
    }

}
