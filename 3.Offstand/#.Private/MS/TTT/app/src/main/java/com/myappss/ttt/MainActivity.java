package com.myappss.ttt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

          /*
    level = 10 삼팔광땡
    level = 9 광땡 // 번외 2
    level = 8 장떙 // 번외 4
    level = 7 땡 // 번외 1,4
    level = 6 족보 알리 독사 구삥 장삥 // 번외 4,3
    level = 5 족보 세륙 장사 // 번외 4,3
    level = 4 멍구사 // 8,7,6,5,4,3,2,1,0 재경기 // 2이길경우 무시당함
    level = 3 구사 5,4,3,2,1,0 재경기 // 2,1 이길경우 무시당함
    level = 2 암행어사 // 9 이김 // 나머지 무시당함 1끝
    level = 1 떙잡이 // 번외 4 // 7 이김 // 나머지 무시당함 0끝
    level = 0 끝 // 번외 4, 3 //
     */


    /*
    38광
    30
    18,13광
    28,29
    27멍구사
    1,2,3,4,5,6,7,8,9,10땡
    17,18,19,20,21,22,23,24,25,26
    구사 16
    알리 15
    독사 14
    구삥 13
    장삥 12
    장사 11,
    세륙 10
    끝    0,1,2,3,4,5,6,7,8,9
     */

    Button button;

    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.text);
        textView2 = findViewById(R.id.text2);
        textView3 = findViewById(R.id.text3);
        textView4 = findViewById(R.id.text4);

        //내꺼 체크 값 돌리기
        int[] myNum = new int[2]; // 배열생성 -> 이건 내꺼만 돌려봄 다른사람들은 그냥 클래스로 받아서 결과확인
        myNum[0] = 1;
        myNum[1] = 8;

        int[] myNum2 = new int[2]; // 테스트용
        myNum2[0] = 4;
        myNum2[1] = 7;

        int[] myNum3 = new int[2]; // 테스트용
        myNum3[0] = 10;
        myNum3[1] = 20;


        final User[] users = new User[3];

        users[0] = new User(0, 0, 0, 0, 0, 0, myNum, "");
        users[1] = new User(1, 0, 0, 0, 0, 0, myNum2, "");
        ;
        users[2] = new User(2, 0, 0, 0, 0, 0, myNum3, "");
        ;

        check(users[0]);
        check(users[1]);
        check(users[2]);

        //도중에 죽으면 result에 빼서 보내면 됨
        result(users);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 3개 선언해서 버튼 클릭하면 3개 보이게
                String str = ""; // 패 String 변수
                str = result(users);
                textView4.setText(str);
            }
        });

    }

    public User check(User user) {


        if ((user.myNum[0] == 3 && user.myNum[1] == 8) || (user.myNum[0] == 8 && user.myNum[1] == 3)) {
            user.level = 10;
            user.sum = 30;
            user.str = "삼팔광땡";

        } else if ((user.myNum[0] == 4 && user.myNum[1] == 7) || (user.myNum[0] == 7 && user.myNum[1] == 4)) {
            user.level = 2;
            user.str = "암행어사";

        } else if (user.myNum[0] == 1 && (user.myNum[1] == 3 || user.myNum[1] == 8) || user.myNum[1] == 1 && (user.myNum[0] == 3 || user.myNum[0] == 8)) { // level9는 암행어사한테잡힘
            user.level = 9;
            user.sum = user.myNum[0] + user.myNum[1];
            switch (user.sum) {
                case 4:
                    user.str = "일삼광땡";
                    user.sum = 28;
                case 9:
                    user.str = "일팔광땡";
                    user.sum = 29;
            }
        } else if ((user.myNum[0] == 9 && user.myNum[1] == 4) || (user.myNum[0] == 4 && user.myNum[1] == 9)) {
            user.level = 4;
            user.str = "멍텅구리 구사";
            user.sum = 27;
        } else if ((user.myNum[0] == 3 && user.myNum[1] == 7) || (user.myNum[0] == 7 && user.myNum[1] == 3)) {
            user.level = 1;
            user.str = "땡잡이";
        } else if ((user.myNum[0] % 10) == (user.myNum[1] % 10)) {
            user.level = 7;
            switch (user.myNum[0] % 10) {
                case 1:
                    user.str = "일떙";
                    user.sum = 1;
                    user.sum = 17;
                    break;
                case 2:
                    user.str = "이떙";
                    user.sum = 2;
                    user.sum = 18;
                    break;
                case 3:
                    user.str = "삼떙";
                    user.sum = 3;
                    user.sum = 19;
                    break;
                case 4:
                    user.str = "사떙";
                    user.sum = 4;
                    user.sum = 20;
                    break;
                case 5:
                    user.str = "오떙";
                    user.sum = 5;
                    user.sum = 21;
                    break;
                case 6:
                    user.str = "육떙";
                    user.sum = 6;
                    user.sum = 22;
                    break;
                case 7:
                    user.str = "칠떙";
                    user.sum = 7;
                    user.sum = 23;
                    break;
                case 8:
                    user.str = "팔떙";
                    user.sum = 8;
                    user.sum = 24;
                    break;
                case 9:
                    user.str = "구땡";
                    user.sum = 9;
                    user.sum = 25;
                    break;
                case 0:
                    user.level = 8; // Level7 까지 땡잡이 한테 잡힘
                    user.str = "장땡";
                    user.sum = 26;
                    break;
            }
        } else if ((user.myNum[0] % 10) == 1 || (user.myNum[1] % 10) == 1) {
            user.level = 6;
            user.sum = (user.myNum[0] + user.myNum[1]) % 10;
            switch (user.sum) {
                case 3:
                    user.str = "알리";
                    user.sum = 15;
                    break;
                case 5:
                    user.str = "독사";
                    user.sum = 14;
                    break;
                case 0:
                    user.str = "구삥";
                    user.sum = 13;
                    break;
                case 1:
                    user.str = "장삥";
                    user.sum = 12;
                    break;
                default:
                    user.level = 0;
                    user.sum = (user.myNum[0] + user.myNum[1]) % 10;
                    user.str = user.sum + "끝";
            }
        } else if ((user.myNum[0] % 10) == 4 || (user.myNum[1] % 10) == 4) {
            user.level = 5;
            user.sum = (user.myNum[0] + user.myNum[1]) % 10;
            switch (user.sum) {
                case 4:
                    user.str = "장사";
                    user.sum = 11;
                    break;
                case 0:
                    user.str = "세륙";
                    user.sum = 10;
                    break;
                case 1:
                    user.level = 3;
                    user.str = "구사";
                    user.sum = 16;
                    break;
                default:
                    user.level = 0;
                    user.sum = (user.myNum[0] + user.myNum[1]) % 10;
                    user.str = user.sum + "끝";
            }
        } else {
            user.level = 0;
            user.sum = (user.myNum[0] + user.myNum[1]) % 10;
            user.str = user.sum + "끝";
            if (user.sum == 0)
                user.str = "망통";
        }
        //return user.str;
        return user;
    }



    public String result(User[] user) { //User.length 만큼  User 정보를 받아서 셋팅 // return 1등
        boolean endFlag = false;


        int max = 0;
        int[] maxId = new int[user.length]; //maxId 체크 하기
        int cnt = 0;
        int winNum = 0;


        //다 패 올리기
        //도중에 죽을경우 리프레쉬 -> 죽은사람은 result에 못오게
        //동점일 경우 두명, 세명일경우 예외처리
        for (int i = 0; i < user.length; i++) {
            if (max <= user[i].sum) {
                max = user[i].sum;
                maxId[cnt] = i;
                winNum = i;
                cnt++; // cnt가 2이상일 확률은 족보 이하
            }
        }

        if (cnt >= 2) {
            User[] drawUser = new User[cnt];
            for (int i = 0; i < cnt; i++) {
                drawUser[i] = user[maxId[i]];
            }
            draw(drawUser);
        }
        if (max == 30) {
            user[winNum].win++;
            user[winNum].lose--;
        } else if (max == 28 || max == 29) { // 광땡
            for (int i = 0; i < user.length; i++) {
                if (user[i].level == 2) { // 암행어사
                    user[i].win++;
                    user[i].lose--;
                    winNum = i;
                    break;
                }
            }
            user[winNum].win++;
            user[winNum].lose--;
        } else if (max == 27) { // 멍구사
            draw(user); // 모든 사람 다시 시작
        } else if (max == 26) { // 장땡
            user[winNum].win++;
            user[winNum].lose--;
        } else if (17 <= max && max <= 25) { // 땡
            for (int i = 0; i < user.length; i++) {
                if (user[i].level == 1) { // 땡잡이
                    user[i].win++;
                    user[i].lose--;
                    winNum = i;
                    break;
                }
            }
            user[winNum].win++;
            user[winNum].lose--;
        } else if (max == 16) { // 구사
            draw(user);
        } else if (0 <= max && 15 <= max) {
            user[winNum].win++;
            user[winNum].lose--;
        }
        //여기서 다 패배처리 ++
        for (int i = 0; i < user.length; i++) {
            user[i].lose++;
        }
        return String.valueOf(winNum);
    }

    public void draw(User[] users) {
        // 패 다시 돌리는 명령 해주고
        // 그후에 다시 돌리기
        result(users);
    }
}


class User {
    int id;
    int win, lose, mydraw;
    int level, sum;
    int[] myNum;
    String str = "";

    public User(int id, int win, int lose, int mydraw, int level, int sum, int[] myNum, String str) {
        this.id = id;
        this.win = win;
        this.lose = lose;
        this.mydraw = mydraw;
        this.level = level;
        this.sum = sum;
        this.myNum = myNum;
        this.str = str;
    }
}