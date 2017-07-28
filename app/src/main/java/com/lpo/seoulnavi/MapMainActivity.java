package com.lpo.seoulnavi;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lpo.seoulnavi.seoulapi.NMapPOIflagType;
import com.lpo.seoulnavi.seoulapi.NMapViewerResourceProvider;
import com.lpo.seoulnavi.utils.ApiUtil;
import com.lpo.seoulnavi.seoulapi.SearchParkInfo;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

public class MapMainActivity extends NMapActivity {

    protected static final String TAG = "MapMainActivity";
    private NMapViewerResourceProvider mMapViewerResourceProvider;
    private NMapOverlayManager mOverlayManager;
    private static final boolean DEBUG = false;
    // 지도 화면 View
    private NMapView mMapView;
    // 플로팅 View
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        mMapView = new NMapView(this);
        setContentView(mMapView);
        mMapView.setClientId(ApiUtil.NAVER_API_KEY);
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();
        SearchParkInfo searchParkInfo = new SearchParkInfo();
        searchParkInfo.searchParkInfo();
      //  testPOIdataOverlay();



//        mFab = (FloatingActionButton) findViewById(R.id.fab);
//        mFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Filter View", Toast.LENGTH_LONG).show();
//            }
//        });

    }


    //마커표시 클릭시 들어왔다
    private void testPOIdataOverlay() {
        Log.d(TAG, "	>>>>	24 testPOIdataOverlay	");
        // Markers for POI item
        int markerId = NMapPOIflagType.PIN;
        Log.d(TAG, "	>>>>	24-1 testPOIdataOverlay	");
        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(2);

        Log.d(TAG, "	>>>>	24-2 testPOIdataOverlay	");
        NMapPOIitem item = poiData.addPOIitem(127.0630205, 37.5091300, "Pizza 777-111", markerId, 0);
        item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);

        Log.d(TAG, "	>>>>	24-3 testPOIdataOverlay	");
        NMapPOIitem item2 = poiData.addPOIitem(126.99032745918585, 37.56428173790483, "쌍용정보통신", markerId, 0);
        item2.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);

        Log.d(TAG, "	>>>>	24-4 testPOIdataOverlay	");
        NMapPOIitem item3 = poiData.addPOIitem(127.056960477563, 37.5151448382399, "봉은공원2", markerId, 0);
        item3.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);

        Log.d(TAG, "	>>>>	24-5 testPOIdataOverlay	");
        poiData.addPOIitem(127.061, 37.51, "Pizza 123-456", markerId, 0);
        poiData.endPOIdata();

        Log.d(TAG, "	>>>>	24-6 testPOIdataOverlay	");
        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

        Log.d(TAG, "	>>>>	24-7 testPOIdataOverlay	");
        // set event listener to the overlay
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

        Log.d(TAG, "	>>>>	24-8 testPOIdataOverlay	");

        // select an item
        poiDataOverlay.selectPOIitem(0, true);
        Log.d(TAG, "	>>>>	24-9 testPOIdataOverlay	");
    }//testPOIdataOverlay end

    /* POI data State Change Listener*/
    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            Log.d(TAG, "	>>>>	53 	onCalloutClick");
            if (DEBUG) {
                Log.d(TAG, "	>>>>	54 	onCalloutClick");
                Log.i(TAG, "onCalloutClick: title=" + item.getTitle());
            }

            // [[TEMP]] handle a click event of the callout
            //  Toast.makeText(MapMainActivity.this, "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            Log.d(TAG, "	>>>>	55 	onFocusChanged");
            if (DEBUG) {
                Log.d(TAG, "	>>>>	56 	onFocusChanged");
                if (item != null) {
                    Log.d(TAG, "	>>>>	57 	onFocusChanged");
                    Log.i(TAG, "onFocusChanged: " + item.toString());
                } else {
                    Log.d(TAG, "	>>>>	58 	onFocusChanged");
                    Log.i(TAG, "onFocusChanged: ");
                }
            }
        }
    };//NMapPOIdataOverlay.OnStateChangeListener end
}
