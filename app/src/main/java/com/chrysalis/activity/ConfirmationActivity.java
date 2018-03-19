package com.chrysalis.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chrysalis.R;

/**
 * Created by Bitware Marketing on 24-05-2017.
 */

public class ConfirmationActivity extends Activity {
    Button btnReturnHome;
    TextView tvListOnBrouser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        inIt();
    }

    private void inIt() {
        btnReturnHome = findViewById(R.id.btnReturnHome);
        tvListOnBrouser = findViewById(R.id.tvListOnBrouser);

        tvListOnBrouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://chrysalis.dotcomweavers.net/"));
                startActivity(browserIntent);
            }
        });

        btnReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ConfirmationActivity.this, ProductListingScreen.class);
                startActivity(i);
                finish();
            }
        });

    }

}
