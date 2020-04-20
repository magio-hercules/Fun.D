package com.fund.iam.data.remote;

import com.fund.iam.data.model.Channel;
import com.fund.iam.data.model.ChannelUser;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.Location;
import com.fund.iam.data.model.Notice;
import com.fund.iam.data.model.Portfolio;
import com.fund.iam.data.model.User;
import com.fund.iam.data.model.request.PushBody;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiHelper {

    // Aws

    //
    @FormUrlEncoded
    @POST(ApiDefine.Body.API_USERS)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<List<User>>> postUsers(@Field("id") int userId);

    // 포트폴리오 리스트
    @FormUrlEncoded
    @POST(ApiDefine.Body.API_PORTFOLIOS)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<List<Portfolio>>> postPortfolios(@Field("id") int userId);

    // 로그인
    @POST(ApiDefine.Body.API_LOGIN)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<List<User>>> postLogin(@Body User user);

    // 카카오 토큰 인증 (사용 X)
    @POST(ApiDefine.Body.API_KAKAO_VERIFY_TOKEN)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<User>> postVerifyToken(@Field("token") String token);

    // 전체채널 조회
    @POST(ApiDefine.Body.API_CHANNELS)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<List<Channel>>> postChannels();

    // 전체유저 조회
    @POST(ApiDefine.Body.API_USERSALL)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<List<User>>> postUsersAll();

    // 신규채널 생성
    @FormUrlEncoded
    @POST(ApiDefine.Body.API_NEW_CHANNEL)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<List<Channel>>> postCreateChannel(@Field("owner_id") int ownerId, @Field("name") String name, @Field("purpose") String purpose,@Field("location") String location ,@Field("description") String description, @Field("password")String password);

    // 특정채널 조회
    @FormUrlEncoded
    @POST(ApiDefine.Body.API_CHANNEL)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<List<Channel>>> postChannel(@Field("id") int id);

    // 특정채널 전체유저조회
    @FormUrlEncoded
    @POST(ApiDefine.Body.API_CHANNEL_USERS)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<List<ChannelUser>>> postChannelUsers(@Field("channel_id") int id);

    // 특정채널 유저 가입
    @FormUrlEncoded
    @POST(ApiDefine.Body.API_CHANNEL_USER_INSERT)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<List<ChannelUser>>> postChannelUserInsert(@Field("channel_id") int channel_id, @Field("user_id") int user_id);

    // 특정채널 조회
    @POST(ApiDefine.Body.API_LOCATIONS)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<List<Location>>> postLocations();

    // 전체직업 조회
    @POST(ApiDefine.Body.API_JOBS)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<List<Job>>> postJobs();

    // 특정채널 조회
    @POST(ApiDefine.Body.API_NOTICES)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<List<Notice>>> postNotices();

    @FormUrlEncoded
    @POST(ApiDefine.Body.API_LIST_JOB_INFO)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<Job>> postJobInfo(@Field("id") int jobId);

    ///////////////
    // 포트폴리오 //
    @FormUrlEncoded
    @POST(ApiDefine.Body.API_INSERT_PORTFOLIO)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<Void>> postInsertPortfolio(@Field("user_id") int user_id, @Field("type") int type, @Field("text") String text);

    @FormUrlEncoded
    @POST(ApiDefine.Body.API_DELETE_PORTFOLIO)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<Void>> postDeletePortfolio(@Field("id") int id);

    @FormUrlEncoded
    @POST(ApiDefine.Body.API_UPDATE_PORTFOLIO)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Single<Response<Void>> postUpdatePortfolio(@Field("id") int id, @Field("user_id") int userId,
                                               @Field("type") int type, @Field("text") String text);
    // 포트폴리오 //
    ///////////////


    // Firebase
    @POST(ApiDefine.Body.API_FCM_SEND)
    @Headers({ApiDefine.Header.CONTENT_TYPE_JSON, ApiDefine.Header.AUTHORIZATION})
    Single<Response<Void>> postFcmSend(@Body PushBody pushBody);

}