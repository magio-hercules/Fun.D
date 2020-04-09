package com.fund.iam.data.remote;

import com.fund.iam.data.model.Job;
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
    Maybe<Response<List<Channel>>> postChannels();

    // 전체유저 조회
    @POST(ApiDefine.Body.API_USERSALL)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Maybe<Response<List<User>>> postUsersAll();

    // 신규채널 생성
    @FormUrlEncoded
    @POST(ApiDefine.Body.API_NEW_CHANNEL)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Maybe<Response<Channel>> postCreateChannel(@Field("owner_id") int ownerId, @Field("name") String name, @Field("purpose") String purpose,@Field("location") String location ,@Field("description") String description, @Field("password")String password);

    // 특정채널 조회
    @FormUrlEncoded
    @POST(ApiDefine.Body.API_CHANNEL)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Maybe<Response<List<Channel>>> postChannel(@Field("id") int id);

    @POST(ApiDefine.Body.API_LIST_JOB)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Maybe<Response<List<Job>>> postJobList();

    @FormUrlEncoded
    @POST(ApiDefine.Body.API_LIST_JOB_INFO)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Maybe<Response<Job>> postJobInfo(@Field("id") int jobId);

    @FormUrlEncoded
    @POST(ApiDefine.Body.API_INSERT_PORTFOLIO)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Maybe<Response<Void>> postInsertPortfolio(@Field("user_id") int user_id, @Field("type") int type, @Field("text") String text);
//    Maybe<Response<Void>> postInsertPortfolio(@Body Portfolio portfolio);

    @FormUrlEncoded
    @POST(ApiDefine.Body.API_DELETE_PORTFOLIO)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Maybe<Response<Void>> postDeletePortfolio(@Field("id") int id);

    // Firebase
    @POST(ApiDefine.Body.API_FCM_SEND)
    @Headers({ApiDefine.Header.CONTENT_TYPE_JSON, ApiDefine.Header.AUTHORIZATION})
    Maybe<Response<Void>> postFcmSend(@Body PushBody pushBody);
}