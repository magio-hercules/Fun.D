package com.fund.iam.data.remote;

import com.fund.iam.data.model.Channel;
import com.fund.iam.data.model.Portfolio;
import com.fund.iam.data.model.User;
import com.fund.iam.data.model.request.LoginBody;
import com.fund.iam.data.model.request.PushBody;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiHelper {

    // Aws
    @FormUrlEncoded
    @POST(ApiDefine.Body.API_USERS)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Maybe<Response<List<User>>> postUsers(@Field("id") int userId);

    @FormUrlEncoded
    @POST(ApiDefine.Body.API_PORTFOLIOS)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Maybe<Response<List<Portfolio>>> postPortfolios(@Field("id") int userId);

    @POST(ApiDefine.Body.API_LOGIN)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Maybe<Response<Void>> postLogin(@Body LoginBody loginBody);

    @POST(ApiDefine.Body.API_KAKAO_VERIFY_TOKEN)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Maybe<Response<User>> postVerifyToken(@Field("token") String token);

    // 전체채널 조회
    @POST(ApiDefine.Body.API_CHANNELS)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<List<Channel>>> postChannels();

    // 전체유저 조회
    @POST(ApiDefine.Body.API_USERSALL)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<List<User>>> postUsersAll();

    // Firebase
    @POST(ApiDefine.Body.API_FCM_SEND)
    @Headers({ApiDefine.Header.CONTENT_TYPE_JSON, ApiDefine.Header.AUTHORIZATION})
    Maybe<Response<Void>> postFcmSend(@Body PushBody pushBody);

}
