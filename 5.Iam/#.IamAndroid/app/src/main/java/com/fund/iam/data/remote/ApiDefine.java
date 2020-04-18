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
        String API_KAKAO_VERIFY_TOKEN = "verifyToken";      // 카카오 토큰 인증

        String API_USERSALL = "users";                      // 전체 유저 조회
        String API_USER_INFO = "users/info";                // 메시지 보내기
        String API_USER_UPDATE = "users/userUpdate";        // 유저 정보 업데이트
        String API_LOGIN = "users/login";                   // 유저 로그인

        String API_PORTFOLIOS = "users/portfolio";          // 포트폴리오
        String API_INSERT_PORTFOLIO = "users/portfolioInsert";    // 포트폴리오 추가
        String API_DELETE_PORTFOLIO = "users/portfolioDelete";    // 포트폴리오 삭제
        String API_UPDATE_PORTFOLIO = "users/portfolioUpdate";    // 포트폴리오 수정

        String API_CHANNELS = "channel/channer";            // 전체 채널 조회
        String API_NEW_CHANNEL = "channel/channerInsert";   // 신규 채널 생성
        String API_CHANNEL = "channel/channerInfo";         // 특정 채널 조회
        String API_CHANNEL_USERS = "channel/user";          // 특정 채널 전체유저 조회
        String API_CHANNEL_USER_INSERT = "channerInsert";   // 특정 채널 유저가입

        String API_NOTICES = "list/notice";                 // 전체 공지사항 조회
        String API_LOCATIONS = "list/location";             // 전체 장소 조회
        String API_JOBS = "list/job";                       // 전체 직업 조회
        String API_LIST_JOB_INFO = "list/jobInfo";          // 직업 정보

        String API_LETTER_BOX_INFO = "letterbox/letterboxInfo";          // 쪽지함
        String API_LETTER = "letterbox/message";          // 쪽지내용
        String API_LETTER_INSERT = "letterbox/messageInsert";          // 쪽지내용 입력하기

        String API_VERSION_CHECK = "store/apps/details";

    }

}
