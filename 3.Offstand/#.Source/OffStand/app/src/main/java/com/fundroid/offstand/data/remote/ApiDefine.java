package com.fundroid.offstand.data.remote;

public interface ApiDefine {
    int API_ENTER_ROOM = 1;             // 대기실 입장 client
    int API_ROOM_INFO = 2;              // 대기실 입장 host ( 지금 입장하는 User 에게 )
    int API_ENTER_ROOM_TO_OTHER = 3;    // 대기실 입장 host ( 이미 입장한 User 에게 )
    int API_READY = 4;                  // 레디 client
    int API_READY_BR = 5;               // 레디 전파 host
    int API_READY_CANCEL = 6;           // 레디 취소 client
    int API_READY_CANCEL_BR = 7;        // 레디 취소 전파 host
    int API_BAN = 8;                    // 강퇴 host
    int API_BAN_BR = 9;                 // 강퇴 전파 host
    int API_MOVE = 10;                  // 자리 바꾸기 host
    int API_MOVE_BR = 11;               // 자리 바꾸기 전파 host
    int API_OUT = 12;                   // 방 나가기 client
    int API_OUT_BR = 13;                // 방 나가기 전파 host
    int API_SHUFFLE = 14;               // 게임 시작 host
    int API_SHUFFLE_BR = 15;            // 게임 시작 전파 host
    int API_DIE = 16;                   // 다이 client
    int API_DIE_BR = 17;                // 다이 전파 host
    int API_CARD_OPEN = 18;             // 패 오픈 client
    int API_GAME_RESULT = 19;           // 게임 결과 host
    int API_GAME_RESULT_BR = 20;        // 게임 결과 전파 host
    int API_SHUFFLE_AVAILABLE = 21;     // 게임 시작 가능 host
    int API_SHUFFLE_NOT_AVAILABLE = 22; // 게임 시작 불가능 host
    int API_GAME_RESULT_AVAILABLE = 23; // 결과 전파 가능 host
    int API_TEST = 100;                 // 서버의 User List 정보 host
    int API_OUT_SELF = 24;              // 소켓 close시 본인에게 메시지 전달

}
