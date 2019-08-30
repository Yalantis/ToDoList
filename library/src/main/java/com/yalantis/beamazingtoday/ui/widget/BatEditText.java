package com.yalantis.beamazingtoday.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yalantis.beamazingtoday.R;
import com.yalantis.beamazingtoday.ui.callback.EditListener;
import com.yalantis.beamazingtoday.ui.callback.OnCursorMovedListener;
import com.yalantis.beamazingtoday.util.TypefaceUtil;

import java.lang.reflect.Field;

public class BatEditText extends FrameLayout implements OnCursorMovedListener {

    AppCompatImageView mCursor;
    WatcherEditText mEditText;

    private boolean isInEditMode;
    private EditListener mListener;

    public BatEditText(Context context) {
        this(context, null);
    }

    public BatEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.bat_edit_text, this, true);
        mCursor = view.findViewById(R.id.cursor);
        mEditText = view.findViewById(R.id.edit_text);
        mEditText.setCursorListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEditText.setTypeface(TypefaceUtil.getTypeface(getContext()));
    }

    void setEditListener(EditListener listener) {
        mListener = listener;
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
        isInEditMode = false;
    }

    public boolean isEnabled() {
        return mEditText.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        mEditText.setEnabled(enabled);
    }

    public void showCursor() {
        isInEditMode = true;
        mCursor.setVisibility(VISIBLE);
        startBlinking();
        mEditText.setCursorVisible(false);
    }

    private void startBlinking() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blinking_anim);
        mCursor.startAnimation(animation);
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

    void setHintColor(@ColorInt int color) {
        mEditText.setHintTextColor(color);
    }

    void setTextColor(@ColorInt int color) {
        mEditText.setTextColor(color);
    }

    void setCursorColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCursor.setBackgroundTintList(ColorStateList.valueOf(color));
        }
    }

    void setCursorDrawable(@DrawableRes int res) {
        try {
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            field.set(mEditText, res);
            mCursor.setImageResource(res);
        } catch (Exception ignored) {
        }
    }

    void setHint(@StringRes int hint) {
        mEditText.setHint(hint);
    }

    void setHint(String hint) {
        mEditText.setHint(hint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isInEditMode) {
            mEditText.onTouchEvent(event);
            onCursorMoved();
        } else if (mListener != null) {
            mListener.onStartEdit();
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public void onCursorMoved() {
        boolean isOnStart = mEditText.getSelectionStart() == 0;
        if (isOnStart) {
            startBlinking();
        } else {
            mCursor.clearAnimation();
        }
        mEditText.setCursorVisible(!isOnStart);
        mCursor.setVisibility(isOnStart ? VISIBLE : INVISIBLE);
    }
}
