package com.chrysalis.activity;

import android.view.View;

/**
 * Created by bitwarepc on 06-Sep-17.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
