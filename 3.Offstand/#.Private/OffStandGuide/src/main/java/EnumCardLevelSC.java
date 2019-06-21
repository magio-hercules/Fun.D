public enum EnumCardLevelSC {

    LEVEL28(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 38광땡
    LEVEL27(EnumCard.ONE_KWANG, EnumCard.EIGHT_KWANG),          // 18광땡
    LEVEL26(EnumCard.ONE_KWANG, EnumCard.THREE_KWANG),          // 13광땡
    LEVEL25(EnumCard.TEN_MUNG, EnumCard.TEN),                   // 장땡
    LEVEL24(EnumCard.NINE_MUNG, EnumCard.NINE),                 // 9땡
    LEVEL23(EnumCard.EIGHT_KWANG, EnumCard.EIGHT),              // 8땡
    LEVEL22(EnumCard.SEVEN_MUNG, EnumCard.SEVEN),               // 7땡
    LEVEL21(EnumCard.SIX_MUNG, EnumCard.SIX),                   // 6땡
    LEVEL20(EnumCard.FIVE_MUNG, EnumCard.FIVE),                 // 5땡
    LEVEL19(EnumCard.FOUR_MUNG, EnumCard.FOUR),                 // 4땡
    LEVEL18(EnumCard.THREE_KWANG, EnumCard.THREE),              // 3땡
    LEVEL17(EnumCard.TWO_MUNG, EnumCard.TWO),                   // 2땡
    LEVEL16(EnumCard.ONE_KWANG, EnumCard.ONE),                  // 삥땡
    LEVEL15(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 알리
    LEVEL14(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 독사
    LEVEL13(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 구삥
    LEVEL12(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 장삥
    LEVEL11(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 장사
    LEVEL10(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 세륙
    LEVEL09(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 9끗
    LEVEL08(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 8끗
    LEVEL07(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 7끗
    LEVEL06(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 6끗
    LEVEL05(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 5끗
    LEVEL04(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 4끗
    LEVEL03(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 3끗
    LEVEL02(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 2끗
    LEVEL01(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 1끗
    LEVEL00(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG),        // 망통
    DARKHORSE(EnumCard.FOUR_MUNG, EnumCard.SEVEN_MUNG),     // 암행어사
    DDAENGCATCHER(EnumCard.THREE_KWANG, EnumCard.SEVEN_MUNG), // 땡잡이
    IDIOTGOOSA(EnumCard.FOUR_MUNG, EnumCard.NINE_MUNG),    // 멍텅구리 구사
    GOOSA(EnumCard.THREE_KWANG, EnumCard.EIGHT_KWANG);         // 구사

    private int cardLevel;
    private EnumCard cardNo1;
    private EnumCard cardNo2;

    EnumCardLevelSC(EnumCard cardNo1, EnumCard cardNo2) {
        this.cardNo1 = cardNo1;
        this.cardNo2 = cardNo2;
    }

    public int getCardLevel() {
        return cardLevel;
    }
}
