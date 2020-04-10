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
        String API_KAKAO_VERIFY_TOKEN = "verifyToken";       // 카카오 토큰 인증

        String API_USERS = "users";                         // 메시지 보내기
        String API_PORTFOLIOS = "users/portfolio";          // 포트폴리오
        String API_INSERT_PORTFOLIO = "users/portfolioInsert";    // 포트폴리오 추가
        String API_DELETE_PORTFOLIO = "users/portfolioDelete";    // 포트폴리오 삭제
        String API_LOGIN = "users/userInsert";              // 유저 로그인

        String API_CHANNELS = "channel/channer";            // 전체 채널 조회
        String API_USERSALL = "users";                      // 전체 유저 조회
        String API_NEW_CHANNEL = "channel/channerInsert";   // 신규 채널 생성
        String API_CHANNEL = "channel/channerInfo";         // 특정 채널 조회

        String API_NOTICES = "list/notice";                 // 전체 공지사항 조회
        String API_LOCATIONS = "list/location";             // 전체 장소 조회
        String API_JOBS = "list/job";                       // 전체 직업 조회
        String API_LIST_JOB_INFO = "list/jobInfo";          // 직업 정보

    }

}
