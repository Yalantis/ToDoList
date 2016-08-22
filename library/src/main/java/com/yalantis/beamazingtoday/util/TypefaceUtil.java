package com.yalantis.beamazingtoday.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by galata on 26.07.16.
 */
public class TypefaceUtil {
    private static final String FONT_NAME = "font.otf";

    private static Typeface sTypeface;

    public static Typeface getTypeface(Context context) {
        if (sTypeface == null) {
            sTypeface = Typeface.createFromAsset(context.getAssets(), FONT_NAME);
        }

        return sTypeface;
    }
}
