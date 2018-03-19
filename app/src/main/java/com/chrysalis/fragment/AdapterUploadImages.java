package com.chrysalis.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.chrysalis.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-PC on 7/8/2017.
 */

public class AdapterUploadImages extends RecyclerView.Adapter<AdapterUploadImages.ViewHolder> {

    NewUploadFragment mPhotoFragment;
    Context context;
    String mMode = "";
    AQuery aQuery;
    private LayoutInflater mInflater;
    private List<ModelUploadImages> arModelphotoses = new ArrayList<>();

    // data is passed into the constructor
    public AdapterUploadImages(Context context, List<ModelUploadImages> data, NewUploadFragment mainActivity, String mode) {
        this.mInflater = LayoutInflater.from(context);
        this.arModelphotoses = data;
        this.context = context;
        this.mPhotoFragment = mainActivity;
        this.mMode = mode;
        aQuery = new AQuery(context);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.new_row_addproducts, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.myTextView.setText(arModelphotoses.get(position).getPhotoName());

        if (arModelphotoses.get(position).getPhotoLocation().equals("")) {
            holder.ivDelete.setVisibility(View.GONE);
        } else {
            holder.ivDelete.setVisibility(View.VISIBLE);
        }

        if (!arModelphotoses.get(position).getPhotoLocation().isEmpty()) {
            Bitmap myBitmap = null;

            if (!arModelphotoses.get(position).getPhotoLocation().contains("http")) {
                System.out.println(">> ADD BITMAP");
                File file = new File(arModelphotoses.get(position).getPhotoLocation());
                myBitmap = ImageUtils.getInstant().getCompressedBitmap(file.getAbsolutePath());
                int width = myBitmap.getWidth();
                int height = myBitmap.getHeight();
                if (width > height) {
                    System.out.println("landscape at position " + position + 1);
                    holder.ivCamHolder.setScaleType(ImageView.ScaleType.FIT_CENTER);
                } else {
                    System.out.println("portrait at position " + position + 1);
                }

                holder.ivCamHolder.setImageBitmap(myBitmap);
            } else {
                System.out.println(">> EDIT BITMAP");
                String imgPath = arModelphotoses.get(position).getPhotoLocation().toString().trim();
                aQuery.id(holder.ivCamHolder).image(arModelphotoses.get(position).getPhotoLocation().toString().trim());
            }
        }

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mPhotoFragment.deletePhoto(position);
                arModelphotoses.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return arModelphotoses.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView myTextView;
        public ImageView ivCamHolder, ivDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tv_productPrice);
            ivCamHolder = itemView.findViewById(R.id.ivCamHolder);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivCamHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mPhotoFragment.uploadImage(getAdapterPosition(), arModelphotoses);
        }
    }
}