package com.fund.iam.api;

import com.fund.iam.model.PortfolioInfo;
import com.fund.iam.model.UserInfo;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    String URL = "http://ec2-15-164-210-60.ap-northeast-2.compute.amazonaws.com:3000/";


    //////////
    // USER //

    @GET("/users")
    Call<List<UserInfo>> getUserList();

    @GET("/users/portfolio")
    Call<List<PortfolioInfo>> getPortfolioList();


    // end of USER //
    /////////////////








    ///////////
    // 참고용 //
    ///////////
    /**
     * GET 방식, URL/posts/{userId} 호출.
     * Data Type의 JSON을 통신을 통해 받음.
     * @Path("userId") String id : id 로 들어간 STring 값을, 첫 줄에 말한
     * {userId}에 넘겨줌.
     * userId에 1이 들어가면
     * "http://jsonplaceholder.typicode.com/posts/1" 이 최종 호출 주소.
     * @param userId 요청에 필요한 userId
     * @return Data 객체를 JSON 형태로 반환.
     */
    @GET("/posts/{userId}")
    Call<UserInfo> getData(@Path("userId") String userId);

    /**
     * GET 방식, URL/posts/{userId} 호출.
     * Data Type의 여러 개의 JSON을 통신을 통해 받음.
     * @Query("userId") String id : getData와 다르게 뒤에 붙는 파라미터가 없음.
     * 방식은 위와 같음.
     * 단, 주소값이 "http://jsonplaceholder.typicode.com/posts?userId=1" 이 됨.
     * @param userId 요청에 필요한 userId
     * @return 다수의 Data 객체를 JSON 형태로 반환.
     */
    @GET("/posts")
    Call<List<UserInfo>> getData2(@Query("userId") String userId);


    /**
     * POST 방식, 주소는 위들과 같음.
     * @FieldMap HashMap<String, Object> param :
     * Field 형식을 통해 넘겨주는 값들이 여러 개일 때 FieldMap을 사용함.
     * Retrofit에서는 Map 보다는 HashMap 권장.
     * @FormUrlEncoded Field 형식 사용 시 Form이 Encoding 되어야 하기 때문에 사용하는 어노테이션
     * Field 형식은 POST 방식에서만 사용가능.
     * @param param 요청에 필요한 값들.
     * @return Data 객체를 JSON 형태로 반환.
     */
    @FormUrlEncoded
    @POST("/posts")
    Call<UserInfo> postData(@FieldMap HashMap<String, Object> param);


    /**
     * PUT 방식. 값은 위들과 같음.
     * @Body Data param : 통신을 통해 전달하는 값이 특정 JSON 형식일 경우
     * 매번 JSON 으로 변환하지 않고, 객체를 통해서 넘겨주는 방식.
     * PUT 뿐만 아니라 다른 방식에서도 사용가능.
     * @param param 전달 데이터
     * @return Data 객체를 JSON 형태로 반환.
     */
    @PUT("/posts/1")
    Call<UserInfo> putData(@Body UserInfo param);


    /**
     * PATCH 방식. 값은 위들과 같습니다.
     * @FIeld("title") String title : patch 방식을 통해 title 에 해당하는 값을 넘기기 위해 사용.
     * @FormUrlEncoded Field 형식 사용 시 Form이 Encoding 되어야 하기 때문에 사용하는 어노테이션
     * @param title
     * @return
     */
    @FormUrlEncoded
    @PATCH("/posts/1")
    Call<UserInfo> patchData(@Field("title") String title);


    /**
     * DELETE 방식. 값은 위들과 같습니다.
     * Call<ResponseBody> : ResponseBody는 통신을 통해 되돌려 받는 값이 없을 경우 사용.
     * @return
     */
    @DELETE("/posts/1")
    Call<ResponseBody> deleteData();

    /*
     * DELETE 방식에서 @Body를 사용하기 위해서는 아래처럼 해야함.
     * @HTTP(method = "DELETE", path = "/Arahant/Modification/Profile/Image/User", hasBody = true)
     * Call<ResponseBody> delete(@Body RequestGet parameters);
     */

}