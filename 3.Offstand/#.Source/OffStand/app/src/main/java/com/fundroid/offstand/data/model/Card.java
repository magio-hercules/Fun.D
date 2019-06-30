package com.fundroid.offstand.data.model;


import io.reactivex.Completable;

import static com.fundroid.offstand.data.model.Card.EnumCard.FOUR;
import static com.fundroid.offstand.data.model.Card.EnumCard.FOUR_MUNG;
import static com.fundroid.offstand.data.model.Card.EnumCard.NINE_MUNG;
import static com.fundroid.offstand.data.model.Card.EnumCard.ONE;
import static com.fundroid.offstand.data.model.Card.EnumCard.SIX_MUNG;
import static com.fundroid.offstand.data.model.Card.EnumCard.TEN_MUNG;
import static com.fundroid.offstand.data.model.Card.EnumCard.TWO_MUNG;
import static com.fundroid.offstand.data.model.User.EnumStatus.DIE;

public class Card {

    public enum EnumCardLevel {
        LEVEL0(0),       // 끗 or 망통                     sum : 0 ~ 9
        LEVEL1(1),       // 장사 or 세륙                    sum : 세륙 10 장사 11
        LEVEL2(2),       // 알리 or 독사 or 구삥 or 장삥      sum : 장삥 12 구삥 13 독사 14 알리 15
        LEVEL3(3),       // 구사                           sum : 16     * 최고점일 경우 재경기
        LEVEL4(4),       // 1땡 ~ 9땡                     sum : 17 ~ 25
        LEVEL5(5),       // 땡잡이                         sum : 0 or 26    * 26 되는 조건 : 상대방 LEVEL 중 4가 존재할 경우
        LEVEL6(6),       // 장땡                          sum : 27
        LEVEL7(7),       // 멍텅구리 구사                    sum : 28     * 최고점일 경우 재경기
        LEVEL8(8),       // 일삼광떙 or 일팔광땡             sum : 29
        LEVEL9(9),       // 암행어사                        sum : 1 or 30    * 30 되는 조건 : 상대방 LEVEL 중 8가 존재할 경우
        LEVEL10(10),     // 삼팔광땡                       sum : 31
        LEVEL_1(-1);     // 다이                          sum : -1
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

    public static Completable setCardValue(User user) {
        return checkLevel0(user)
                .andThen(checkLevel1and3(user))
                .andThen(checkLevel2(user))
                .andThen(checkLevel4and6(user))
                .andThen(checkLevel5(user))
                .andThen(checkLevel7(user))
                .andThen(checkLevel8(user))
                .andThen(checkLevel9(user))
                .andThen(checkLevel10(user))
                .andThen(checkLevel_1(user));
    }

    private static Completable checkLevel_1(User user) {
        if (user.getStatus() == DIE.getEnumStatus()) {
            user.setCardSum(-1);
            user.setCardLevel(EnumCardLevel.LEVEL_1.getCardLevel());
        }
        return Completable.complete();
    }

    private static Completable checkLevel0(User user) {
        user.setCardLevel(EnumCardLevel.LEVEL0.getCardLevel());
        user.setCardSum((user.getCards().first + user.getCards().second) % 10);
        return Completable.complete();
    }

    private static Completable checkLevel1and3(User user) {
        if (user.getCards().first % 10 == 4) {
            switch (user.getCards().second % 10) {
                case 6:
                    user.setCardLevel(EnumCardLevel.LEVEL1.getCardLevel());
                    user.setCardSum(10);
                    break;
                case 0:
                    user.setCardLevel(EnumCardLevel.LEVEL1.getCardLevel());
                    user.setCardSum(11);
                    break;
                case 9:
                    user.setCardLevel(EnumCardLevel.LEVEL3.getCardLevel());
                    user.setCardSum(16);
                    break;
            }
        } else if (user.getCards().first == SIX_MUNG.getCardNo() && user.getCards().second == FOUR.getCardNo()) {
            user.setCardLevel(EnumCardLevel.LEVEL1.getCardLevel());
            user.setCardSum(10);
        } else if (user.getCards().first == TEN_MUNG.getCardNo() && user.getCards().second == FOUR.getCardNo()) {
            user.setCardLevel(EnumCardLevel.LEVEL1.getCardLevel());
            user.setCardSum(11);
        } else if (user.getCards().first == NINE_MUNG.getCardNo() && user.getCards().second == FOUR.getCardNo()) {
            user.setCardLevel(EnumCardLevel.LEVEL3.getCardLevel());
            user.setCardSum(16);
        }
        return Completable.complete();
    }

    private static Completable checkLevel2(User user) {
        if (user.getCards().first % 10 == 1) {
            switch (user.getCards().second % 10) {
                case 0:
                    user.setCardLevel(EnumCardLevel.LEVEL2.getCardLevel());
                    user.setCardSum(12);
                    break;
                case 9:
                    user.setCardLevel(EnumCardLevel.LEVEL2.getCardLevel());
                    user.setCardSum(13);
                    break;
                case 4:
                    user.setCardLevel(EnumCardLevel.LEVEL2.getCardLevel());
                    user.setCardSum(14);
                    break;
                case 2:
                    user.setCardLevel(EnumCardLevel.LEVEL2.getCardLevel());
                    user.setCardSum(15);
                    break;
            }
        } else if (user.getCards().first == TEN_MUNG.getCardNo() && user.getCards().second == ONE.getCardNo()) {
            user.setCardLevel(EnumCardLevel.LEVEL2.getCardLevel());
            user.setCardSum(12);
        } else if (user.getCards().first == NINE_MUNG.getCardNo() && user.getCards().second == ONE.getCardNo()) {
            user.setCardLevel(EnumCardLevel.LEVEL2.getCardLevel());
            user.setCardSum(13);
        } else if (user.getCards().first == FOUR_MUNG.getCardNo() && user.getCards().second == ONE.getCardNo()) {
            user.setCardLevel(EnumCardLevel.LEVEL2.getCardLevel());
            user.setCardSum(14);
        } else if (user.getCards().first == TWO_MUNG.getCardNo() && user.getCards().second == ONE.getCardNo()) {
            user.setCardLevel(EnumCardLevel.LEVEL2.getCardLevel());
            user.setCardSum(15);
        }


        return Completable.complete();
    }

    private static Completable checkLevel4and6(User user) {
        if (user.getCards().first % 10 == user.getCards().second % 10) {
            if (user.getCards().first.equals(TEN_MUNG.getCardNo())) {
                user.setCardLevel(EnumCardLevel.LEVEL6.getCardLevel());
                user.setCardSum(user.getCards().first + 17);
            } else {
                user.setCardLevel(EnumCardLevel.LEVEL4.getCardLevel());
                user.setCardSum(user.getCards().first + 16);
            }

        }
        return Completable.complete();
    }

    private static Completable checkLevel5(User user) {
        if (user.getCards().first.equals(EnumCard.THREE_KWANG.getCardNo()) && user.getCards().second.equals(EnumCard.SEVEN_MUNG.getCardNo())) {
            user.setCardLevel(EnumCardLevel.LEVEL5.getCardLevel());
            user.setCardSum(26);
        }
        return Completable.complete();
    }

    private static Completable checkLevel7(User user) {
        if (user.getCards().first.equals(FOUR_MUNG.getCardNo()) && user.getCards().second.equals(NINE_MUNG.getCardNo())) {
            user.setCardLevel(EnumCardLevel.LEVEL7.getCardLevel());
            user.setCardSum(28);
        }
        return Completable.complete();
    }

    private static Completable checkLevel8(User user) {
        if (user.getCards().first.equals(EnumCard.ONE_KWANG.getCardNo()) && (user.getCards().second.equals(EnumCard.THREE_KWANG.getCardNo()) || user.getCards().second.equals(EnumCard.EIGHT_KWANG.getCardNo()))) {
            user.setCardLevel(EnumCardLevel.LEVEL8.getCardLevel());
            user.setCardSum(29);    // 일삼광땡, 일팔광땡 같이 나올 경우가 없음
        }
        return Completable.complete();
    }

    private static Completable checkLevel9(User user) {
        if (user.getCards().first.equals(FOUR_MUNG.getCardNo()) && user.getCards().second.equals(EnumCard.SEVEN_MUNG.getCardNo())) {
            user.setCardLevel(EnumCardLevel.LEVEL9.getCardLevel());
            user.setCardSum(30);
        }
        return Completable.complete();
    }

    private static Completable checkLevel10(User user) {
        if (user.getCards().first.equals(EnumCard.THREE_KWANG.getCardNo()) && user.getCards().second.equals(EnumCard.EIGHT_KWANG.getCardNo())) {
            user.setCardLevel(EnumCardLevel.LEVEL10.getCardLevel());
            user.setCardSum(31);
        }
        return Completable.complete();
    }

}
