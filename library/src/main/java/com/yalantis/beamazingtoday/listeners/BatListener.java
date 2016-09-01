package com.yalantis.beamazingtoday.listeners;

/**
 * Created by galata on 22.08.16.
 */
public interface BatListener {

    void add(String string);

    void delete(int position);

    void move(int from, int to);

}
