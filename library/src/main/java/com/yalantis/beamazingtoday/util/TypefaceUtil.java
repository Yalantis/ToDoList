package com.yalantis.beamazingtoday.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by galata on 26.07.16.
 */
public class TypefaceUtil {

    private static final String FONT_NAME = "font.otf";
    private static final String AVENIR_FONT_NAME = "avenir.ttf";

    private static Typeface sTypeface;
    private static Typeface sAvenirTypeface;

    public static Typeface getTypeface(Context context) {
        if (sTypeface == null) {
            sTypeface = Typeface.createFromAsset(context.getAssets(), FONT_NAME);
        }

        return sTypeface;
    }

    public static Typeface getAvenirTypeface(Context context) {
        if (sAvenirTypeface == null) {
            sAvenirTypeface = Typeface.createFromAsset(context.getAssets(), AVENIR_FONT_NAME);
        }

        return sAvenirTypeface;
    }

}
