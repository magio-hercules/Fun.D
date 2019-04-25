package com.fundroid.offstand.data.remote;

public interface ApiDefine {
    int API_ENTER_ROOM = 1;           // 대기실 입장 client
    int API_ROOM_INFO = 2;            // 대기실 입장 host ( 지금 입장하는 User 에게 )
    int API_ENTER_ROOM_TO_OTHER = 3;  // 대기실 입장 host ( 이미 입장한 User 에게 )
    int API_READY = 4;                // 레디
    int API_READY_BR = 5;             // 레디 전파
    int API_BAN_BR = 6;               // 강퇴 전파
    int API_MOVE = 7;                 // 자리 바꾸기
    int API_SHUFFLE = 8;              // 게임 시작
    int API_DIE = 9;                  // 다이
    int API_DIE_BR = 10;              // 다이 전파
    int API_CARD_OPEN = 11;           // 패 오픈
    int API_GAME_RESULT = 12;         // 게임 결과
    int API_OUT = 13;                 // 방 나가기
    int API_OUT_BR = 14;              // 방 나가기 전파
}
