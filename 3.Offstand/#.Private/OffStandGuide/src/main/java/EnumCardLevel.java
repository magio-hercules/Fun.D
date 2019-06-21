public enum EnumCardLevel {

    LEVEL28(28),       // 38광땡
    LEVEL27(27),       // 18광땡
    LEVEL26(26),       // 13광땡
    LEVEL25(25),       // 장땡
    LEVEL24(24),       // 9땡
    LEVEL23(23),       // 8땡
    LEVEL22(22),       // 7땡
    LEVEL21(21),       // 6땡
    LEVEL20(20),       // 5땡
    LEVEL19(19),       // 4땡
    LEVEL18(18),       // 3땡
    LEVEL17(17),       // 2땡
    LEVEL16(16),       // 삥땡
    LEVEL15(15),       // 알리
    LEVEL14(14),       // 독사
    LEVEL13(13),       // 구삥
    LEVEL12(12),       // 장삥
    LEVEL11(11),       // 장사
    LEVEL10(10),       // 세륙
    LEVEL09(9),        // 9끗
    LEVEL08(8),        // 8끗
    LEVEL07(7),        // 7끗
    LEVEL06(6),        // 6끗
    LEVEL05(5),        // 5끗
    LEVEL04(4),        // 4끗
    LEVEL03(3),        // 3끗
    LEVEL02(2),        // 2끗
    LEVEL01(1),        // 1끗
    LEVEL00(0),        // 망통
    DARKHORSE(-1),     // 암행어사
    DDAENGCATCHER(-2), // 땡잡이
    IDIOTGOOSA(-3),    // 멍텅구리 구사
    GOOSA(-4);         // 구사

    private int cardLevel;

    EnumCardLevel(int cardLevel) {
        this.cardLevel = cardLevel;
    }

    public int getCardLevel() {
        return cardLevel;
    }
}
