package com.lpo.seoulnavi.SeoulApi;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by parkjongkook on 2017. 7. 17..
 *  서울시 주소별 공원정보 조회
 */

public class SearchParkInformationByAddressService {
    protected static final String TAG = "SearchParkInformationByAddressService";
    private static final String baseUrl = "http://openapi.seoul.go.kr:8088/476b6d4a4c74776f3131394d78654457/json/SearchParkInformationByAddressService/1/100/";
    String pAddr ="";
    public void searchParkInfo(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ContentService service = retrofit.create(ContentService.class);
        Call<ParkInfoRes> call = service.getPostParkInfo(pAddr);
        Log.d(TAG,"calllll>>>>>"+call );
    }
}
