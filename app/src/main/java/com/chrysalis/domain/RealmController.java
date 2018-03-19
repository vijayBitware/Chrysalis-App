package com.chrysalis.domain;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.chrysalis.model.ResponseCategoryList;
import com.chrysalis.model.editcostume.EditCostumeResult;

import io.realm.Realm;

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    public ResponseCategoryList getCategoryResponse(String id) {
        return realm.where(ResponseCategoryList.class).equalTo("id", id).findFirst();
    }

    public EditCostumeResult getEditResponse(String costumeID) {
        return realm.where(EditCostumeResult.class).equalTo("id", costumeID).findFirst();
    }

}
