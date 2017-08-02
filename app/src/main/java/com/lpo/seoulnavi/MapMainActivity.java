package com.lpo.seoulnavi;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lpo.seoulnavi.net.response.ParkInfoRes;
import com.lpo.seoulnavi.seoulapi.NMapCalloutCustomOldOverlay;
import com.lpo.seoulnavi.seoulapi.NMapCalloutCustomOverlayView;
import com.lpo.seoulnavi.seoulapi.NMapPOIflagType;
import com.lpo.seoulnavi.seoulapi.NMapViewerResourceProvider;
import com.lpo.seoulnavi.utils.ApiUtil;
import com.lpo.seoulnavi.seoulapi.SearchParkInfo;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutCustomOverlay;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

public class MapMainActivity extends NMapActivity {

    protected static final String TAG = "MapMainActivity";
    private NMapOverlayManager mOverlayManager;
    private NMapMyLocationOverlay mMyLocationOverlay;
    private NMapLocationManager mMapLocationManager;
    private NMapController mMapController;
    private NMapCompassManager mMapCompassManager;
    private MapContainerView mMapContainerView;
    private static final String KEY_ZOOM_LEVEL = "MainActivity.zoomLevel";
    private static final String KEY_CENTER_LONGITUDE = "MainActivity.centerLongitudeE6";
    private static final String KEY_CENTER_LATITUDE = "MainActivity.centerLatitudeE6";
    private static final String KEY_VIEW_MODE = "MainActivity.viewMode";
    private static final String KEY_TRAFFIC_MODE = "MainActivity.trafficMode";
    private static final String KEY_BICYCLE_MODE = "MainActivity.bicycleMode";
    private static final NGeoPoint NMAP_LOCATION_DEFAULT = new NGeoPoint(126.978371, 37.5666091);
    private static final int NMAP_ZOOMLEVEL_DEFAULT = 11;
    private static final int NMAP_VIEW_MODE_DEFAULT = NMapView.VIEW_MODE_VECTOR;
    private static final boolean NMAP_TRAFFIC_MODE_DEFAULT = false;
    private static final boolean NMAP_BICYCLE_MODE_DEFAULT = false;
    private SharedPreferences mPreferences;

    private NMapViewerResourceProvider mMapViewerResourceProvider;

    private NMapPOIdataOverlay mFloatingPOIdataOverlay;
    private NMapPOIitem mFloatingPOIitem;
    private static final boolean DEBUG = false;
    // 지도 화면 View
    private NMapView mMapView;
    // 플로팅 View
    private FloatingActionButton mFab;

    private static SearchParkInfo searchParkInfo = new SearchParkInfo();
    private static ParkInfoRes mParkInfoRes = new ParkInfoRes();
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

        // register listener for map state changes
        mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
        mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
        mMapView.setOnMapViewDelegate(onMapViewTouchDelegate);

        // use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mMapView.getMapController();

        // use built in zoom controls
        NMapView.LayoutParams lp = new NMapView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, NMapView.LayoutParams.BOTTOM_RIGHT);
        mMapView.setBuiltInZoomControls(true, lp);

        // create resource provider
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

        // set data provider listener
        super.setMapDataProviderListener(onDataProviderListener);

        // create overlay manager
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
        // register callout overlay listener to customize it.
        mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);
        // register callout overlay view listener to customize it.
        mOverlayManager.setOnCalloutOverlayViewListener(onCalloutOverlayViewListener);

        // location manager
        Log.d(TAG,"location manager>>>>>");
        mMapLocationManager = new NMapLocationManager(this);
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

        // compass manager
        mMapCompassManager = new NMapCompassManager(this);

        // create my location overlay
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);


        Log.d(TAG,"왔나 >>>>>>>>>>>>>>>>>>>>>>");
        testSync();
        Log.d(TAG,"왔나 >>>>>>>>>>>>>>>>>>>>>>2");
        //마크 찍히는지 테스트
      //  markParkInfo();
    }
   void testSync() {
           Log.d(TAG, "sync 맞냐??<<<>>>>>");
           mParkInfoRes = searchParkInfo.searchParkInfo();
           //Log.d(TAG,">>>>.>"+mParkInfoRes.searchParkInfo.row.get(1).pPark);
       Log.d(TAG, "sync 맞냐??<<<>>>>>end");
    }
    private void markParkInfo(){
        mOverlayManager.clearOverlays();
        testPOIdataOverlay();
    }

    //마커표시 클릭시 들어왔다
    private void testPOIdataOverlay() {
        int rowSize = mParkInfoRes.searchParkInfo.row.size();
        Log.d(TAG,"rowSize>>>>"+rowSize);

        // Markers for POI item
        int markerId = NMapPOIflagType.PIN;

        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(2);
/*
        for(int i=0 ;i<rowSize; i++){

            NMapPOIitem item = poiData.addPOIitem(mParkInfoRes.searchParkInfo.row.get(i).latitude
                                                , mParkInfoRes.searchParkInfo.row.get(i).longitude
                                                , mParkInfoRes.searchParkInfo.row.get(i).pPark
                                                , markerId, 0);
            item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
        }
*/

        NMapPOIitem item = poiData.addPOIitem(127.0630205, 37.5091300, "Pizza 777-111", markerId, 0);
        item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);

        NMapPOIitem item2 = poiData.addPOIitem(126.99032745918585, 37.56428173790483, "쌍용정보통신", markerId, 0);
        item2.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);

        NMapPOIitem item3 = poiData.addPOIitem(127.056960477563, 37.5151448382399, "봉은공원2", markerId, 0);
        item3.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);

        poiData.addPOIitem(127.061, 37.51, "Pizza 123-456", markerId, 0);
        poiData.endPOIdata();


        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

        // set event listener to the overlay
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

        // select an item
        poiDataOverlay.selectPOIitem(0, true);
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

    private final NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener = new NMapOverlayManager.OnCalloutOverlayListener() {

        @Override
        public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay itemOverlay, NMapOverlayItem overlayItem,
                                                         Rect itemBounds) {
            Log.d(TAG, "	>>>>	62 	OnCalloutOverlayListener");
            // handle overlapped items
            if (itemOverlay instanceof NMapPOIdataOverlay) {
                Log.d(TAG, "	>>>>	63 	OnCalloutOverlayListener");
                NMapPOIdataOverlay poiDataOverlay = (NMapPOIdataOverlay)itemOverlay;

                // check if it is selected by touch event
                if (!poiDataOverlay.isFocusedBySelectItem()) {
                    Log.d(TAG, "	>>>>	64 	OnCalloutOverlayListener");
                    int countOfOverlappedItems = 1;

                    NMapPOIdata poiData = poiDataOverlay.getPOIdata();
                    for (int i = 0; i < poiData.count(); i++) {
                        Log.d(TAG, "	>>>>	65 	OnCalloutOverlayListener");
                        NMapPOIitem poiItem = poiData.getPOIitem(i);

                        // skip selected item
                        if (poiItem == overlayItem) {
                            Log.d(TAG, "	>>>>	66 	OnCalloutOverlayListener");
                            continue;
                        }

                        // check if overlapped or not
                        if (Rect.intersects(poiItem.getBoundsInScreen(), overlayItem.getBoundsInScreen())) {
                            Log.d(TAG, "	>>>>	67 	OnCalloutOverlayListener");
                            countOfOverlappedItems++;
                        }
                    }

                    if (countOfOverlappedItems > 1) {
                        Log.d(TAG, "	>>>>	68 	OnCalloutOverlayListener");
                        String text = countOfOverlappedItems + " overlapped items for " + overlayItem.getTitle();
                        //Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                        return null;
                    }
                }
            }

            // use custom old callout overlay
            if (overlayItem instanceof NMapPOIitem) {
                Log.d(TAG, "	>>>>	69 	OnCalloutOverlayListener");
                NMapPOIitem poiItem = (NMapPOIitem)overlayItem;

                if (poiItem.showRightButton()) {
                    Log.d(TAG, "	>>>>	70 	OnCalloutOverlayListener");
                    return new NMapCalloutCustomOldOverlay(itemOverlay, overlayItem, itemBounds,
                            mMapViewerResourceProvider);
                }
            }
            Log.d(TAG, "	>>>>	71 	OnCalloutOverlayListener");
            // use custom callout overlay
            return new NMapCalloutCustomOverlay(itemOverlay, overlayItem, itemBounds, mMapViewerResourceProvider);

            // set basic callout overlay
            //return new NMapCalloutBasicOverlay(itemOverlay, overlayItem, itemBounds);
        }

    };//NMapOverlayManager.OnCalloutOverlayListener end

    private final NMapOverlayManager.OnCalloutOverlayViewListener onCalloutOverlayViewListener = new NMapOverlayManager.OnCalloutOverlayViewListener() {

        @Override
        public View onCreateCalloutOverlayView(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {
            Log.d(TAG, "	>>>>	72 	OnCalloutOverlayViewListener");
            if (overlayItem != null) {
                Log.d(TAG, "	>>>>	73 	OnCalloutOverlayViewListener");
                // [TEST] 말풍선 오버레이를 뷰로 설정함
                String title = overlayItem.getTitle();
                if (title != null && title.length() > 5) {
                    Log.d(TAG, "	>>>>	74 	OnCalloutOverlayViewListener");
                    return new NMapCalloutCustomOverlayView(MapMainActivity.this, itemOverlay, overlayItem, itemBounds);
                }
            }

            // null을 반환하면 말풍선 오버레이를 표시하지 않음
            return null;
        }

    };//NMapOverlayManager.OnCalloutOverlayViewListener end

    /* MyLocation Listener */
    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

        @Override
        public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {
            Log.d(TAG, "	>>>>	31 	onLocationChanged");
            if (mMapController != null) {
                mMapController.animateTo(myLocation);
            }

            return true;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager locationManager) {
            Log.d(TAG, "	>>>>	32 	onLocationUpdateTimeout");
            // stop location updating
            //			Runnable runnable = new Runnable() {
            //				public void run() {
            //					stopMyLocation();
            //				}
            //			};
            //			runnable.run();

           // Toast.makeText(MainActivity.this, "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {
            Log.d(TAG, "	>>>>	33 	onLocationUnavailableArea");
          //  Toast.makeText(MainActivity.this, "Your current location is unavailable area.", Toast.LENGTH_LONG).show();

            stopMyLocation();
        }

    };//NMapLocationManager.OnLocationChangeListener end

    private void stopMyLocation() {
        Log.d(TAG, "	>>>>	18	stopMyLocation");
        if (mMyLocationOverlay != null) {
            Log.d(TAG, "	>>>>	19	");
            mMapLocationManager.disableMyLocation();

            if (mMapView.isAutoRotateEnabled()) {
                Log.d(TAG, "	>>>>	20	");
                mMyLocationOverlay.setCompassHeadingVisible(false);

                mMapCompassManager.disableCompass();

                mMapView.setAutoRotateEnabled(false, false);

                mMapContainerView.requestLayout();
            }
        }
    }//stopMyLocation end

    /**
     * Container view class to rotate map view.
     */
    private class MapContainerView extends ViewGroup {

        public MapContainerView(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            Log.d(TAG, "	>>>>	87 	onLayout");
            final int width = getWidth();
            final int height = getHeight();
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                Log.d(TAG, "	>>>>	88 	onLayout");
                final View view = getChildAt(i);
                final int childWidth = view.getMeasuredWidth();
                final int childHeight = view.getMeasuredHeight();
                final int childLeft = (width - childWidth) / 2;
                final int childTop = (height - childHeight) / 2;
                view.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            }

            if (changed) {
                Log.d(TAG, "	>>>>	89 	onLayout");
                mOverlayManager.onSizeChanged(width, height);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            Log.d(TAG, "	>>>>	90 	onMeasure");
            int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            int h = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            int sizeSpecWidth = widthMeasureSpec;
            int sizeSpecHeight = heightMeasureSpec;

            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View view = getChildAt(i);

                if (view instanceof NMapView) {
                    if (mMapView.isAutoRotateEnabled()) {
                        int diag = (((int)(Math.sqrt(w * w + h * h)) + 1) / 2 * 2);
                        sizeSpecWidth = MeasureSpec.makeMeasureSpec(diag, MeasureSpec.EXACTLY);
                        sizeSpecHeight = sizeSpecWidth;
                    }
                }

                view.measure(sizeSpecWidth, sizeSpecHeight);
            }
            Log.d(TAG, "	>>>>	91 	onMeasure");
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }//MapContainerView end
    /* MapView State Change Listener*/
    private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {

        @Override
        public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
            Log.d(TAG, "	>>>>	34 	onMapInitHandler");
            if (errorInfo == null) { // success
                Log.d(TAG, "	>>>>	35 	onMapInitHandler");
                // restore map view state such as map center position and zoom level.
                restoreInstanceState();

            } else { // fail
                Log.e(TAG, "onFailedToInitializeWithError: " + errorInfo.toString());
                Log.d(TAG, "	>>>>	36 	onMapInitHandler");
                Toast.makeText(MapMainActivity.this, errorInfo.toString(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onAnimationStateChange(NMapView mapView, int animType, int animState) {
            Log.d(TAG, "	>>>>	37 	onAnimationStateChange");
            if (DEBUG) {
                Log.d(TAG, "	>>>>	38 	onAnimationStateChange");
                Log.i(TAG, "onAnimationStateChange: animType=" + animType + ", animState=" + animState);
            }
        }

        @Override
        public void onMapCenterChange(NMapView mapView, NGeoPoint center) {
            Log.d(TAG, "	>>>>	39 	onMapCenterChange");
            if (DEBUG) {
                Log.d(TAG, "	>>>>	40 	onMapCenterChange");
                Log.i(TAG, "onMapCenterChange: center=" + center.toString());
            }
        }

        @Override
        public void onZoomLevelChange(NMapView mapView, int level) {
            Log.d(TAG, "	>>>>	41 	onZoomLevelChange");
            if (DEBUG) {
                Log.d(TAG, "	>>>>	42 	onZoomLevelChange");
                Log.i(TAG, "onZoomLevelChange: level=" + level);
            }
        }

        @Override
        public void onMapCenterChangeFine(NMapView mapView) {
            Log.d(TAG, "	>>>>	43 	onMapCenterChangeFine");
        }
    };//NMapView.OnMapStateChangeListener end

    /* Local Functions */
    private static boolean mIsMapEnlared = false;

    private void restoreInstanceState() {
        Log.d(TAG, "	>>>>	75 	restoreInstanceState");
        mPreferences = getPreferences(MODE_PRIVATE);

        int longitudeE6 = mPreferences.getInt(KEY_CENTER_LONGITUDE, NMAP_LOCATION_DEFAULT.getLongitudeE6());
        int latitudeE6 = mPreferences.getInt(KEY_CENTER_LATITUDE, NMAP_LOCATION_DEFAULT.getLatitudeE6());
        int level = mPreferences.getInt(KEY_ZOOM_LEVEL, NMAP_ZOOMLEVEL_DEFAULT);
        int viewMode = mPreferences.getInt(KEY_VIEW_MODE, NMAP_VIEW_MODE_DEFAULT);
        boolean trafficMode = mPreferences.getBoolean(KEY_TRAFFIC_MODE, NMAP_TRAFFIC_MODE_DEFAULT);
        boolean bicycleMode = mPreferences.getBoolean(KEY_BICYCLE_MODE, NMAP_BICYCLE_MODE_DEFAULT);

        mMapController.setMapViewMode(viewMode);
        mMapController.setMapViewTrafficMode(trafficMode);
        mMapController.setMapViewBicycleMode(bicycleMode);
        mMapController.setMapCenter(new NGeoPoint(longitudeE6, latitudeE6), level);

        if (mIsMapEnlared) {
            Log.d(TAG, "	>>>>	76 	restoreInstanceState");
            mMapView.setScalingFactor(2.0F);
        } else {
            Log.d(TAG, "	>>>>	77 	restoreInstanceState");
            mMapView.setScalingFactor(1.0F);
        }
    }//restoreInstanceState end

    private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {

        @Override
        public void onLongPress(NMapView mapView, MotionEvent ev) {
            Log.d(TAG, "	>>>>	44 	onLongPress");
            // TODO Auto-generated method stub

        }

        @Override
        public void onLongPressCanceled(NMapView mapView) {
            Log.d(TAG, "	>>>>	45 	onLongPressCanceled");
            // TODO Auto-generated method stub

        }

        @Override
        public void onSingleTapUp(NMapView mapView, MotionEvent ev) {
            Log.d(TAG, "	>>>>	46 	onSingleTapUp");
            // TODO Auto-generated method stub

        }

        @Override
        public void onTouchDown(NMapView mapView, MotionEvent ev) {
            Log.d(TAG, "	>>>>	47 	onTouchDown");
        }

        @Override
        public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
            Log.d(TAG, "	>>>>	48 	onScroll");
        }

        @Override
        public void onTouchUp(NMapView mapView, MotionEvent ev) {
            Log.d(TAG, "	>>>>	49 	onTouchUp");
            //mMapController.get
            Log.d(TAG, "	>>>>49-1 onTouchUp>>"+ "getX>>> "+ mapView.getX()+ "getY>>> "+mapView.getY());
            Log.d(TAG, "	>>>>49-2 onTouchUp>>"+ "getPivotX>>> "+ mapView.getPivotX()+ "getPivtY>>> "+mapView.getPivotY());

            // NGeoPoint point = mFloatingPOIitem.getPoint();
            //    Log.d(LOG_TAG, "	>>>>49-3 onTouchUp>>"+ "getTranslationX>>> "
            //           + mapView.get+"  getTranslationY>>>"+mapView.getTranslationY());
        }

    };//NMapView.OnMapViewTouchEventListener end
    private final NMapView.OnMapViewDelegate onMapViewTouchDelegate = new NMapView.OnMapViewDelegate() {

        @Override
        public boolean isLocationTracking() {
            Log.d(TAG, "	>>>>	50 	OnMapViewDelegate");
            if (mMapLocationManager != null) {
                Log.d(TAG, "	>>>>	51 	OnMapViewDelegate");
                if (mMapLocationManager.isMyLocationEnabled()) {
                    Log.d(TAG, "	>>>>	52 	OnMapViewDelegate");
                    return mMapLocationManager.isMyLocationFixed();
                }
            }
            return false;
        }

    };//NMapView.OnMapViewDelegate onMapViewTouchDelegate  end

    /* NMapDataProvider Listener */
    private final OnDataProviderListener onDataProviderListener = new OnDataProviderListener() {

        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {
            Log.d(TAG, "	>>>>	28 OnDataProviderListener	");
            if (DEBUG) {
                Log.i(TAG, "onReverseGeocoderResponse: placeMark="
                        + ((placeMark != null) ? placeMark.toString() : null));
            }

            if (errInfo != null) {
                Log.e(TAG, "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());

                Toast.makeText(MapMainActivity.this, errInfo.toString(), Toast.LENGTH_LONG).show();
                return;
            }

            if (mFloatingPOIitem != null && mFloatingPOIdataOverlay != null) {
                Log.d(TAG, "	>>>>	29 	");
                mFloatingPOIdataOverlay.deselectFocusedPOIitem();

                if (placeMark != null) {
                    mFloatingPOIitem.setTitle(placeMark.toString());
                }
                Log.d(TAG, "	>>>>	30 	");
                mFloatingPOIdataOverlay.selectPOIitemBy(mFloatingPOIitem.getId(), false);
            }
        }

    };//OnDataProviderListener end
}
