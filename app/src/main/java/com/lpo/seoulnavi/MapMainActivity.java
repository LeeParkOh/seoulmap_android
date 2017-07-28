package com.lpo.seoulnavi;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lpo.seoulnavi.utils.ApiUtil;
import com.lpo.seoulnavi.seoulapi.SearchParkInfo;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapView;

public class MapMainActivity extends NMapActivity {

    protected static final String TAG = "MapMainActivity";

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

//        mFab = (FloatingActionButton) findViewById(R.id.fab);
//        mFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Filter View", Toast.LENGTH_LONG).show();
//            }
//        });

    }
}
