import java.util.ArrayList;
import java.util.Collections;

public class SortTest {

    private static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) {
        users.add(new User(3, "삼번", 2));
        users.add(new User(2, "이번", 7));
        users.add(new User(5, "오번", 5));
        users.add(new User(1, "일번", 9));
        System.out.println("=== 소팅 전 ===");
        for (User user : users) System.out.println(user);
        Collections.sort(users);
        Collections.reverse(users);
        System.out.println("=== 소팅 후 ===");
        for (User user : users) System.out.println(user);

    }
}
