package com.chrysalis.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chrysalis.R;
import com.chrysalis.model.ModelProductListing;

import java.util.ArrayList;

/**
 * Created by Admin on 05-12-2016.
 */
public class AdapterUploadNewImages extends RecyclerView.Adapter<AdapterUploadNewImages.MyViewHolder> {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private final int SELECT_PHOTO = 1;
    Context context;
    String filePathIdCard = "";
    private ArrayList<ModelProductListing> arrSearchData;
    private Uri fileUriId;

    public AdapterUploadNewImages(Context context, ArrayList<ModelProductListing> horizontalList) {
        this.arrSearchData = horizontalList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_products, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_productPrice.setText(arrSearchData.get(position).getName());
        holder.ivCamHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  OpenMediaFile();
                //viewNextBlock(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrSearchData.size();
    }

    /*private void OpenMediaFile() {
            final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("AddPhoto");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("camera"))
                    {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        fileUriId = UploadStuff.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriId);
                        context.startActivityForResult(intent, SELECT_PHOTO);

                    }else if (options[item].equals("Gallery"))
                    {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image*//*");
                        startActivityForResult(photoPickerIntent, SELECT_PHOTO);

                    } else if (options[item].equals("cancel")) {
                        dialog.dismiss();
                    }
                }

            });
            builder.show();
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == context.RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    String imagepath = getPath(selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                    holder.ivCamHolder.setImageBitmap(bitmap);
                    filePathIdCard = imagepath;
                    *//*if (idCamera) {
                        filePathIdCard = fileUriId.getPath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        final Bitmap bitmap = BitmapFactory.decodeFile(filePathIdCard, options);
                        idcard.setImageBitmap(bitmap);

                    } else {
                        //if idCard pic  selected from Gallery
                        Uri selectedImageUri = data.getData();
                        String imagepath = getPath(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                        idcard.setImageBitmap(bitmap);
                        filePathIdCard = imagepath;
                    }*//*
                    break;
                }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void viewNextBlock(int recPosition) {

        if(recPosition == arrSearchData.size()-1){
            ModelProductListing modelProductListing2 = new ModelProductListing();
            modelProductListing2.setName("Additional "+recPosition);
            arrSearchData.add(modelProductListing2);

            notifyDataSetChanged();

        }
    }*/

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_productPrice;
        public ImageView ivCamHolder;

        public MyViewHolder(View view) {
            super(view);
            tv_productPrice = view.findViewById(R.id.tv_productPrice);
            ivCamHolder = view.findViewById(R.id.ivCamHolder);
            //  tvRemove = (TextView) view.findViewById(R.id.tvRemove);
        }
    }
}
