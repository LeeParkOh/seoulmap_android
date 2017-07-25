package com.lpo.seoulnavi.seoulapi;

import android.util.Log;

import com.lpo.seoulnavi.net.response.ParkInfoRes;
import com.lpo.seoulnavi.net.retrofit.ContentService;
import com.lpo.seoulnavi.seoulapi.ApiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by parkjongkook on 2017. 7. 17..
 *  서울시 주소별 공원정보 조회
 */

public class SearchParkInformationByAddressService {
    protected static final String TAG = "SearchParkInformationByAddressService";
    private static ParkInfoRes mParkInfoRes;
    ApiUtil apiUtil = new ApiUtil();
    protected final String baseUrl = apiUtil.getUrl("");

    //String pAddr ="";
    public void searchParkInfo(){
        Log.d(TAG,"baseUrl>>>"+baseUrl);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ContentService service = retrofit.create(ContentService.class);
        Call<ParkInfoRes> call = service.getPostParkInfo("");
        Log.d(TAG,"calllll>>>>>"+call);

        call.enqueue(new Callback<ParkInfoRes>() {
            @Override
            public void onResponse(Call<ParkInfoRes> call, Response<ParkInfoRes> response) {
                Log.d(TAG, "onResponse");
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse  2");
                    Log.d(TAG, "Retrofit Response Success");
                    mParkInfoRes = response.body();
                    //mBoardMainAdapter = new BoardMainAdapter(mBoardMainRes);
                    //mRecyclerView.setAdapter(mBoardMainAdapter);
                    Log.d(TAG, "listTotlaCount>>>"+mParkInfoRes.listTotlaCount);
                    Log.d(TAG, "resultList>>>"+mParkInfoRes.resultList);
                    Log.d(TAG, "rowList>>>"+mParkInfoRes.rowList);
                } else {
                    Log.d(TAG, "onResponse  3");
                    Log.d(TAG, "Retrofit Response Not Success");
                }
            }

            @Override
            public void onFailure(Call<ParkInfoRes> call, Throwable t) {
                Log.d(TAG, "onFailure");
                Log.d(TAG, "Retrofit Response Failed");
            }
        });
    }
}
