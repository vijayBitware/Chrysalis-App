package com.chrysalis.model;

import io.realm.RealmObject;

/**
 * Created by bitwarepc on 04-Jun-17.
 */

public class ModelImgScreenEdit extends RealmObject {

    public String imgPath;
    public String id;
    public String imgType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

}
