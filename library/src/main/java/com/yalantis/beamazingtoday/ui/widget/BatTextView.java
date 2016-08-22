package com.yalantis.beamazingtoday.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yalantis.beamazingtoday.util.TypefaceUtil;

/**
 * Created by galata on 26.07.16.
 */
public class BatTextView extends TextView {
    public BatTextView(Context context) {
        this(context, null);
    }

    public BatTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(TypefaceUtil.getTypeface(context));
    }
}
