import jdk.internal.net.http.common.Pair;

public class User {
    private String name;
    private Pair<Integer, Integer> cards;
    private EnumCardLevel cardLevel;
    private EnumCardLevelSC cardLevelSC;
    private int rank;

    public User(String name, Pair<Integer, Integer> cards) {
        this.name = name;
        this.cards = cards;

    }

    public Pair<Integer, Integer> getCards() {
        return cards;
    }

    public void setCardLevel(EnumCardLevel cardLevel) {
        this.cardLevel = cardLevel;
    }

    public EnumCardLevelSC getCardLevelSC() {
        return cardLevelSC;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
