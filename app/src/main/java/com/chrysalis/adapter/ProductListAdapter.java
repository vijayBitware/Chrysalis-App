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
import com.chrysalis.activity.ClickListener;
import com.chrysalis.model.ProductModelClass;

import java.util.ArrayList;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by bitwarepc on 04-Sep-17.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static ClickListener itemListener;
    private final int SELECT_PHOTO = 1;
    Context context;
    String filePathIdCard = "";
    private ArrayList<ProductModelClass> arrList;
    private Uri fileUriId;

    public ProductListAdapter(Context context, ArrayList<ProductModelClass> horizontalList) {
        this.arrList = horizontalList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_grid, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtName.setText(arrList.get(position).getImgName());
        //holder.txtId.setText(arrList.get(position).getImgId());
        if (arrList.get(position).getImg().equalsIgnoreCase("")) {
            //Glide.with(context).load(R.drawable.cam_holder).apply(new RequestOptions().centerCrop()).into(holder.ivImgView);
            Picasso.with(context)
                    .load(R.drawable.cam_holder)
                    .into(holder.ivImgView);
            holder.ivCloseOneFront.setVisibility(View.GONE);
        } else {
            // Glide.with(context).load(arrList.get(position).getImg()).apply(new RequestOptions().centerCrop()).into(holder.ivImgView);
            Picasso.with(context)
                    .load(arrList.get(position).getImg())
                    .into(holder.ivImgView);
            holder.ivCloseOneFront.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return arrList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtId;
        public ImageView ivImgView, ivCloseOneFront;

        public MyViewHolder(View view) {
            super(view);
            //txtId = (TextView) view.findViewById(R.id.txtId);
            txtName = view.findViewById(R.id.txtName);
            ivImgView = view.findViewById(R.id.ivImgView);
            ivCloseOneFront = view.findViewById(R.id.ivCloseOneFront);
            //  tvRemove = (TextView) view.findViewById(R.id.tvRemove);

        }
    }
}
