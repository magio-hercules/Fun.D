import org.jetbrains.annotations.NotNull;

public class User implements  Comparable<User> {

    private int id;

    private String name;

    private Integer cardSum;

    public User(int id, String name, int cardSum) {
        this.id = id;
        this.name = name;
        this.cardSum = cardSum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCardSum() {
        return cardSum;
    }

    public void setCardSum(Integer cardSum) {
        this.cardSum = cardSum;
    }

    @Override
    public int compareTo(@NotNull User user) {
        return cardSum.compareTo(user.getCardSum());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cardSum=" + cardSum +
                '}';
    }
}
