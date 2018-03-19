package com.chrysalis.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.chrysalis.R;
import com.chrysalis.activity.AddEditCostumeActivity;
import com.chrysalis.domain.AppPreference;
import com.chrysalis.domain.Config;
import com.chrysalis.domain.UploadStuff;
import com.chrysalis.domain.Utility;
import com.chrysalis.domain.api.APIRequest;
import com.chrysalis.model.editcostume.EditCostumeResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static android.app.Activity.RESULT_OK;

/**
 * Created by samsung on 28/7/17.
 */

public class UploadPhotoFragment extends BaseFragment {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static ArrayList<String> arrPaths = new ArrayList<>();
    private final int SELECT_PHOTO = 1;
    private final int SELECT_PHOTO1 = 2;
    @BindView(R.id.ivFrontView)
    ImageView ivFrontView;
    @BindView(R.id.ivBackView)
    ImageView ivBackView;
    @BindView(R.id.ivCloseOneFront)
    ImageView ivCloseOneFront;
    @BindView(R.id.ivCloseTwoBack)
    ImageView ivCloseTwoBack;
    @BindView(R.id.btnAddProductNext)
    Button btnAddProductNext;
    Boolean idCamera = false, idBackView = false;
    String filePathIdCard = "", filePathBackView = "";
    AppPreference appPreference;

    // public  static String costumeID = "479";
    private Uri fileUriId;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.activity_addproducts, container, false);
            ButterKnife.bind(this, view);
            appPreference = new AppPreference(getActivity());
            System.out.println("## Mode : " + Config.MODE);

            if (Config.MODE.equals(Config.EDIT_MODE)) {
                getCostumeEditData();
            }
        }
        return view;
    }

    @OnClick(R.id.ivFrontView)
    public void frontImageSelected() {
        selectFrontImage();
    }

    @OnClick(R.id.ivBackView)
    public void backImageSelected() {
        selectBackView();
    }

    @OnClick(R.id.btnAddProductNext)
    public void onClickNext() {

        if (Config.MODE.equals(Config.EDIT_MODE)) {
            Toast.makeText(getActivity(), "CALL HERE EDIT IMAGES API", Toast.LENGTH_LONG).show();
            CostumeDescriptionFragment costumeDescriptionFragment = new CostumeDescriptionFragment();
            ((AddEditCostumeActivity) getActivity()).replaceFragment(costumeDescriptionFragment);
        } else {
            CostumeDescriptionFragment costumeDescriptionFragment = new CostumeDescriptionFragment();
            ((AddEditCostumeActivity) getActivity()).replaceFragment(costumeDescriptionFragment);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //((AddEditCostumeActivity) getActivity()).getSupportActionBar().setTitle("Upload Photo");
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

    private void getCostumeEditData() {
        if (Utility.isNetworkAvailable(getActivity())) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("costume_id", appPreference.getCostumeId());
                String CategoryURL = Config.BASE_URL + "getAllDataForEdit";
                new APIRequest(getActivity(), jsonObject, CategoryURL, Config.API_EDIT_COSTUME, Config.POST);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void getEditData(EditCostumeResult editCostumeResult) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        editCostumeResult.setId(appPreference.getCostumeId());
        realm.copyToRealmOrUpdate(editCostumeResult);
        realm.commitTransaction();

        //get path of images here & call  the grid adapter
    }

    private void selectFrontImage() {
        final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {
                    idCamera = true;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUriId = UploadStuff.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriId);
                    startActivityForResult(intent, SELECT_PHOTO);
                } else if (options[item].equals("Gallery")) {
                    idCamera = false;
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");

                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectBackView() {
        final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {
                    idBackView = true;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUriId = UploadStuff.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriId);
                    startActivityForResult(intent, SELECT_PHOTO1);
                } else if (options[item].equals("Gallery")) {
                    idBackView = false;
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (idCamera) {
                        filePathIdCard = fileUriId.getPath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        final Bitmap bitmap = BitmapFactory.decodeFile(filePathIdCard, options);
                        ivFrontView.setImageBitmap(bitmap);
                    } else {                      // if idCard pic  selected from Gallery
                        Uri selectedImageUri = data.getData();
                        String imagepath = getPath(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                        ivFrontView.setImageBitmap(bitmap);
                        filePathIdCard = imagepath;
                    }
                    arrPaths.add(filePathIdCard);
                    break;
                }

            case SELECT_PHOTO1:
                if (resultCode == RESULT_OK) {
                    if (idBackView) {
                        filePathBackView = fileUriId.getPath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        final Bitmap bitmap = BitmapFactory.decodeFile(filePathBackView, options);
                        ivBackView.setImageBitmap(bitmap);
                    } else {                      // if idCard pic  selected from Gallery
                        Uri selectedImageUri = data.getData();
                        String imagepath = getPath(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                        ivBackView.setImageBitmap(bitmap);
                        filePathBackView = imagepath;
                    }
                    arrPaths.add(filePathBackView);
                    break;
                }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
