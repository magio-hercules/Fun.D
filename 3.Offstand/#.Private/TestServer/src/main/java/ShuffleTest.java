import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ShuffleTest {

    public static void main(String[] args) {
        ArrayList<Integer> numList = new ArrayList<>();
        for(int i = 1; i < 21; i++) {
            numList.add(i);
        }
        Collections.shuffle(numList);

        System.out.println(numList);
    }
}
