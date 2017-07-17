package com.lpo.seoulnavi.SeoulApi;

/**
 * Created by parkjongkook on 2017. 7. 17..
 */
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ContentService {
    /**
     * 공원정보 조회
     *
     * @param pAddr 공원주소
     * @return
     */
    @FormUrlEncoded
    @POST("/SearchParkInformationByAddressService.do")
    Call<ParkInfoRes> getPostParkInfo(@Field("pAddr") String pAddr);


}
