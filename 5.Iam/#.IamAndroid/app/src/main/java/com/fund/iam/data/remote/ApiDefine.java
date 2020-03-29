package com.fund.iam.data.remote;

public class ApiDefine {

    public interface Header {
        String CONTENT_TYPE_JSON = "Content-Type:application/json";
        String AUTHORIZATION = "Authorization:key=AAAA_K1SLKg:APA91bFHJ4trxWMNkfJB0xiP1WtLrTRT02wOMhYA5qAmuTe1JjkmCCfC_A0qS0vNC2CTBUcQ2ugycOv9qY5oVDil0_BHZ6zTZRk0GIBOBKsaLI40J-MIeVZnw4aI_UM8HaYQQg8YcZDx";
        String CONTENT_TYPE_AUTH = "Content-Type: application/x-www-form-urlencoded";
        String ACCEPT_JSON = "Accept:application/json;charset=utf-8";
    }

    public interface Body {

        String API_FCM_SEND = "fcm/send";                   // 메시지 보내기

        String API_USERS = "users";                         // 메시지 보내기
        String API_PORTFOLIOS = "users/portfolio";          // 메시지 보내기
    }

}
