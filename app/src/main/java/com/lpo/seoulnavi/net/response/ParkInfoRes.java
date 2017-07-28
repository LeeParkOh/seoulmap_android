package com.lpo.seoulnavi.net.response;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
/**
 * Created by parkjongkook on 2017. 7. 17..
 */

public class ParkInfoRes {
    protected static final String TAG = "ParkInfoRes";

    @SerializedName("SearchParkInformationByAddressService")
    public SearchParkInfo searchParkInfo;

    public class SearchParkInfo {

        @SerializedName("list_total_count")
        public String listTotlaCount;

        @SerializedName("RESULT")
        public RESULTLIST resultList;

        public class RESULTLIST {
            @SerializedName("CODE")
            public String code;
            @SerializedName("MESSAGE")
            public String message;
        }

        @SerializedName("row")
        public ArrayList<row> row = new ArrayList<>();

        public class row {
            //공원번호
            @SerializedName("P_IDX")
            public String pIdx;

            //공원명
            @SerializedName("P_PARK")
            public String pPark;

            //공원설명
            @SerializedName("P_LIST_CONTENT")
            public String pListContent;

            //주소
            @SerializedName("P_ADDR")
            public String pAddr;

            //지역
            @SerializedName("P_ZONE")
            public String pZone;

            //관리부서
            @SerializedName("P_DIVISION")
            public String pDivision;

            //이미지
            @SerializedName("P_IMG")
            public String pImg;

            //전화번호
            @SerializedName("P_ADMINTEL")
            public String pAdminTel;

            //X좌표(WGS84)
            @SerializedName("LONGITUDE")
            public String longitude;

            //Y좌표(WGS84)
            @SerializedName("LATITUDE")
            public String latitude;

            //X좌표(GRS80TM)
            @SerializedName("G_LONGITUDE")
            public String gLongitude;

            //Y좌표(GRS80TM)
            @SerializedName("G_LATITUDE")
            public String gLatitude;
        }
    }
}
