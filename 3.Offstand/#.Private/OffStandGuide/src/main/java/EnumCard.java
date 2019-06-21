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