package com.chrysalis.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Bitware Marketing on 22-05-2017.
 */

public class TextViewThin extends TextView {

    public TextViewThin(Context context) {
        super(context);
        setFont();
    }

    public TextViewThin(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public TextViewThin(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextViewThin(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setFont();
    }

    public void setFont() {

        Typeface typedValue = Typeface.createFromAsset(getContext().getAssets(), "proxima_nova_thin.otf");
        setTypeface(typedValue);
    }
}
