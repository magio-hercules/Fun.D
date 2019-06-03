package com.fundroid.offstand.data.model;


import androidx.core.util.Pair;

import com.annimon.stream.Stream;
import com.fundroid.offstand.model.User;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class Card {

    public enum EnumCardLevel {
        LEVEL0(0),       // 끗 or 망통
        LEVEL1(1),       // 땡잡이
        LEVEL2(2),       // 암행어사
        LEVEL3(3),       // 구사
        LEVEL4(4),       // 멍텅구리 구사
        LEVEL5(5),       // 장사 or 세륙
        LEVEL6(6),       // 알리 or 독사 or 구삥 or 장삥
        LEVEL7(7),       // 1땡 ~ 9땡
        LEVEL8(8),       // 장땡
        LEVEL9(9),       // 일삼광떙 or 일팔광땡
        LEVEL10(10);     // 삼팔광땡
        private int cardLevel;

        EnumCardLevel(int cardLevel) {
            this.cardLevel = cardLevel;
        }

        public int getCardLevel() {
            return cardLevel;
        }
    }

    public enum EnumCard {

        ONE_KWANG(1), TWO_MUNG(2), THREE_KWANG(3), FOUR_MUNG(4), FIVE_MUNG(5),
        SIX_MUNG(6), SEVEN_MUNG(7), EIGHT_KWANG(8), NINE_MUNG(9), TEN_MUNG(10),
        ONE(11), TWO(12), THREE(13), FOUR(14), FIVE(15),
        SIX(16), SEVEN(17), EIGHT(18), NINE(19), TEN(20);

        private int cardNo;

        EnumCard(int cardNo) {
            this.cardNo = cardNo;
        }

        public int getCardNo() {
            return cardNo;
        }
    }

    //Todo 땡잡이랑 암행어사 sum?
    public static Completable setCardValue(User user) {
        return checkLevel0(user)
                .andThen(checkLevel10(user))
                .andThen(checkLeve2(user))
                .andThen(checkLeve9(user))
                .andThen(checkLeve4(user))
                .andThen(checkLeve1(user))
                .andThen(checkLeve7(user))
                .andThen(checkLeve6(user))
                .andThen(checkLeve5(user));
    }

    public static Completable setRank() {

        return Completable.complete();
    }

    private static Completable checkLevel10(User user) {
        if (user.getCards().first.equals(EnumCard.THREE_KWANG) && user.getCards().second.equals(EnumCard.EIGHT_KWANG)) {
            user.setCardLevel(EnumCardLevel.LEVEL10.getCardLevel());
            user.setCardSum(30);
        }
        return Completable.complete();
    }

    private static Completable checkLeve2(User user) {
        if (user.getCards().first.equals(EnumCard.FOUR_MUNG) && user.getCards().second.equals(EnumCard.SEVEN_MUNG)) {
            user.setCardLevel(EnumCardLevel.LEVEL2.getCardLevel());
            user.setCardSum(-1);
        }
        return Completable.complete();
    }

    private static Completable checkLeve9(User user) {
        if (user.getCards().first.equals(EnumCard.ONE_KWANG) && (user.getCards().second.equals(EnumCard.THREE_KWANG) || user.getCards().second.equals(EnumCard.EIGHT_KWANG))) {
            user.setCardLevel(EnumCardLevel.LEVEL9.getCardLevel());
            user.setCardSum(29);    // 일삼광땡, 일팔광땡 같이 나올 경우가 없음
        }
        return Completable.complete();
    }

    private static Completable checkLeve4(User user) {
        if (user.getCards().first.equals(EnumCard.FOUR_MUNG) && user.getCards().second.equals(EnumCard.NINE_MUNG)) {
            user.setCardLevel(EnumCardLevel.LEVEL4.getCardLevel());
            user.setCardSum(27);
        }
        return Completable.complete();
    }

    private static Completable checkLeve1(User user) {
        if (user.getCards().first.equals(EnumCard.THREE_KWANG) && user.getCards().second.equals(EnumCard.EIGHT_KWANG)) {
            user.setCardLevel(EnumCardLevel.LEVEL1.getCardLevel());
            user.setCardSum(-1);
        }
        return Completable.complete();
    }

    private static Completable checkLeve7(User user) {
        if (user.getCards().first % 10 == user.getCards().second % 10) {
            if (user.getCards().first.equals(EnumCard.TEN_MUNG)) {
                user.setCardLevel(EnumCardLevel.LEVEL8.getCardLevel());
            } else {
                user.setCardLevel(EnumCardLevel.LEVEL7.getCardLevel());
            }
            user.setCardSum(user.getCards().first + 16);
        }
        return Completable.complete();
    }

    private static Completable checkLeve6(User user) {
        if (user.getCards().first % 10 == 1) {
            user.setCardLevel(EnumCardLevel.LEVEL6.getCardLevel());
            switch (user.getCards().second % 10) {
                case 2:
                    user.setCardSum(15);
                    break;
                case 4:
                    user.setCardSum(14);
                    break;
                case 9:
                    user.setCardSum(13);
                    break;
                case 0:
                    user.setCardSum(12);
                    break;
                default:
                    user.setCardLevel(EnumCardLevel.LEVEL0.getCardLevel());
                    user.setCardSum((user.getCards().first + user.getCards().second) % 10);
                    break;
            }
        }
        return Completable.complete();
    }

    private static Completable checkLeve5(User user) {
        if (user.getCards().first % 10 == 4) {
            user.setCardLevel(EnumCardLevel.LEVEL5.getCardLevel());
            switch (user.getCards().second % 10) {
                case 9:
                    user.setCardLevel(EnumCardLevel.LEVEL3.getCardLevel());
                    user.setCardSum(16);
                    break;
                case 0:
                    user.setCardSum(11);
                    break;
                case 6:
                    user.setCardSum(10);
                    break;
                default:
                    user.setCardLevel(EnumCardLevel.LEVEL0.getCardLevel());
                    user.setCardSum((user.getCards().first + user.getCards().second) % 10);
                    break;
            }
        }
        return Completable.complete();
    }

    private static Completable checkLevel0(User user) {
        user.setCardLevel(EnumCardLevel.LEVEL0.getCardLevel());
        user.setCardSum((user.getCards().first + user.getCards().second) % 10);
        return Completable.complete();
    }

}
