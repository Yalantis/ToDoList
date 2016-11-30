package com.yalantis.beamazingtoday.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.yalantis.beamazingtoday.ui.callback.OnCursorMovedListener;

/**
 * Created by irinagalata on 11/29/16.
 */

public class WatcherEditText extends EditText {

    private OnCursorMovedListener mListener;

    public WatcherEditText(Context context) {
        super(context);
    }

    public WatcherEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WatcherEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCursorListener(OnCursorMovedListener listener) {
        mListener = listener;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);

        if (mListener != null) {
            mListener.onCursorMoved();
        }
    }

}
