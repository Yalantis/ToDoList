package com.yalantis.beamazingtoday.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.yalantis.beamazingtoday.Constant;
import com.yalantis.beamazingtoday.R;
import com.yalantis.beamazingtoday.R2;
import com.yalantis.beamazingtoday.listeners.AnimationListener;
import com.yalantis.beamazingtoday.listeners.BatListener;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by galata on 15.07.16.
 */
public class BatRecyclerView extends FrameLayout {

    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R2.id.header_view)
    BatHeaderView mHeaderView;
    @BindView(R2.id.view)
    AppCompatImageView mBackground;

    public BatRecyclerView(Context context) {
        this(context, null);
    }

    public BatRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.bat_recycler_view, this, true);
        ButterKnife.bind(this);

        obtainAttributes(context, attrs);
    }

    private void obtainAttributes(Context context, AttributeSet attributeSet) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.BatRecyclerView, 0, 0);

        int colorBlue = getColor(R.color.colorBlue);

        setDividerColor(array.getColor(R.styleable.BatRecyclerView_divider_color, getColor(R.color.colorDivider)));
        setPlusColor(array.getColor(R.styleable.BatRecyclerView_plus_color, colorBlue));
        setDividerVisibility(array.getBoolean(R.styleable.BatRecyclerView_show_divider, true));
        setListBackgroundColor(array.getColor(R.styleable.BatRecyclerView_list_background, getColor(R.color.colorBlueLightBackground)));
        setHintColor(array.getColor(R.styleable.BatRecyclerView_hint_color, getColor(R.color.colorGrey)));
        setCursorColor(array.getColor(R.styleable.BatRecyclerView_cursor_color, colorBlue));
        setHint(array.getString(R.styleable.BatRecyclerView_hint));

        if (array.hasValue(R.styleable.BatRecyclerView_radio_button_res)) {
            setRadioButtonSelector(array.getResourceId(R.styleable.BatRecyclerView_radio_button_res, R.drawable.selector_radio_button));
        }

        if (array.hasValue(R.styleable.BatRecyclerView_add_button_color)) {
            setAddButtonColor(array.getColorStateList(R.styleable.BatRecyclerView_add_button_color));
        }

        if (array.hasValue(R.styleable.BatRecyclerView_cursor_drawable)) {
            setCursorDrawable(array.getResourceId(R.styleable.BatRecyclerView_cursor_drawable, R.drawable.ic_cursor_drawable));
        }

        array.recycle();
    }

    @ColorInt int getColor(@ColorRes int color) {
        return ContextCompat.getColor(getContext(), color);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderView.setAnimationListener(new AnimationListenerImpl());
    }

    public void setAddItemListener(BatListener listener) {
        mHeaderView.setAddItemListener(listener);
    }

    public void revertAnimation() {
        mHeaderView.animateDecreasing();
    }

    public RecyclerView getView() {
        return mRecyclerView;
    }

    public void setListBackgroundColor(@ColorInt int color) {
        mBackground.setSupportBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void setHintColor(@ColorInt int color) {
        mHeaderView.mEditText.setHintColor(color);
    }

    public void setTextColor(@ColorInt int color) {
        mHeaderView.mEditText.setTextColor(color);
    }

    @Deprecated
    public void setCursorColor(@ColorInt int color) {
        mHeaderView.mEditText.setCursorColor(color);
    }

    public void setCursorDrawable(@DrawableRes int res) {
        mHeaderView.mEditText.setCursorDrawable(res);
    }

    public void setDividerColor(@ColorInt int color) {
        mHeaderView.setDividerColor(color);
    }

    public void setDividerVisibility(boolean visible) {
        mHeaderView.setDividerVisibility(visible);
    }

    public void setRadioButtonColor(@ColorInt int color) {
        mHeaderView.setRadioButtonColor(color);
    }

    public void setRadioButtonSelector(@DrawableRes int drawable) {
        mHeaderView.setRadioButtonSelector(drawable);
    }

    public void setPlusColor(@ColorInt int color) {
        mHeaderView.setPlusColor(color);
    }

    public void setHint(@StringRes int hint) {
        mHeaderView.mEditText.setHint(hint);
    }

    public void setHint(String hint) {
        mHeaderView.mEditText.setHint(hint);
    }

    public void setAddButtonColor(@ColorInt int color) {
        mHeaderView.setAddButtonColor(ColorStateList.valueOf(color));
    }

    public void setAddButtonColor(ColorStateList color) {
        mHeaderView.setAddButtonColor(color);
    }

    private int getDimen(@DimenRes int res) {
        return getContext().getResources().getDimensionPixelOffset(res);
    }

    class AnimationListenerImpl implements AnimationListener {
        @Override
        public void onIncreaseAnimationStarted() {
            ViewCompat.animate(mRecyclerView).translationY(getDimen(R.dimen.header_translation) * 2)
                    .setDuration(Constant.ANIM_DURATION_MILLIS).start();
        }

        @Override
        public void onDecreaseAnimationStarted() {
            ViewCompat.animate(mRecyclerView).translationY(0).setDuration(Constant.ANIM_DURATION_MILLIS).start();
        }

        @Override
        public void onAddAnimationStarted() {
            ViewCompat.animate(mRecyclerView).translationY(0).setDuration(0).start();
            mRecyclerView.scrollToPosition(0);
        }
    }
}
