package com.tjyw.atom.pub.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by stephen on 17-8-18.
 */
public class AtomPubInterceptFrameLayout extends FrameLayout {

    public AtomPubInterceptFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AtomPubInterceptFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return true;
    }
}
