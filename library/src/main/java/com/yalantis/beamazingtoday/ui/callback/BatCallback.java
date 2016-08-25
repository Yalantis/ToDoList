package com.yalantis.beamazingtoday.ui.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.yalantis.beamazingtoday.listeners.BatListener;

/**
 * Created by galata on 18.07.16.
 */
public class BatCallback extends ItemTouchHelper.SimpleCallback {

    private BatListener mListener;

    public BatCallback(BatListener listener) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mListener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (mListener != null) {
            mListener.delete(viewHolder.getAdapterPosition());
        }
    }
}
