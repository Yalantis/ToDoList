package com.yalantis.beamazingtoday.listeners;

/**
 * Created by galata on 18.07.16.
 */
public interface RecyclerViewListener {
    void onDismiss(int position);

    void onMove(int from, int to);
}
