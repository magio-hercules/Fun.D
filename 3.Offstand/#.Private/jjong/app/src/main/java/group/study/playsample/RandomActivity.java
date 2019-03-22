package group.study.playsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class RandomActivity extends Activity {
    public static int MAX = 20;

    Button button_main;
    Button button_random;
    Button button_test;


//    TextView text1;
//    TextView text2;
    EditText editText;

    int[][] arrCard;

    String card1, card2;

    public RandomActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);


//        text1 = (TextView) findViewById(R.id.text_card1);
//        text2 = (TextView) findViewById(R.id.text_card2);

        editText = (EditText) findViewById(R.id.editText);


        button_main = (Button) findViewById(R.id.button_main);
        button_main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RandomActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        button_random = (Button) findViewById(R.id.button_random);
        button_random.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initArray();
                makeRandomCard();
            }
        });

        button_test = (Button) findViewById(R.id.button_test);
        button_test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                test();
            }
        });

        initArray();
    }

    private void test() {
    }

    private void makeRandomNumber() {
        double d = Math.random(); // 0 <= d < 1

        Random rnd = new Random();
        int n1 = rnd.nextInt(20);
        int n2 = rnd.nextInt(20);

        if (n1 == n2) {
            n2 = rnd.nextInt(20);
        }

        String val = "";
        int tempNum = n1;
        if (tempNum/11 > 0) {
            val = String.valueOf(tempNum%10 == 0 ? 10 : tempNum%10) + "-2";
        } else {
            val = String.valueOf(tempNum) + "-1";
        }
        card1 = val;

        tempNum = n2;
        if (tempNum/11 > 0) {
            val = String.valueOf(tempNum%10 == 0 ? 10 : tempNum%10) + "-2";
        } else {
            val = String.valueOf(tempNum) + "-1";
        }
        card2 = val;

        Log.d("[RANDOM]", "Card1 : " + card1);
        Log.d("[RANDOM]", "Card2 : " + card2);
    }

    private void initArray() {
        arrCard = new int[MAX+1][2];
        for(int i=0; i<MAX;i++) {
            arrCard[i][0] = i + 1;
            arrCard[i][1] = 0;
        }
    }

    private void makeRandomCard() {
        Random rnd = new Random();

        int randomCount = 20;
        int[] arrResult = new int[randomCount];
        int index = 0;

        int temp;
        while(index < randomCount) {
            temp = rnd.nextInt(20);

            if (arrCard[temp][1] == 0) {
                arrResult[index++] = arrCard[temp][0];
                arrCard[temp][1] = 1;
            }
        }

        String tempText = "";
        String dashText = "";
        String val = "";
        for(int i=0; i<randomCount;i++) {
            Log.d("[PLAY]", "arrResult : " + i + ", " + arrResult[i] );

            if (arrResult[i]/11 > 0) {
                val = String.valueOf(arrResult[i]%10 == 0 ? 10 : arrResult[i]%10) + "-2";
            } else {
                val = String.valueOf(arrResult[i]) + "-1";
            }

            if (i % 2 == 0) {
                tempText += (i/2) + 1 + "번째 유저 (" + val;
            } else {
                tempText += ", " + val + ")\n\n";
            }
        }
        editText.setText(tempText);
    }
}
