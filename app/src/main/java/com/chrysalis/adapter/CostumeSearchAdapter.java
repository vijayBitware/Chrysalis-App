package com.chrysalis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chrysalis.R;

import java.util.ArrayList;

/**
 * Created by Admin on 05-12-2016.
 */
public class CostumeSearchAdapter extends RecyclerView.Adapter<CostumeSearchAdapter.MyViewHolder> {

    Context context;
    private ArrayList<String> arrSearchData;

    public CostumeSearchAdapter(Context context, ArrayList<String> horizontalList) {
        this.arrSearchData = horizontalList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_recyclerview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtView.setText(arrSearchData.get(position) + " ");
        holder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrSearchData.remove(arrSearchData.get(position));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrSearchData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView, tvRemove;

        public MyViewHolder(View view) {
            super(view);
            txtView = view.findViewById(R.id.name);
            tvRemove = view.findViewById(R.id.tvRemove);
        }
    }
}
