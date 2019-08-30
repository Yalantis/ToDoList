package com.yalantis.beamazingtoday.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.yalantis.beamazingtoday.util.AnimationUtil;
import com.yalantis.beamazingtoday.R;

public class AddView extends FrameLayout {

    private static final int START_POSITION = 90;
    private static final int END_POSITION = 180;

    AppCompatImageView mHorizontalView;
    AppCompatImageView mVerticalView;

    public AddView(Context context) {
        this(context, null);
    }

    public AddView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.bat_add_view, this, true);
        mHorizontalView = view.findViewById(R.id.view_horizontal);
        mVerticalView = view.findViewById(R.id.view_vertical);
    }

    public void rotate(@Nullable Runnable endAction) {
        AnimationUtil.rotate(mHorizontalView, END_POSITION, endAction);
    }

    public void rotateBack(@Nullable Runnable endAction) {
        AnimationUtil.rotate(mHorizontalView, START_POSITION, endAction);
    }

    public void increase() {
        setVisibility(VISIBLE);
        setAlpha(1);
        mHorizontalView.setRotation(START_POSITION);
        startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.increasing_anim));
    }

    public void show() {
        AnimationUtil.show(this);
    }

    public void hide() {
        AnimationUtil.hide(this);
    }

    void setColor(@ColorInt int color) {
        ColorStateList list = ColorStateList.valueOf(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mHorizontalView.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mVerticalView.setBackgroundTintList(ColorStateList.valueOf(color));
        }
    }
}
