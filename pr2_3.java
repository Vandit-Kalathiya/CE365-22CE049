import java.util.*;

public class pr2_3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        int states = 2;
        List<Integer> accepting_states = Arrays.asList(2);

        int[][] transition_table = new int[states][36];

        // Transition table
        for (int i = 0; i < transition_table.length; i++) {
            for (int j = 0; j < transition_table[0].length; j++) {
                if (i == 0 && j < 26) {
                    transition_table[i][j] = 2;
                } else if (i == 1) {
                    transition_table[i][j] = 2;
                } else {
                    transition_table[i][j] = 1;
                }
            }
        }

        System.out.println("Transition table created automatically.");

        while (!exit) {
            System.out.print("Enter input string (or 'exit' to quit) : ");
            String str = sc.nextLine();

            if (str.equalsIgnoreCase("exit")) {
                exit = true;
            } else {
                boolean isValid = validateString(str, transition_table, accepting_states);
                System.out.println(isValid ? "Valid" : "Invalid");
            }
        }
    }

    private static boolean validateString(String str, int[][] transition_table, List<Integer> accepting_states) {
        if (str == null || str.isEmpty() || !Character.isLowerCase(str.charAt(0))) {
            return false;
        }

        int curr = 1;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            int index;
            if (Character.isLowerCase(c)) {
                index = c - 'a';
            } else if (Character.isDigit(c)) {
                index = c - '0' + 26;
            } else {
                return false;
            }
            curr = transition_table[curr - 1][index];
        }

        return accepting_states.contains(curr);
    }
}