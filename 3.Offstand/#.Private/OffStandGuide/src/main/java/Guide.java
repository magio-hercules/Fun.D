import jdk.internal.net.http.common.Pair;
import jdk.javadoc.internal.doclets.toolkit.util.Utils;

public class Guide {

    /*
    1 - 2, 3, 4, 8, 9, 10 ,11, 12, 14, 19, 20
    2 - 1, 11, 12
    3 - 1, 7, 8, 13
    4 - 1, 6, 9, 10, 11, 14, 16, 19, 20
    5 - 15
    6 - 4, 14, 16
    7 - 3, 17
    8 - 1, 3, 18
    9 - 1, 4, 11, 14, 19
    10- 1, 4, 11, 14, 20
    11 - 1, 2, 4, 9, 10, 12, 14, 19, 20
    12 - 1, 2, 11
    13 - 3
    14 - 1, 4, 10, 11, 16, 19, 20
    15 - 5
    16 - 4, 6, 14
    17 - 7
    18 - 8
    19 - 1, 4, 9, 11, 14
    20 - 1, 4, 10, 11, 14
    */

    /*
    1 - 2, 3, 4, 8, 9, 10 ,11, 12, 14, 19, 20
    2 - 11, 12
    3 - 7, 8, 13
    4 - 6, 9, 10, 11, 14, 16, 19, 20
    5 - 15
    6 - 14, 16
    7 - 17
    8 - 18
    9 - 11, 14, 19
    10 - 11, 14, 20
    11 - 12, 14, 19, 20
    12 -
    13 -
    14 - 16, 19, 20
    15 -
    16 -
    17 -
    18 -
    19 -
    20 -
    */


    public static void main(String[] args) {
        User 유재석 = new User("이승철", new Pair<>(1,10));
        User 정준하 = new User("정준하", new Pair<>(3,7));
        User 노홍철 = new User("노홍철", new Pair<>(4,9));
        User 박명수 = new User("박명수", new Pair<>(8,13));

        test(유재석);

    }

    private static void test(User user) {

        switch (user.getCardLevelSC()) {
            case LEVEL00:
                break;

        }
    }

}

