package com.chrysalis.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.chrysalis.R;
import com.chrysalis.domain.Config;
import com.chrysalis.domain.Utility;
import com.chrysalis.domain.api.APIRequest;
import com.chrysalis.fragment.CostumeDescriptionFragment;
import com.chrysalis.model.ResponseCategoryList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AddEditCostumeActivity extends AppCompatActivity {
    @BindView(R.id.container)
    FrameLayout container;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_costume);
        ButterKnife.bind(this);
        // setToolbar();
        getCategoryList();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void getCategoryList() {
        if (Utility.isNetworkAvailable(AddEditCostumeActivity.this)) {
            try {
                JSONObject jsonObject = new JSONObject();
                String CategoryURL = Config.BASE_URL + "categoryList";

                new APIRequest(AddEditCostumeActivity.this, jsonObject, CategoryURL, Config.API_CATEGORY_LIST, Config.POST);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(AddEditCostumeActivity.this, getApplicationContext().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();

        }
    }

    @Subscribe
    public void getCategoryResponse(ResponseCategoryList categoryList) {
        System.out.println("ResponseCategoryList : " + categoryList);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        categoryList.setId("1");
        realm.copyToRealmOrUpdate(categoryList);
        realm.commitTransaction();

        launchFragment();

    }

    private void launchFragment() {
        //NewUploadFragment photoFragment = new NewUploadFragment();
        // UploadPhotoFragment photoFragment = new UploadPhotoFragment();
        CostumeDescriptionFragment costumeDescriptionFragment = new CostumeDescriptionFragment();
        replaceFragment(costumeDescriptionFragment);
    }

    /**
     * menu click
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * is used to replace the fragment in the conatiner specified
     * in replace method.
     *
     * @param fragment
     */
    public void replaceFragment(Fragment fragment) {
        String className = fragment.getClass().getName();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(className);
        fragmentTransaction.replace(R.id.container, fragment, className).commit();
    }

    public void clearBackstack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public void popFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent i = new Intent(AddEditCostumeActivity.this, AddProductsActivityNew.class);
        //startActivity(i);
        finish();
    }
}
