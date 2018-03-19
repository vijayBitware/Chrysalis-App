
package com.chrysalis.model.editcostume;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class EditData extends RealmObject {

    @SerializedName("imgScreen")
    @Expose
    private RealmList<ImgScreen> imgScreen = null;
    @SerializedName("costumedescScreen")
    @Expose
    private CostumedescScreen costumedescScreen;

    public RealmList<ImgScreen> getImgScreen() {
        return imgScreen;
    }

    public void setImgScreen(RealmList<ImgScreen> imgScreen) {
        this.imgScreen = imgScreen;
    }

    public CostumedescScreen getCostumedescScreen() {
        return costumedescScreen;
    }

    public void setCostumedescScreen(CostumedescScreen costumedescScreen) {
        this.costumedescScreen = costumedescScreen;
    }

}
