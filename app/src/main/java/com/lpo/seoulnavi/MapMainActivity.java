package com.lpo.seoulnavi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapView;

public class MapMainActivity extends NMapActivity {

    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "xX_X9wNXFyIe0z3vGgi2";// 애플리케이션 클라이언트 아이디 값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        mMapView = new NMapView(this);
        setContentView(mMapView);
        mMapView.setClientId(CLIENT_ID);
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();
    }
}
