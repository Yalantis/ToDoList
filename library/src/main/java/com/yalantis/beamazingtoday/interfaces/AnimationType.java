package com.yalantis.beamazingtoday.interfaces;

import android.support.annotation.IntDef;

/**
 * Created by galata on 22.08.16.
 */

@IntDef({AnimationType.ADD, AnimationType.REMOVE, AnimationType.MOVE})
public @interface AnimationType {

    int ADD = 0;

    int REMOVE = 1;

    int MOVE = 2;

}
