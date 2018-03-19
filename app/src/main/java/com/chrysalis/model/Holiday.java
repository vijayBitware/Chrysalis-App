package com.chrysalis.model;

import io.realm.RealmObject;

/**
 * Created by samsung on 29/7/17.
 */

public class Holiday extends RealmObject {

    private String s;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}
