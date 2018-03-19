package com.chrysalis.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chrysalis.R;
import com.chrysalis.activity.AddEditCostumeActivity;
import com.chrysalis.activity.ProductListingScreen;
import com.chrysalis.domain.AndroidMultiPartEntity;
import com.chrysalis.domain.AppPreference;
import com.chrysalis.domain.Config;
import com.chrysalis.domain.UploadStuff;
import com.chrysalis.domain.Utility;
import com.chrysalis.domain.api.APIRequest;
import com.chrysalis.model.editcostume.EditCostumeResult;
import com.chrysalis.model.editcostume.ImgScreen;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import io.realm.Realm;
import io.realm.RealmList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by bitwarepc on 06-Aug-17.
 */

public class NewUploadFragment extends Fragment implements View.OnClickListener {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int SELECT_PHOTO1 = 102;
    AppPreference appPreference;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.btnAddProductNext)
    Button btnAddProductNext;
    View view;
    private RecyclerView rvPhotos;
    private List<ModelUploadImages> arrModelphotoses = new ArrayList<>();
    private Uri fileUriBack = null;
    private String filePathIdCard;
    private boolean idCamera;
    private AdapterUploadImages gridAdapter;
    private int pos;
    private List<ModelUploadImages> arrExistingEditImages = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_addproducts, container, false);
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            ButterKnife.bind(this, view);
            inIt();
        }

        return view;
    }

    private void inIt() {
        adapterInIt();
        appPreference = new AppPreference(getActivity());
        System.out.println("## Mode : " + Config.MODE);

        if (Config.MODE.equals(Config.EDIT_MODE)) {
            getCostumeEditData();
        }
    }

    @OnClick(R.id.btnAddProductNext)
    public void OnNextClick() {
        Gson gson = new Gson();
        String json = gson.toJson(arrModelphotoses);
        appPreference.setPhotoModel(json);
        System.out.println(">>> Total Size of images :" + arrModelphotoses.size());

        if (Config.MODE.equals(Config.EDIT_MODE)) {
           /* Toast.makeText(getActivity(), "CALL HERE EDIT IMAGES API", Toast.LENGTH_LONG).show();
            CostumeDescriptionFragment costumeDescriptionFragment = new CostumeDescriptionFragment();
            ((AddEditCostumeActivity) getActivity()).replaceFragment(costumeDescriptionFragment);*/
            callUpdateEditImagesAPI();
        } else {
            CostumeDescriptionFragment costumeDescriptionFragment = new CostumeDescriptionFragment();
            ((AddEditCostumeActivity) getActivity()).replaceFragment(costumeDescriptionFragment);
        }
    }

    private void callUpdateEditImagesAPI() {
        if (Utility.isNetworkAvailable(getActivity())) {
            try {
              /*  JSONObject jsonObject = new JSONObject();
                jsonObject.put("costume_id", appPreference.getCostumeId());
                String CategoryURL = Config.BASE_URL + "getAllDataForEdit";

                new APIRequest(getActivity(), jsonObject, CategoryURL, Config.API_EDIT_COSTUME, Config.POST);*/
                new callUpdateImagesAPI().execute("");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.NoNetworkMsg), Toast.LENGTH_SHORT).show();
        }
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

        ArrayList<ModelUploadImages> arrEditedReceivedImages = new ArrayList<>();
        RealmList<ImgScreen> arrEditImages = editCostumeResult.getData().getImgScreen();
        for (int i = 0; i < arrEditImages.size(); i++) {
            ModelUploadImages modelUploadImages = new ModelUploadImages();
            modelUploadImages.setId(arrEditImages.get(i).getId());
            modelUploadImages.setPhotoLocation(arrEditImages.get(i).getImage());
            modelUploadImages.setType(arrEditImages.get(i).getType());
            if (i == 0) {
                modelUploadImages.setPhotoName("Front View*");
            } else if (i == 1) {
                modelUploadImages.setPhotoName("Back View*");
            } else {
                modelUploadImages.setPhotoName("Additional");
            }

            arrEditedReceivedImages.add(modelUploadImages);
        }
        if (arrEditedReceivedImages.size() <= 9) {
            ModelUploadImages modelphotos = new ModelUploadImages();
            modelphotos.setPhotoName("Additional");
            modelphotos.setPhotoLocation("");
            arrEditedReceivedImages.add(modelphotos);
        }

        gridAdapter = new AdapterUploadImages(getActivity(), arrEditedReceivedImages, this, Config.EDIT_MODE);
        rvPhotos.setAdapter(gridAdapter);
        //get path of images here & call  the grid adapter
    }

    private void adapterInIt() {
        for (int i = 0; i < 3; i++) {
            ModelUploadImages modelphotos = new ModelUploadImages();
            if (i == 0) {
                modelphotos.setPhotoName("Front View*");
            } else if (i == 1) {
                modelphotos.setPhotoName("Back View*");
            } else {
                modelphotos.setPhotoName("Additional");
            }
            modelphotos.setPhotoLocation("");
            arrModelphotoses.add(modelphotos);
        }

        rvPhotos = view.findViewById(R.id.rvPhotos);
        rvPhotos.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        gridAdapter = new AdapterUploadImages(getActivity(), arrModelphotoses, this, Config.ADD_MODE);
        rvPhotos.setAdapter(gridAdapter);
    }

    public void uploadImage(final int adapterPosition, List<ModelUploadImages> arrExistingImgData) {

        pos = adapterPosition;
        arrExistingEditImages = arrExistingImgData;
        final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {
                    idCamera = true;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUriBack = UploadStuff.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    System.out.println("FILE BACK URL :" + fileUriBack);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriBack);
                    startActivityForResult(intent, SELECT_PHOTO1);

                } else if (options[item].equals("Gallery")) {
                    idCamera = false;
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

    private void addBlankView(int size) {
        if (pos >= 2 && !arrModelphotoses.get(pos).getPhotoLocation().equals("")) {
            System.out.println("method called success" + arrModelphotoses.get(pos).getPhotoLocation());
            for (int i = size; i < size + 1; i++) {
                ModelUploadImages modelphotos = new ModelUploadImages();
                modelphotos.setPhotoName("Additional");
                modelphotos.setPhotoLocation("");
                arrModelphotoses.add(modelphotos);
            }
            gridAdapter = new AdapterUploadImages(getActivity(), arrModelphotoses, this, Config.ADD_MODE);
            rvPhotos.setAdapter(gridAdapter);
        }
    }

    public void deletePhoto(int position) {
        arrModelphotoses.remove(position);
        //gridAdapter.notifyDataSetChanged();
        gridAdapter = new AdapterUploadImages(getActivity(), arrModelphotoses, this, Config.ADD_MODE);
        rvPhotos.setAdapter(gridAdapter);
        System.out.println("after delte " + arrModelphotoses.get(0).getPhotoName());
        if (arrModelphotoses.size() <= 2) {
            for (int i = arrModelphotoses.size() - 1; i < 2; i++) {
                ModelUploadImages modelphotos = new ModelUploadImages();
                modelphotos.setPhotoName("Additional");
                modelphotos.setPhotoLocation("");
                arrModelphotoses.add(modelphotos);
            }
            gridAdapter = new AdapterUploadImages(getActivity(), arrModelphotoses, this, Config.ADD_MODE);
            rvPhotos.setAdapter(gridAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrModelphotoses);
        editor.putString("PhotoModel", json);
        editor.commit();
       /* if (arrModelphotoses.get(0).getPhotoLocation().equals("") || arrModelphotoses.get(1).getPhotoLocation().equals("")) {
            Toast.makeText(getActivity(), "Front and back Images are Mandatory", Toast.LENGTH_SHORT).show();
        } else {
            s
            Toast.makeText(getActivity(), "NEXT PROCESS", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(NewUploadPhotoFragment.this, SecondActivity.class);
            //startActivity(intent);
        }*/

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECT_PHOTO1:
                if (resultCode == RESULT_OK) {
                    if (idCamera) {
                        filePathIdCard = fileUriBack.getPath();

                        if (pos <= arrModelphotoses.size() - 1) {
                            arrModelphotoses.get(pos).setPhotoLocation(filePathIdCard);
                            if (pos == 0) {
                                arrModelphotoses.get(pos).setPhotoName("Front View*");
                            } else if (pos == 1) {
                                arrModelphotoses.get(pos).setPhotoName("Back View*");
                            } else {
                                arrModelphotoses.get(pos).setPhotoName("Additional");
                            }
                            gridAdapter.notifyDataSetChanged();
                        } else {
                            ModelUploadImages modelphotos = new ModelUploadImages();
                            modelphotos.setPhotoName("Additional");
                            modelphotos.setPhotoLocation(filePathIdCard);
                            if (arrExistingEditImages.size() > 0) {
                                arrModelphotoses.addAll(arrExistingEditImages);
                            }
                            arrModelphotoses.add(modelphotos);
                            gridAdapter.notifyDataSetChanged();

                        }
                    } else {
                        //if idCard pic  selected from Gallery
                        Uri selectedImageUri = data.getData();
                        String imagepath = getPath(selectedImageUri);
                        filePathIdCard = imagepath;
                        System.out.println(">>> Galllery filePathIdCard :" + filePathIdCard);
                        if (pos <= arrModelphotoses.size() - 1) {
                            arrModelphotoses.get(pos).setPhotoLocation(filePathIdCard);
                            if (pos == 0) {
                                arrModelphotoses.get(pos).setPhotoName("Front View*");
                            } else if (pos == 1) {
                                arrModelphotoses.get(pos).setPhotoName("Back View*");
                            } else {
                                arrModelphotoses.get(pos).setPhotoName("Additional");
                            }
                            gridAdapter.notifyDataSetChanged();

                        } else {
                            ModelUploadImages modelphotos = new ModelUploadImages();
                            modelphotos.setPhotoName("Additional");
                            modelphotos.setPhotoLocation(filePathIdCard);
                            System.out.println("Existing images size : " + arrExistingEditImages.size());
                            if (arrExistingEditImages.size() > 0) {
                                arrModelphotoses.addAll(arrExistingEditImages);
                            }
                            arrModelphotoses.add(modelphotos);
                            System.out.println(">>>> Aftr size is :" + arrModelphotoses.size());
                            gridAdapter.notifyDataSetChanged();
                        }
                    }
                    if (pos < 9) {
                        addBlankView(arrModelphotoses.size());
                    } else {
                        Toast.makeText(getActivity(), "Cannot add more than 10 images", Toast.LENGTH_SHORT).show();
                    }
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

    @OnClick(R.id.tvCancel)
    public void OnCacelClick() {
        Intent i = new Intent(getActivity(), ProductListingScreen.class);
        startActivity(i);
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

    class callUpdateImagesAPI extends AsyncTask<String, Void, String> {
        ProgressDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(getActivity());
            p.setMessage("In Progress..");
            p.setCanceledOnTouchOutside(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseString = null;
            org.apache.http.client.HttpClient httpclient = new org.apache.http.impl.client.DefaultHttpClient();
            org.apache.http.client.methods.HttpPost httppost = new org.apache.http.client.methods.HttpPost(Config.BASE_URL + "uploadImageMultiple");
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                //publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

/*
                System.out.println(">>> FronOne : "+strFrontPath);
                if(!strFrontPath.equals("")){
                    File sourceFile1 = new File(strFrontPath);
                    entity.addPart("front",   new FileBody(sourceFile1));
                    entity.addPart("type1",  new StringBody("1"));
                }

                System.out.println(">>> strBackOne : "+strBackPath);
                if(!strBackPath.equals("")){
                    File sourceFile2 = new File(strBackPath);
                    entity.addPart("back",  new FileBody(sourceFile2));
                    entity.addPart("type2", new StringBody("2"));
                }

                for(int i = 0 ; i < arrImgsEditPath.size();i++){
                    String testPath = arrImgsEditPath.get(i).toString().trim();
                    System.out.println(">>> Img Path in edit loop is :"+testPath);
                    File sourceFileObj = new File(testPath);
                    entity.addPart("pic"+i,  new FileBody(sourceFileObj));
                    //System.out.println("ImagePth - "+i+"-- "+arrImgsEditPath.get(i));
                }*/
                String gsonPhotoString = appPreference.getPhotoModel();
                Gson gson = new Gson();
                Type type = new TypeToken<List<ModelUploadImages>>() {
                }.getType();
                List<ModelUploadImages> images = gson.fromJson(gsonPhotoString, type);
                System.out.println(">>> Received images arr size : " + images.size());

                File sourceFile1 = new File(images.get(0).getPhotoLocation().toString().trim());
                entity.addPart("front", new FileBody(sourceFile1));
                entity.addPart("type1", new StringBody("1"));

                File sourceFile2 = new File(images.get(1).getPhotoLocation().toString().trim());
                entity.addPart("back", new FileBody(sourceFile2));
                entity.addPart("type2", new StringBody("2"));

                if (images.size() > 2) {
                    images.remove(0);
                    images.remove(1);
                    for (int i = 0; i < images.size(); i++) {
                        String testPath = images.get(i).getPhotoLocation().toString().trim();
                        File sourceFileObj = new File(testPath);
                        entity.addPart("pic" + i, new FileBody(sourceFileObj));
                    }
                    entity.addPart("type3", new StringBody("3"));

                }

                entity.addPart("costume_id", new StringBody(appPreference.getCostumeId()));
                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                responseString = EntityUtils.toString(r_entity);

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                p.dismiss();
                System.out.println(">>> Costume Description result : " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String resCode = jsonObject.getString("result");
                    if (resCode.equalsIgnoreCase("success")) {
                        CostumeDescriptionFragment costumeDescriptionFragment = new CostumeDescriptionFragment();
                        ((AddEditCostumeActivity) getActivity()).replaceFragment(costumeDescriptionFragment);
                    } else {
                        String strMessage = jsonObject.getString("message");
                        Toast.makeText(getActivity(), strMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                p.dismiss();
            }
        }
    }

}
