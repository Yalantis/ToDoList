package com.yalantis.beamazingtoday.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatImageView;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.yalantis.beamazingtoday.R;
import com.yalantis.beamazingtoday.R2;
import com.yalantis.beamazingtoday.util.TypefaceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by galata on 15.07.16.
 */
public class BatEditText extends FrameLayout {

    @BindView(R2.id.cursor)
    AppCompatImageView mCursor;
    @BindView(R2.id.edit_text)
    EditText mEditText;

    public BatEditText(Context context) {
        this(context, null);
    }

    public BatEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.bat_edit_text, this, true);
        ButterKnife.bind(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        RxTextView.textChanges(mEditText).subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                if (!TextUtils.isEmpty(charSequence)) {
                    moveCursor();
                } else {
                    moveToStart();
                }
            }
        });
        mEditText.setTypeface(TypefaceUtil.getTypeface(getContext()));
    }

    public EditText getView() {
        return mEditText;
    }

    public void clear() {
        mEditText.getText().clear();
    }

    public void hideCursor() {
        mEditText.setEnabled(false);
        mEditText.clearFocus();
        mCursor.clearAnimation();
        mCursor.setVisibility(GONE);
    }

    public boolean isEnabled() {
        return mEditText.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        mEditText.setEnabled(enabled);
    }

    public void showCursor() {
        mCursor.setVisibility(VISIBLE);
        startBlinking();
    }

    private void startBlinking() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blinking_anim);
        mCursor.startAnimation(animation);
    }

    public void moveToStart() {
        mCursor.setX(mEditText.getX());
        mCursor.setY(mEditText.getY());
    }

    public String getText() {
        return mEditText.getText().toString();
    }

    public void focus() {
        mEditText.requestFocus();
    }

    public void clearFocus() {
        mEditText.clearFocus();
    }

    private void moveCursor() {
        mCursor.setX(getCursorPosition() + mCursor.getWidth() * 1.5f);
        mCursor.setY(mEditText.getY());
    }

    private float getCursorPosition() {
        Layout layout = mEditText.getLayout();

        if (layout == null) {
            return 0;
        }

        int position = mEditText.getSelectionStart();
        return layout.getPrimaryHorizontal(position);
    }

    void setHintColor(@ColorInt int color) {
        mEditText.setHintTextColor(color);
    }

    void setTextColor(@ColorInt int color) {
        mEditText.setTextColor(color);
    }

    void setCursorColor(@ColorInt int color) {
        mCursor.setSupportBackgroundTintList(ColorStateList.valueOf(color));
    }

    void setHint(@StringRes int hint) {
        mEditText.setHint(hint);
    }

    void setHint(String hint) {
        mEditText.setHint(hint);
    }
}
