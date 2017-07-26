package com.lpo.seoulnavi.seoulapi;

import android.util.Log;

import com.lpo.seoulnavi.net.response.ParkInfoRes;
import com.lpo.seoulnavi.net.retrofit.ContentService;
import com.lpo.seoulnavi.utils.ApiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by parkjongkook on 2017. 7. 17..
 *  서울시 주소별 공원정보 조회
 */

public class SearchParkInfo {
    private static final String TAG = "SearchParkInfo";
    private static ParkInfoRes mParkInfoRes;
    ApiUtil apiUtil = new ApiUtil();
    protected final String baseUrl = apiUtil.getUrl("");

    //String pAddr ="";
    public void searchParkInfo(){
        Log.d(TAG,"baseUrl>>>"+baseUrl);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtil.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ContentService service = retrofit.create(ContentService.class);
        Call<ParkInfoRes> call = service.getPostParkInfo();

        call.enqueue(new Callback<ParkInfoRes>() {
            @Override
            public void onResponse(Call<ParkInfoRes> call, Response<ParkInfoRes> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "Retrofit Response Success");
                    mParkInfoRes = response.body();
                    Log.d(TAG, "mParkInfoRes Row Size = " + mParkInfoRes.row.size());

                } else {
                    Log.d(TAG, "Retrofit Response Not Success");
                }
            }

            @Override
            public void onFailure(Call<ParkInfoRes> call, Throwable t) {
                Log.d(TAG, "onFailure");
                Log.d(TAG, "mParkInfoRes Result Code = " + mParkInfoRes.resultList.code);
                Log.d(TAG, "mParkInfoRes Result Msg = " + mParkInfoRes.resultList.message);
            }
        });
    }
}
