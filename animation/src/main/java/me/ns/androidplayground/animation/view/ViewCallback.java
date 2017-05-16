package me.ns.androidplayground.animation.view;

import android.support.annotation.IdRes;
import android.view.View;

/**
 * Created by shintaro.nosaka on 2017/05/16.
 */

public interface ViewCallback {

    void onAction(View view, @IdRes int eventViewId);
}
