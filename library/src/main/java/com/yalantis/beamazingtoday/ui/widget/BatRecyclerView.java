package com.yalantis.beamazingtoday.ui.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
    View mBackground;

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
        mBackground.setBackgroundColor(color);
    }

    class AnimationListenerImpl implements AnimationListener {
        @Override
        public void onIncreaseAnimationStarted() {
            ViewCompat.animate(mRecyclerView).translationY(BatHeaderView.HEADER_TRANSLATION_Y * 2)
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
