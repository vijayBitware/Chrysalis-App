package com.chrysalis.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chrysalis.R;
import com.chrysalis.model.ModelDataObject;

import java.util.ArrayList;

/**
 * Created by bitwarepc on 30-May-17.
 */

public class AdapterCategoryList extends ArrayAdapter<ModelDataObject> {
    LayoutInflater inflater;
    Context context;
    ViewHolder holder;
    ArrayList<ModelDataObject> arrProductList = new ArrayList<>();

    public AdapterCategoryList(Context context, int resource, ArrayList<ModelDataObject> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.arrProductList = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return arrProductList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if (vi == null) {

            vi = inflater.inflate(R.layout.row_costume_category_dialog, null);
            holder = new ViewHolder();

            holder.tv_CostumeCategoryName = vi.findViewById(R.id.tv_CostumeCategoryName);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();

        }

        holder.tv_CostumeCategoryName.setText(arrProductList.get(position).getVal());

        return vi;
    }

    public static class ViewHolder {
        TextView tv_CostumeCategoryName;

    }
}


