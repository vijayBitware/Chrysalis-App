package com.chrysalis.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chrysalis.R;
import com.chrysalis.model.PricingShippingModel;

import java.util.ArrayList;

/**
 * Created by bitwarepc on 02-Jun-17.
 */

public class AdapterPricingShipping extends ArrayAdapter<PricingShippingModel> {
    LayoutInflater inflater;
    Context context;
    AdapterCategoryList.ViewHolder holder;
    ArrayList<PricingShippingModel> arrProductList = new ArrayList<>();

    public AdapterPricingShipping(Context context, int resource, ArrayList<PricingShippingModel> arrayList) {
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
            holder = new AdapterCategoryList.ViewHolder();

            holder.tv_CostumeCategoryName = vi.findViewById(R.id.tv_CostumeCategoryName);

            vi.setTag(holder);
        } else {
            holder = (AdapterCategoryList.ViewHolder) vi.getTag();

        }

        holder.tv_CostumeCategoryName.setText(arrProductList.get(position).getVal());

        return vi;
    }

    public static class ViewHolder {
        TextView tv_CostumeCategoryName;

    }
}


