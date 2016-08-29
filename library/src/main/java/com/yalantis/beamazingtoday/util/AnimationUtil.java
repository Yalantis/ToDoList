package com.yalantis.beamazingtoday.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.yalantis.beamazingtoday.Constant;

/**
 * Created by galata on 15.07.16.
 */
public class AnimationUtil {

    public static void show(View view) {
        animateAlpha(view, 1, null);
    }

    public static void hide(View view) {
        animateAlpha(view, 0, null);
    }

    public static void rotate(View view, int value, @Nullable Runnable endAction) {
        ViewPropertyAnimatorCompat animator = ViewCompat.animate(view).rotation(value);

        if (endAction != null) {
            animator.withEndAction(endAction);
        }

        animator.setDuration(Constant.ANIM_DURATION_MILLIS).start();
    }

    public static void showViews(View ... views) {
        for (View view : views) {
            show(view);
        }
    }

    public static void hideViews(View ... views) {
        for (View view : views) {
            hide(view);
        }
    }

    public static void scaleXViews(float value, View ... views) {
        for (View view : views) {
            scaleX(value, view);
        }
    }

    public static void scaleX(float value, View view) {
        ViewCompat.animate(view).scaleX(value).setDuration(Constant.ANIM_DURATION_MILLIS).start();
    }

    public static void moveX(View view, float value) {
        ViewCompat.animate(view).translationX(value).setDuration(Constant.ANIM_DURATION_MILLIS).start();
    }

    public static void moveX(View view, float value, Runnable endAction) {
        ViewCompat.animate(view).translationX(value).withEndAction(endAction).setDuration(Constant.ANIM_DURATION_MILLIS).start();
    }

    private static void animateAlpha(final View view, final float alpha, @Nullable final Runnable endAction) {
        if (alpha == 1) {
            view.setVisibility(View.VISIBLE);
            ViewCompat.animate(view).alpha(0);
        }
        ViewCompat.animate(view).alpha(alpha).setDuration(Constant.ANIM_DURATION_MILLIS).withEndAction(new Runnable() {
            @Override
            public void run() {
                if (alpha == 0) {
                    view.setVisibility(View.INVISIBLE);
                }

                if (endAction != null) {
                    endAction.run();
                }
            }
        }).start();
    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }
}
