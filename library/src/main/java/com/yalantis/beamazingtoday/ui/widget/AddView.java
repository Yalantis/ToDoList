package com.yalantis.beamazingtoday.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yalantis.beamazingtoday.R2;
import com.yalantis.beamazingtoday.util.AnimationUtil;
import com.yalantis.beamazingtoday.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by galata on 14.07.16.
 */
public class AddView extends FrameLayout {

    private static final int START_POSITION = 90;
    private static final int END_POSITION = 180;

    @BindView(R2.id.view_horizontal)
    AppCompatImageView mHorizontalView;
    @BindView(R2.id.view_vertical)
    AppCompatImageView mVerticalView;

    public AddView(Context context) {
        this(context, null);
    }

    public AddView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.bat_add_view, this, true);
        ButterKnife.bind(this);
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
        mHorizontalView.setSupportBackgroundTintList(list);
        mVerticalView.setSupportBackgroundTintList(list);
    }
}
