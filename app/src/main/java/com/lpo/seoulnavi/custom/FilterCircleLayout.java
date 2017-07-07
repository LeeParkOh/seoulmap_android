package com.lpo.seoulnavi.custom;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by leewoonho on 2017. 7. 7..
 */

public class FilterCircleLayout extends FrameLayout {

    private static final String TAG = "FilterCircleLayout";

    public FilterCircleLayout(@NonNull Context context) {
        super(context);
    }

    public FilterCircleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FilterCircleLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
