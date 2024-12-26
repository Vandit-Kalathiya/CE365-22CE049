import java.util.ArrayList;
import java.util.*;

public class pr2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Number of input symbols : ");
        int ip = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter symbols : ");
        List<String> symbols = new ArrayList<>();
        for (int i = 0; i < ip; i++) {
            String s = sc.nextLine();
            symbols.add(s);
        }

        System.out.print("Number of states : ");
        int states = sc.nextInt();

        System.out.print("Number of accepting states : ");
        int a_states = sc.nextInt();

        List<Integer> accepting_states = new ArrayList<>();
        for (int i = 0; i < a_states; i++) {
            int t = sc.nextInt();
            accepting_states.add(t);
        }

        int[][] transition_table = new int[states][ip];

        for (int i = 0; i < transition_table.length; i++) {
            for (int j = 0; j < transition_table[0].length; j++) {
                System.out.print((i + 1) + " to " + (j == 0 ? "a" : "b") + " : ");
                int t = sc.nextInt();
                transition_table[i][j] = t;
            }
        }
        sc.nextLine();
        System.out.print("Enter input string : ");
        String str = sc.nextLine();

        int curr = 1;
        if (str == "null") {
            if (accepting_states.contains(curr)) {
                System.out.println("Valid");
            } else
                System.out.println("Invalid");
        } else {
            for (int i = 0; i < transition_table.length; i++) {
                for (int j = 0; j < transition_table[0].length; j++) {
                    System.out.print(transition_table[i][j] + " ");
                }
                System.out.println();
            }

            for (int i = 0; i < str.length(); i++) {
                int from = curr - 1, to = str.charAt(i) == 'a' ? 0 : 1;
                // System.out.println(transition_table[from][to]);
                curr = transition_table[from][to];
            }
            System.out.println(curr);
            if (accepting_states.contains(curr)) {
                System.out.println("Valid");
            } else
                System.out.println("Invalid");
        }
    }
}
