
package com.chrysalis.model.deleteimage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("imgScreen")
    @Expose
    private List<ImgScreen> imgScreen = null;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<ImgScreen> getImgScreen() {
        return imgScreen;
    }

    public void setImgScreen(List<ImgScreen> imgScreen) {
        this.imgScreen = imgScreen;
    }

}
