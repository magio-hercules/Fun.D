package com.fund.iam.data.remote;

import com.fund.iam.data.model.Portfolio;
import com.fund.iam.data.model.User;
import com.fund.iam.data.model.request.PushBody;

import java.util.List;

import io.reactivex.Maybe;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiHelper {

    // Aws
    @GET(ApiDefine.Body.API_USERS)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Maybe<Response<List<User>>> getUsers();

    @GET(ApiDefine.Body.API_PORTFOLIOS)
    @Headers({ApiDefine.Header.ACCEPT_JSON})
    Maybe<Response<List<Portfolio>>> getPortfolios();

    // Firebase
    @POST(ApiDefine.Body.API_FCM_SEND)
    @Headers({ApiDefine.Header.CONTENT_TYPE_JSON, ApiDefine.Header.AUTHORIZATION})
    Maybe<Response<Void>> postFcmSend(@Body PushBody pushBody);

}
