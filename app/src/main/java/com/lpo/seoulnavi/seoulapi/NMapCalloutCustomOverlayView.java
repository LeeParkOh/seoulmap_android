package com.lpo.seoulnavi.seoulapi;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.overlay.NMapPOIitem;

/**
 * Created by parkjongkook on 2017. 7. 31..
 */


public class NMapCalloutCustomOverlayView extends NMapCalloutOverlayView {

    private View mCalloutView;
    private TextView mCalloutText;
    private View mRightArrow;

    public NMapCalloutCustomOverlayView(Context context, NMapOverlay itemOverlay, NMapOverlayItem item, Rect itemBounds) {
        super(context, itemOverlay, item, itemBounds);

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater)getContext().getSystemService(infService);
        li.inflate(com.lpo.seoulnavi.R.layout.callout_overlay_view, this, true);

        mCalloutView = findViewById(com.lpo.seoulnavi.R.id.callout_overlay);
        mCalloutText = (TextView)mCalloutView.findViewById(com.lpo.seoulnavi.R.id.callout_text);
        mRightArrow = findViewById(com.lpo.seoulnavi.R.id.callout_rightArrow);

        mCalloutView.setOnClickListener(callOutClickListener);

        mCalloutText.setText(item.getTitle());

        if (item instanceof NMapPOIitem) {
            if (((NMapPOIitem)item).hasRightAccessory() == false) {
                mRightArrow.setVisibility(GONE);
            }
        }
    }

    private final View.OnClickListener callOutClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(null, mItemOverlay, mOverlayItem);
            }
        }
    };

}
