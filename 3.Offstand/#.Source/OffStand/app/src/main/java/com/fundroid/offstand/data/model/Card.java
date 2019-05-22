package com.fundroid.offstand.data.model;


import androidx.core.util.Pair;

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

    public static Observable<Pair<Integer, Integer>> setCardValue(Pair<Integer, Integer> levelAndSum) {

        return Observable.just(levelAndSum);
    }

    public static Observable<Pair<Integer, Integer>> checkLevel0(Pair<Integer, Integer> cards) {
        if ((cards.first == EnumCard.THREE_KWANG.getCardNo() && cards.second == EnumCard.EIGHT_KWANG.getCardNo()) || (cards.first == EnumCard.EIGHT_KWANG.getCardNo() && cards.second == EnumCard.THREE_KWANG.getCardNo())) {
            new Pair<>(EnumCardLevel.LEVEL10.getCardLevel(), 30);
        } else {

        }
        return Observable.just(cards);
    }

    public static Observable<Pair<Integer, Integer>> checkLeve9(Pair<Integer, Integer> cards) {
        if ((cards.first == EnumCard.THREE_KWANG.getCardNo() && cards.second == EnumCard.EIGHT_KWANG.getCardNo()) || (cards.first == EnumCard.EIGHT_KWANG.getCardNo() && cards.second == EnumCard.THREE_KWANG.getCardNo())) {
            new Pair<>(EnumCardLevel.LEVEL10.getCardLevel(), 30);
        } else {

        }
        return Observable.just(cards);
    }

    public static Observable<Pair<Integer, Integer>> checkLeve8(Pair<Integer, Integer> cards) {
        if ((cards.first == EnumCard.THREE_KWANG.getCardNo() && cards.second == EnumCard.EIGHT_KWANG.getCardNo()) || (cards.first == EnumCard.EIGHT_KWANG.getCardNo() && cards.second == EnumCard.THREE_KWANG.getCardNo())) {
            new Pair<>(EnumCardLevel.LEVEL10.getCardLevel(), 30);
        } else {

        }
        return Observable.just(cards);
    }

    public static Observable<Pair<Integer, Integer>> checkLeve7(Pair<Integer, Integer> cards) {
        if ((cards.first == EnumCard.THREE_KWANG.getCardNo() && cards.second == EnumCard.EIGHT_KWANG.getCardNo()) || (cards.first == EnumCard.EIGHT_KWANG.getCardNo() && cards.second == EnumCard.THREE_KWANG.getCardNo())) {
            new Pair<>(EnumCardLevel.LEVEL10.getCardLevel(), 30);
        } else {

        }
        return Observable.just(cards);
    }

    public static Observable<Pair<Integer, Integer>> checkLeve6(Pair<Integer, Integer> cards) {
        if ((cards.first == EnumCard.THREE_KWANG.getCardNo() && cards.second == EnumCard.EIGHT_KWANG.getCardNo()) || (cards.first == EnumCard.EIGHT_KWANG.getCardNo() && cards.second == EnumCard.THREE_KWANG.getCardNo())) {
            new Pair<>(EnumCardLevel.LEVEL10.getCardLevel(), 30);
        } else {

        }
        return Observable.just(cards);
    }

    public static Observable<Pair<Integer, Integer>> checkLeve5(Pair<Integer, Integer> cards) {
        if ((cards.first == EnumCard.THREE_KWANG.getCardNo() && cards.second == EnumCard.EIGHT_KWANG.getCardNo()) || (cards.first == EnumCard.EIGHT_KWANG.getCardNo() && cards.second == EnumCard.THREE_KWANG.getCardNo())) {
            new Pair<>(EnumCardLevel.LEVEL10.getCardLevel(), 30);
        } else {

        }
        return Observable.just(cards);
    }

    public static Observable<Pair<Integer, Integer>> checkLeve4(Pair<Integer, Integer> cards) {
        if ((cards.first == EnumCard.THREE_KWANG.getCardNo() && cards.second == EnumCard.EIGHT_KWANG.getCardNo()) || (cards.first == EnumCard.EIGHT_KWANG.getCardNo() && cards.second == EnumCard.THREE_KWANG.getCardNo())) {
            new Pair<>(EnumCardLevel.LEVEL10.getCardLevel(), 30);
        } else {

        }
        return Observable.just(cards);
    }

    public static Observable<Pair<Integer, Integer>> checkLeve3(Pair<Integer, Integer> cards) {
        if ((cards.first == EnumCard.THREE_KWANG.getCardNo() && cards.second == EnumCard.EIGHT_KWANG.getCardNo()) || (cards.first == EnumCard.EIGHT_KWANG.getCardNo() && cards.second == EnumCard.THREE_KWANG.getCardNo())) {
            new Pair<>(EnumCardLevel.LEVEL10.getCardLevel(), 30);
        } else {

        }
        return Observable.just(cards);
    }

    public static Observable<Pair<Integer, Integer>> checkLeve2(Pair<Integer, Integer> cards) {
        if ((cards.first == EnumCard.FOUR_MUNG.getCardNo() && cards.second == EnumCard.SEVEN_MUNG.getCardNo()) || (cards.first == EnumCard.SEVEN_MUNG.getCardNo() && cards.second == EnumCard.FOUR_MUNG.getCardNo())) {
            new Pair<>(EnumCardLevel.LEVEL2.getCardLevel(), 30);
        } else {

        }
        return Observable.just(cards);
    }

}
