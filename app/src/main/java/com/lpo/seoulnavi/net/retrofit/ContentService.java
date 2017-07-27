package com.lpo.seoulnavi.net.retrofit;

/**
 * Created by parkjongkook on 2017. 7. 17..
 */
import com.lpo.seoulnavi.net.response.ParkInfoRes;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ContentService {
    /**
     * 공원정보 조회
     * @return
     */
    @GET("/476b6d4a4c74776f3131394d78654457/json/SearchParkInformationByAddressService/1/100")
    Call<ParkInfoRes> getPostParkInfo();

}
