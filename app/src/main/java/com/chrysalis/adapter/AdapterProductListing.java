package com.chrysalis.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.androidquery.AQuery;
import com.chrysalis.R;
import com.chrysalis.domain.AppSingleton;
import com.chrysalis.domain.ConnectionDetector;
import com.chrysalis.model.ModelMyListingNew;

import org.json.JSONObject;

import java.util.ArrayList;

import it.sephiroth.android.library.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by bitware on 17/2/17.
 */

public class AdapterProductListing extends ArrayAdapter<ModelMyListingNew> {
    int screenWidth, screenHeight;
    LayoutInflater inflater;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ViewHolder holder;
    AQuery aQuery;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    ProgressDialog progressDialog;

    ArrayList<ModelMyListingNew> arrProductList = new ArrayList<>();

    public AdapterProductListing(Context context, int resource, ArrayList<ModelMyListingNew> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.arrProductList = arrayList;
        aQuery = new AQuery(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        screenWidth = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getHeight();
        sharedPreferences = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
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

            vi = inflater.inflate(R.layout.row_product_list, null);
            holder = new ViewHolder();
            holder.tv_productName = vi.findViewById(R.id.tv_productName);
            holder.tv_productPrice = vi.findViewById(R.id.tv_productPrice);
            holder.ivNameRow = vi.findViewById(R.id.ivNameRow);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();

        }
        holder.tv_productName.setText(arrProductList.get(position).getImgName().substring(0, 1).toUpperCase() + arrProductList.get(position).getImgName().substring(1));
        holder.tv_productPrice.setText("$" + arrProductList.get(position).getPrice());

      /*  Glide.with(context)
                .load(arrProductList.get(position).getImagePath())
                .into(holder.ivNameRow);*/

        Picasso.with(context)
                .load(arrProductList.get(position).getImagePath())
                .into(holder.ivNameRow);

        //aQuery.id(holder.ivNameRow).image(arrProductList.get(position).getImagePath());

        return vi;
    }

    private void getCostuDetailsAPI(String costumeDetailsURL, JSONObject jsonObject) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(costumeDetailsURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.hide();
                        System.out.println(">>> response :" + response.toString());
                        /*Gson gson = new Gson(); // Or use new GsonBuilder().create();
                        ProductDetails details = gson.fromJson(response.toString(),ProductDetails.class);
                        if(details.getResult().equalsIgnoreCase("success")){

                        }else{
                        }*/
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq);
    }

    public static class ViewHolder {
        TextView tv_productName, tv_productPrice;
        ImageView ivNameRow;

    }
}


